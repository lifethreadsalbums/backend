package com.poweredbypace.pace.domain.layout;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("SpineTextElement")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class SpineTextElement extends TextElement {

	private static final long serialVersionUID = 4063027173093219136L;
	
}
