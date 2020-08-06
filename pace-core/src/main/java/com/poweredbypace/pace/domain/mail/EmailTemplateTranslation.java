package com.poweredbypace.pace.domain.mail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.store.Store;

@Entity
@Table(name = "APP_EMAIL_TEMPLATE_TRANSLATION")
public class EmailTemplateTranslation extends BaseEntity {
	
	private static final long serialVersionUID = 7349266544499345712L;
	
	private String body;
	private String subject;
	private String language;
	private Store store;
	private EmailTemplate emailTemplate;
	
	
	@ManyToOne
	@JoinColumn(name = "EMAIL_TEMPLATE_ID")
	@JsonIgnore
	public EmailTemplate getEmailTemplate() {
		return emailTemplate;
	}

	public void setEmailTemplate(EmailTemplate emailTemplate) {
		this.emailTemplate = emailTemplate;
	}

	@Column(name = "BODY", columnDefinition="TEXT")
	public String getBody() {
		return body;
	}

	public void setBody(String value) {
		this.body = value;
	}

	@Column(name = "SUBJECTS", length=1000)
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	@Column(name = "LANG")
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

}
