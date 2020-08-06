package com.poweredbypace.pace.event;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.TProductOptionValue;
import com.poweredbypace.pace.domain.layout.CameoSetElement;
import com.poweredbypace.pace.domain.layout.ImageStampElement;
import com.poweredbypace.pace.domain.layout.TextStampElement;
import com.poweredbypace.pace.domain.mail.EmailTemplate;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.repository.TProductOptionValueRepository;
import com.poweredbypace.pace.service.BatchService;
import com.poweredbypace.pace.service.EmailService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.ScreenshotService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.service.StoreService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.util.HibernateUtil;
import com.poweredbypace.pace.util.JsonUtil;
import com.poweredbypace.pace.util.SpringContextUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendEmailEventHander implements EventHandler {
	
	private final Log log = LogFactory.getLog(getClass());

	public static class Params {
		public String cc;
		public String bcc;
		public String to;
		public String toName;
		public String from;
		public String fromName;
		public String replyTo;
		public String replyToName;
		public String template;
		public Long smtpServerId;
		public String condition;
		public Class<?> modelWrapperClass;
	}
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private Configuration freemarkerConfig;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BatchService batchService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ScreenshotService screenshotService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private StoreService storeService;
	
	@Autowired
	private TProductOptionValueRepository tProductOptionValueRepository;
	
	
	private Params params;
	
	public SendEmailEventHander(String paramsJson) {
		this.params = JsonUtil.deserialize(paramsJson, Params.class);
	}
	
	@Override
	public void handleEvent(ApplicationEvent e) throws IOException, TemplateException, MessagingException, InterruptedException, URISyntaxException {
		
		EmailTemplate template = emailService.getEmailTemplate(params.template);
		
		Map<String,Object> model = new HashMap<String, Object>();
		model.put("event", e);
		fillEmailModelFromEvent(e, model);
		
		Object templateModel = model;
		if (params.modelWrapperClass!=null) {
			try {
				templateModel = SpringContextUtil.getApplicationContext().getBean(params.modelWrapperClass, e);
			} catch (Exception e1) {
				log.error("Cannot instantiate class "+params.modelWrapperClass.getName(), e1);
			} 
		}
		
		String body = FreeMarkerTemplateUtils.processTemplateIntoString(
				freemarkerConfig.getTemplate(template.getName()), templateModel);
		
		long smtpServerId = params.smtpServerId!=null ? params.smtpServerId : 1;
		
		JavaMailSender mailSender = (JavaMailSender) SpringContextUtil.getApplicationContext()
				.getBean("JavaMailSender" + smtpServerId);
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
		
		StandardEvaluationContext context = new StandardEvaluationContext();
		for(String key:model.keySet()) {
			context.setVariable(key, model.get(key));
		}
		if (templateModel!=null && templateModel instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) templateModel;
			for(String key:map.keySet()) {
				context.setVariable(key, map.get(key));
			}
		}
			
		String to = getExpressionValue(params.to, context);
		String from = getExpressionValue(params.from, context);
		String fromName = getExpressionValue(params.fromName, context);
		message.setTo(to);
		message.setFrom(from, fromName);
		
		if (params.cc!=null) {
			String cc = getExpressionValue(params.cc, context);
			message.setCc(cc.split(","));
		}
		if (params.bcc!=null) {
			String bcc = getExpressionValue(params.bcc, context);
			message.setBcc(bcc.split(","));
		}
		
		if (params.replyTo!=null && params.replyToName!=null) {
			String replyTo = getExpressionValue(params.replyTo, context);
			String replyToName = getExpressionValue(params.replyToName, context);
			message.setReplyTo(replyTo, replyToName);
		}
		
		Map<String, File> screenshots = getScreenshots(body);
		Map<String, File> attachments = getAttachments(body);
		
		body = body.replaceAll("<screenshot.*>.*</screenshot>", "");
		body = body.replaceAll("<attachment.*>.*</attachment>", "");
		
		message.setText(body, true);
		
		Template subjectTemplate = new Template(template.getName()+"-Subject", 
				new StringReader(template.getTranslatedSubject()), freemarkerConfig);
		String subject = FreeMarkerTemplateUtils.processTemplateIntoString(subjectTemplate, templateModel);
		message.setSubject(subject);
		
		for(String key:screenshots.keySet()) {
			message.addAttachment(key, screenshots.get(key));
		}
		for(String key:attachments.keySet()) {
			message.addAttachment(key, attachments.get(key));
		}
		mailSender.send(mimeMessage);
		
		//TODO: delete files later
//		for(String key:screenshots.keySet()) {
//			screenshots.get(key).delete();
//		}
//		for(String key:attachments.keySet()) {
//			attachments.get(key).delete();
//		}
		
	}
	
	private String getExpressionValue(String expression, StandardEvaluationContext context) {
		ExpressionParser parser = new SpelExpressionParser();
		String result = null;
		try {
			Expression exp = parser.parseExpression(expression);
			result = exp.getValue(context, String.class);
		} catch (Exception ex) {
			result = expression;
		}
		return result;
	}
	
	private void fillEmailModelFromEvent(ApplicationEvent e, Map<String,Object> model) {
		if (e instanceof OrderEvent) {
			Order o = orderService.get( ((OrderEvent)e).getOrderId() );
			model.put("order", o);
		}
			
		if (e instanceof ProductEvent) {
			Product p = productService.findOne( ((ProductEvent)e).getProductId() );
			model.put("product", p);
			
			Batch batch = p.getBatch();
			if (batch==null) {
				batch = batchService.getPendingBatch();
			}
			model.put("batch", batch);
			
			for(ProductOption<?> po:p.getProductOptions()) {
				Object value = HibernateUtil.unproxy(po.getValue());
				
				if (value instanceof ImageStampElement) {
					ImageStampElement el = (ImageStampElement) value;
					String code = po.getPrototypeProductOption().getEffectiveCode();
					
					String absUrl = p.getStore().getStorageUrl() + 
							ApplicationConstants.ORIGINAL_IMAGE_PATH + el.getStampUrl();
					
					model.put("stamp_url_" + code, el.getStampUrl());
					model.put("stamp_url_" + code + "_absolute", absUrl);
					
					String positionCode = el.getPositionCode();
					if (positionCode!=null) {
						List<TProductOptionValue> val = tProductOptionValueRepository.findByCode(positionCode);
						if (val!=null && val.size()>0) {
							model.put("stamp_position_" + code, val.get(0).getDisplayName());
						}
					}	
				}
				
				if (value instanceof TextStampElement) {
					TextStampElement el = (TextStampElement) value;
					String code = po.getPrototypeProductOption().getEffectiveCode();
					
					String positionCode = el.getPositionCode();
					if (positionCode!=null) {
						List<TProductOptionValue> val = tProductOptionValueRepository.findByCode(positionCode);
						if (val!=null && val.size()>0) {
							model.put("stamp_position_" + code, val.get(0).getDisplayName());
						}
					}	
				}
				
				if (value instanceof CameoSetElement) {
					CameoSetElement el = (CameoSetElement) value;
					String code = po.getPrototypeProductOption().getEffectiveCode();
					String positionCode = el.getPositionCode();
					if (positionCode!=null) {
						List<TProductOptionValue> val = tProductOptionValueRepository.findByCode(positionCode);
						if (val!=null && val.size()>0) {
							model.put("cameo_position_" + code, val.get(0).getDisplayName());
						}
					}
				}	
			}
		}
		
		if (e instanceof UserEvent) {
			User u = userService.get( ((UserEvent)e).getUser().getId() );
			model.put("user", u);
			model.put("store", storeService.getUserStore(u));
		}
	}
	
	private Map<String, File> getScreenshots(String body) throws IOException, InterruptedException, URISyntaxException, MessagingException {
		Map<String, File> result = new HashMap<String, File>();
		
		Pattern p = Pattern.compile("<screenshot\\s*filename=\"(.*)\">(.*)</screenshot>");
		Matcher m = p.matcher(body);
		while (m.find()) {
			String filename = m.group(1);
			String url = m.group(2);
			File file = screenshotService.screenshot(url);
			result.put(filename, file);
		}
		return result;
	}
	
	private Map<String, File> getAttachments(String body) throws IOException, InterruptedException, URISyntaxException, MessagingException {
		Map<String, File> result = new HashMap<String, File>();
		
		Pattern p = Pattern.compile("<attachment\\s*filename=\"(.*)\">(.*)</attachment>");
		Matcher m = p.matcher(body);
		while (m.find()) {
			String filename = m.group(1);
			String url = m.group(2);
			File file = storageService.getFile(url);
			result.put(filename, file);
		}
		return result;
	}

}
