package com.poweredbypace.pace.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("TEXTURE")
@JsonIgnoreProperties(ignoreUnknown=true)
public class TextureFile extends ImageFile {

	private static final long serialVersionUID = 3784369098593728591L;	
	
}
