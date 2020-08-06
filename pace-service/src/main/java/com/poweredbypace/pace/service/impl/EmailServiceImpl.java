package com.poweredbypace.pace.service.impl;

import java.io.File;
import java.io.StringReader;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.poweredbypace.pace.domain.mail.EmailAccount;
import com.poweredbypace.pace.domain.mail.EmailTemplate;
import com.poweredbypace.pace.repository.EmailTemplateRepository;
import com.poweredbypace.pace.service.EmailService;
import com.poweredbypace.pace.util.SpringContextUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service
public class EmailServiceImpl implements EmailService {
	
	private final Log log = LogFactory.getLog(getClass());
	
	
	@Autowired
	private Configuration freemarkerConfig;

	@Autowired
	private EmailTemplateRepository emailTemplateRepo;
	
	@Override
	public void send(EmailTemplate template, Map<String,Object> model) {
		EmailAccount emailAccount = template.getEmailAccount();
		send(emailAccount.getTo(), template, model);
	}
	
	@Override
	public void send(EmailTemplate template, Map<String,Object> model, Map<String, File> attachments) {
		EmailAccount emailAccount = template.getEmailAccount();
		send(emailAccount.getTo(), template, model, attachments);
	}

	@Override
	public void send(String to, EmailTemplate template, Map<String,Object> model)  {
		send(to, template, model, null);	
	}
	
	@Override
	public void send(String to, String emailTemplateName, Map<String,Object> model)  {
		EmailTemplate template = emailTemplateRepo.findByName(emailTemplateName);
		send(to, template, model, null);	
	}
	
	@Override
	public void send(String to, EmailTemplate template, Map<String,Object> model, 
			Map<String, File> attachments)  {
		try {
			EmailAccount emailAccount = template.getEmailAccount();
			
			String body = FreeMarkerTemplateUtils.processTemplateIntoString(
					freemarkerConfig.getTemplate(template.getName()), model);
			
			JavaMailSender mailSender = (JavaMailSender) SpringContextUtil.getApplicationContext()
					.getBean("JavaMailSender" + emailAccount.getSmtpServer().getId());
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, attachments!=null);
			
			message.setTo(to);
			message.setFrom(emailAccount.getFrom(), emailAccount.getFromName());
			if (emailAccount.getCc()!=null) {
				String[] cc = emailAccount.getCc().split(",");
				message.setCc(cc);
			}
			if (emailAccount.getBcc()!=null) {
				String[] bcc = emailAccount.getBcc().split(",");
				message.setBcc(bcc);
			}
			message.setText(body, true);
			
			Template subjectTemplate = new Template("subject", 
					new StringReader(template.getTranslatedSubject()),
					freemarkerConfig);
			String subject = FreeMarkerTemplateUtils.processTemplateIntoString(
					subjectTemplate, model);
			message.setSubject(subject);
			if (attachments!=null) {
				for(String key:attachments.keySet()) {
					message.addAttachment(key, attachments.get(key));
				}
			}
			
			if (StringUtils.isBlank(body)) {
				log.info("Email body is blank, not sending it. Subject:'" + subject + "'");
				return;
			}
			
			mailSender.send(mimeMessage);
			
			//delete attachments
			//TODO: delete attachments later
//			if (attachments!=null) {
//				for(File file:attachments.values()) {
//					file.delete();
//				}
//			}
			
		} catch (Exception e) {
			log.error("", e);
		}
	}
	
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public EmailTemplate getEmailTemplate(String templateName) {
		
		EmailTemplate template = emailTemplateRepo.findByName(templateName);
		template.getTranslatedBody();
		template.getTranslatedSubject();
		return template;
		
	}

}
