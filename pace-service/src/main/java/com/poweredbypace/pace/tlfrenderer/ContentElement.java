package com.poweredbypace.pace.tlfrenderer;

public class ContentElement {

	protected String text;
	protected ElementFormat elementFormat;
	protected TextBlock textBlock;
	protected GroupElement groupElement;
	
	public GroupElement getGroupElement() {
		return groupElement;
	}
	
	public void setGroupElement(GroupElement groupElement) {
		this.groupElement = groupElement;
	}
	
	public String getText() {
		return text;
	}
	
	public ElementFormat getElementFormat() {
		return elementFormat;
	}
	
	public TextBlock getTextBlock() {
		return textBlock;
	}
	
	public ContentElement()
	{
		
	}
	
	public ContentElement(ElementFormat elementFormat)
	{
		this.elementFormat = elementFormat;
	}
	
}
