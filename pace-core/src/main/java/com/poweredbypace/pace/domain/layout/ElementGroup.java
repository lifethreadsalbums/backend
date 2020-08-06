package com.poweredbypace.pace.domain.layout;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;


@Entity
@DiscriminatorValue("ElementGroup")
public class ElementGroup extends Element {

	
	private static final long serialVersionUID = -7263592456540305059L;
	private List<Element> elements;
	
	@JsonManagedReference("groupElements")
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	public List<Element> getElements() {
		return elements;
	}
	public void setElements(List<Element> elements) {
		this.elements = elements;
	}
}
