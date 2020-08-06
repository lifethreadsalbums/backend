package com.poweredbypace.pace.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("DIE")
@JsonIgnoreProperties(ignoreUnknown=true)
public class DieFile extends ImageFile {

	private static final long serialVersionUID = 1L;	
	
}
