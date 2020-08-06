package com.poweredbypace.pace.domain.mail;

import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.i18n.LocaleContextHolder;

import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.util.SpringContextUtil;


@Entity
@Table(name = "APP_EMAIL_TEMPLATE")
public class EmailTemplate extends BaseEntity {

	private static final long serialVersionUID = 4169844068231638563L;
	
	private String name;
	private Set<EmailTemplateTranslation> translations;
	private EmailAccount emailAccount;

	
	@Column(name = "NAME", unique=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "emailTemplate")
	public Set<EmailTemplateTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<EmailTemplateTranslation> translations) {
		this.translations = translations;
	}
	
	@ManyToOne
	@JoinColumn(name = "EMAIL_ACCOUNT_ID")
	public EmailAccount getEmailAccount() {
		return emailAccount;
	}

	public void setEmailAccount(EmailAccount emailAccount) {
		this.emailAccount = emailAccount;
	}
	
	@Transient
	public String getTranslatedBody() {
		return getTranslatedBody(LocaleContextHolder.getLocale());
	}
	
	@Transient
	public String getTranslatedBody(Locale locale) {
		EmailTemplateTranslation translation = getTranslation(locale);
		if (translation!=null)
			return translation.getBody();
		else {
			Store store = SpringContextUtil.getEnv().getStore();
			return "No translation for language \"" + locale.getLanguage() + "\" and store \"" + 
					store.getName() + "\" [" + this.getClass().getName() + "(id=" + getId() + "\")]";
		}
	}
	
	@Transient
	public String getTranslatedSubject() {
		return getTranslatedSubject(LocaleContextHolder.getLocale());
	}
	
	@Transient
	public String getTranslatedSubject(Locale locale) {
		EmailTemplateTranslation translation = getTranslation(locale);
		if (translation!=null)
			return translation.getSubject();
		else {
			Store store = SpringContextUtil.getEnv().getStore();
			return "No translation for language \"" + locale.getLanguage() + "\" and store \"" + 
					store.getName() + "\" [" + this.getClass().getName() + "(id=" + getId() + "\")]";
		}
	}
	
	@Transient
	public EmailTemplateTranslation getTranslation(Locale locale) {
		Store store = SpringContextUtil.getEnv().getStore();
		for(EmailTemplateTranslation translation : getTranslations()) {
			if (locale.getLanguage().equals(translation.getLanguage()) && 
				(translation.getStore()==null || store.getId().equals(translation.getStore().getId())) ) {
				return translation;
			}
		}
		return null;
	}
	

}
