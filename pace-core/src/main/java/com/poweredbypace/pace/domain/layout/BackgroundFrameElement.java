package com.poweredbypace.pace.domain.layout;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("BackgroundFrameElement")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class BackgroundFrameElement extends Element {

	private static final long serialVersionUID = -2202605471130977010L;

	private String target;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	
}
