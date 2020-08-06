package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.validator.ProductOptionValidator;

@Entity
@Table(name = "T_VALIDATOR")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class TValidator extends BaseTerm {

	private static final long serialVersionUID = 7050049506956907449L;
	
	private Class<? extends ProductOptionValidator> concreteValidatorClass;

	@Column(name = "CONCRETE_CLASS")
	public Class<? extends ProductOptionValidator> getConcreteValidatorClass() {
		return concreteValidatorClass;
	}

	public void setConcreteValidatorClass(Class<? extends ProductOptionValidator> concreteValidatorClass) {
		this.concreteValidatorClass = concreteValidatorClass;
	}
}
