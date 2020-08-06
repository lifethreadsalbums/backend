package com.poweredbypace.pace.domain.layout;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;
import com.poweredbypace.pace.domain.TResource;

@Entity
@Table(name="APP_COVER_TYPE")
public class CoverType extends BaseEntity {

	private static final long serialVersionUID = 1863488415896519952L;
	
	private String code;
	private List<PrototypeProductOptionValue> prototypeProductOptionValues;
	
	@Column(name="CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	private TResource label;
	
	@JsonIgnore
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "LABEL_RESOURCE_ID")
	public TResource getLabel() {
		return label;
	}
	
	public void setLabel(TResource resource) {
		this.label = resource;
	}
	
	@JsonIgnore
	@OneToMany(mappedBy="coverType", fetch=FetchType.LAZY)
	public List<PrototypeProductOptionValue> getPrototypeProductOptionValues() {
		return prototypeProductOptionValues;
	}
	public void setPrototypeProductOptionValues(
			List<PrototypeProductOptionValue> prototypeProductOptionValues) {
		this.prototypeProductOptionValues = prototypeProductOptionValues;
	}

}
