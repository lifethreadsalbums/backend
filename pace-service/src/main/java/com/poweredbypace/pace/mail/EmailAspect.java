package com.poweredbypace.pace.mail;

import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.poweredbypace.pace.domain.mail.EmailAccount;
import com.poweredbypace.pace.domain.mail.EmailTemplate;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.mail.EmailNotification.Recipient;
import com.poweredbypace.pace.repository.EmailTemplateRepository;
import com.poweredbypace.pace.repository.StoreRepository;
import com.poweredbypace.pace.util.SpringContextUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Aspect
public class EmailAspect {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private Configuration freemarkerConfig;
	
	@Autowired
	private EmailTemplateRepository emailTemplateRepo;
	
	@Autowired
	private StoreRepository storeRepository;
	
	@AfterReturning("@annotation(annotation)")
	public void processEmailNotifications(final JoinPoint pjp, EmailNotification annotation) throws Throwable 
	{
		try {
			processEmailNotification(pjp, annotation);
		} catch (Exception e) {
			log.error("", e);
		} 
	}
	
	@AfterReturning("@annotation(annotations)")
	public void processEmailNotifications(final JoinPoint pjp, EmailNotifications annotations) throws Throwable 
	{
		for(EmailNotification annotation:annotations.value())
		{
			try {
				processEmailNotification(pjp, annotation);
			} catch (Exception e) {
				log.error("", e);
			} 
		}
	}
	
	private void processEmailNotification(final JoinPoint pjp, EmailNotification annotation) throws 
		IOException, TemplateException, MessagingException, SecurityException, NoSuchMethodException, 
		IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
		Method method = methodSignature.getMethod();
		if (method.getDeclaringClass().isInterface()) {
	        method = pjp.getTarget().getClass().getDeclaredMethod(
	          		pjp.getSignature().getName(),
	                method.getParameterTypes());
	    }
		
		if (annotation!=null) {
			
			Map<String,Object> model = new HashMap<String, Object>();
			Annotation[][] parameterAnnotations = method.getParameterAnnotations();
			
			EmailNotificationRecipient recipientAnnotation = null;
			Object recipient = null;
			int i=0;
			for(Annotation[] annotations : parameterAnnotations) {
			  for(Annotation a : annotations)
			  {
				  if(a instanceof EmailNotificationParam) {
					  EmailNotificationParam param = (EmailNotificationParam) a;
					  if (param.wrapper().equals(EmailNotificationParam.class)) {
						  model.put(param.value(), pjp.getArgs()[i]);
					  } else {
						  //instantiate wrapper
						  model.put(param.value(),
								  SpringContextUtil.getApplicationContext().getBean(param.wrapper(), pjp.getArgs()[i]));
					  }
				  }
				  if (a instanceof EmailNotificationRecipient) {
					  recipientAnnotation = (EmailNotificationRecipient) a;
					  recipient = pjp.getArgs()[i];
				  }
			  }
			  i++;
			}
			
			Store store = null;
			Env env = SpringContextUtil.getEnv();
			if (env!=null) {
				store = env.getStore();
			} else {
				store = storeRepository.findByIsDefaultTrue();
			}
			
			model.put("store", store);
			
			EmailTemplate emailTemplate = emailTemplateRepo.findByName(annotation.template());
			EmailAccount emailAccount = emailTemplate.getEmailAccount();
			
			String email = FreeMarkerTemplateUtils.processTemplateIntoString(
					freemarkerConfig.getTemplate(annotation.template()), model);
			
			
			
			JavaMailSender mailSender = (JavaMailSender) SpringContextUtil.getApplicationContext()
					.getBean("JavaMailSender" + emailAccount.getSmtpServer().getId());
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
			
			if (annotation.to()==Recipient.LoggedUser) {
				Authentication auth = SecurityContextHolder.getContext().getAuthentication();
				User user = (User)auth.getPrincipal();
				message.setTo(user.getEmail());
			} else if (annotation.to()==Recipient.StoreOwner) {
				message.setTo(store.getOwnerEmail());
			} else if (annotation.to()==Recipient.SuperAdmin) {
				//TODO: implement it!
			} else if (annotation.to()==Recipient.MethodParam && recipient!=null && recipientAnnotation!=null) {
				if (recipient instanceof User) {
					message.setTo( ((User) recipient).getEmail() );
				} else {
					Method m = recipient.getClass().getMethod("get" + StringUtils.capitalize(recipientAnnotation.value()));
					String to = (String) m.invoke(recipient);
					message.setTo(to);
				}
			} else if (emailAccount.getTo()!=null) {
				message.setTo(emailAccount.getTo());
			}
			if (emailAccount.getCc()!=null)
				message.setCc(emailAccount.getCc());
			if (emailAccount.getBcc()!=null)
				message.setBcc(emailAccount.getBcc());
			message.setFrom(emailAccount.getFrom(), emailAccount.getFromName());
			message.setText(email, true);
			
			Template subjectTemplate = new Template("subject", 
					new StringReader(emailTemplate.getTranslatedSubject()),
					freemarkerConfig);
			String subject = FreeMarkerTemplateUtils.processTemplateIntoString(
					subjectTemplate, model);
			message.setSubject(subject);
			
			if (StringUtils.isBlank(email)) {
				log.info("Email body is blank, not sending it. Subject:'" + subject + "'");
				return;
			}
			
			mailSender.send(mimeMessage);
		}
	}
}
