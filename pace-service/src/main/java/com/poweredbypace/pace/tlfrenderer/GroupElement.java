package com.poweredbypace.pace.tlfrenderer;

import java.util.List;

public class GroupElement extends ContentElement {

	protected List<ContentElement> elements;
	
	
	
	public List<ContentElement> getElements() {
		return elements;
	}

	public GroupElement(List<ContentElement> elements, ElementFormat elementFormat) {
		super(elementFormat);
		this.elements = elements;
		if (elements!=null)
		{
			for(ContentElement ce:elements)
				ce.setGroupElement(this);
		}
	}
	
	public GroupElement(List<ContentElement> elements) {
		this.elements = elements;
	}
	
	
}
