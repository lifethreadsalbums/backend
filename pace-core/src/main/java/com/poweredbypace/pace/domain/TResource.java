package com.poweredbypace.pace.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.util.SpringContextUtil;

@Entity
@Table(name = "T_RESOURCE")
public class TResource extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5550768544099694977L;

	private String value;
	private String description;
	private Set<TResourceTranslation> translations = new HashSet<TResourceTranslation>();

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "resource")
	@JsonIgnore
	public Set<TResourceTranslation> getTranslations() {
		return translations;
	}

	public void setTranslations(Set<TResourceTranslation> translations) {
		this.translations = translations;
	}

	@Column(name = "VALUE")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public String getTranslatedValue() {
		return getTranslatedValue(LocaleContextHolder.getLocale());
	}
	
	@Transient
	public String getTranslatedValue(Locale locale) {
		return getTranslatedValue(locale, false);
	}
	
	@Transient
	public String getTranslatedValue(Locale locale, boolean appendInfo) {
		Env env = SpringContextUtil.getEnv();
		
		if (locale!=null && env!=null) {
			Store store = env.getStore();
			View view = env.getView();
			for(TResourceTranslation translation : getTranslations()) {
				if (locale.getLanguage().equals(translation.getLanguage()) && 
					(translation.getStore()==null || store.equals(translation.getStore())) &&
					(translation.getView()==null || view.equals(translation.getView())) ) 
					return translation.getValue();
			}
		}
		
//		if(appendInfo == true) {
//			return "No translation for language \"" + locale.getLanguage() + "\" and store \"" + store.getName() + "\" [" + this.getClass().getName() + "(id=" + getId() + ", value=\"" + getValue() + "\")]";
//		}
		return getValue();
	}
}