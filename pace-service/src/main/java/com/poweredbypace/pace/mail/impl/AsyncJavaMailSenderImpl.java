package com.poweredbypace.pace.mail.impl;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;

public class AsyncJavaMailSenderImpl extends JavaMailSenderImpl {

	@Override
	@Async
	public void send(SimpleMailMessage simpleMessage) throws MailException {
		super.send(simpleMessage);
	}

	@Override
	@Async
	public void send(MimeMessage mimeMessage) throws MailException {
		super.send(mimeMessage);
	}

	
	
}
