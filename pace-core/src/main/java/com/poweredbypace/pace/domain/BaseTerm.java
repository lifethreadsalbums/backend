package com.poweredbypace.pace.domain;

import java.io.Serializable;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.util.SpringContextUtil;

@MappedSuperclass
public class BaseTerm extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5651650598876976297L;

	private String name;
	private String description;
	
	private TResource resource;
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "DESCRIPTION")
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	@Transient
	public String getDisplayName() {
		return getDisplayName(LocaleContextHolder.getLocale());
	}
	
	@Transient
	public String getDisplayName(Locale locale) {
//		if(resource != null) {
//			return resource.getTranslatedValue(locale);
//		}
		if (resource!=null) {
			TResource r = SpringContextUtil.getResourceManager().getResource(resource.getId());
			return r.getTranslatedValue(locale);
		}
		return getName();
		//return "No resource for language \"" + locale.getLanguage() + "\" [" + this.getClass().getName() + "(id=" + getId() + ", name=\"" + getName() + "\")]";
	}

	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "T_RESOURCE_ID")
	public TResource getResource() {
		return resource;
	}

	public void setResource(TResource resource) {
		this.resource = resource;
	}

}