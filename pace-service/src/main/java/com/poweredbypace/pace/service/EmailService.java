package com.poweredbypace.pace.service;

import java.io.File;
import java.util.Map;

import com.poweredbypace.pace.domain.mail.EmailTemplate;

public interface EmailService {

	void send(EmailTemplate template, Map<String,Object> model);
	void send(EmailTemplate template, Map<String,Object> model, Map<String, File> attachments);
	
	void send(String to, EmailTemplate template, Map<String,Object> model);
	void send(String to, EmailTemplate template, Map<String,Object> model, Map<String, File> attachments);
	
	EmailTemplate getEmailTemplate(String templateName);
	void send(String to, String emailTemplateName, Map<String,Object> model);
	
}
