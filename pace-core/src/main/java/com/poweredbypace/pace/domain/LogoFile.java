package com.poweredbypace.pace.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("LOGO")
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogoFile extends ImageFile {

	private static final long serialVersionUID = 1L;
	
}
