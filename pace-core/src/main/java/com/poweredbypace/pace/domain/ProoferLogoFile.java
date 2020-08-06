package com.poweredbypace.pace.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("PROOFER_LOGO")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProoferLogoFile extends ImageFile {

	private static final long serialVersionUID = 1L;	
	
}
