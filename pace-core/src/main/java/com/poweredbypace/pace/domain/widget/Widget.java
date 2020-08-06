package com.poweredbypace.pace.domain.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.TResource;
import com.poweredbypace.pace.json.FlatPrototypeProductOptionSerializer;
import com.poweredbypace.pace.util.UrlUtil;

@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "type"
)  
@JsonSubTypes({  
    @Type(value = Widget.class, name = "Widget"),  
    @Type(value = TextAreaWidget.class, name = "TextAreaWidget"),
    @Type(value = ProductOptionWidget.class, name = "ProductOptionWidget"),
    @Type(value = BuildCoverViewWidget.class, name = "BuildCoverViewWidget"),
    @Type(value = BuildBoxViewWidget.class, name = "BuildBoxViewWidget"),
    @Type(value = BuildNumericOptionWidget.class, name = "BuildNumericOptionWidget"),
    @Type(value = BuildCustomDieWidget.class, name = "BuildCustomDieWidget"),
    @Type(value = BuildSectionWidget.class, name = "BuildSectionWidget"),
    @Type(value = BuildSlideshowViewWidget.class, name = "BuildSlideshowViewWidget"),
    @Type(value = BuildStampWidget.class, name = "BuildStampWidget"),
    @Type(value = BuildCameoWidget.class, name = "BuildCameoWidget"),
}) 
public class Widget extends BaseEntity {
	
	private static final long serialVersionUID = 3174333185615522166L;
		
	//Only a leaf keeps a reference to the actual product prototype option
	private PrototypeProductOption prototypeProductOption;
	
	private List<Widget> children = new ArrayList<Widget>();
	
	private Widget parent;
	
	private TResource label;
	
	private TResource prompt;
	
	private boolean isReprint;
	
	@JsonIgnore
	public boolean isReprint() {
		return isReprint;
	}

	public void setReprint(boolean isReprint) {
		this.isReprint = isReprint;
	}

	@JsonSerialize(using=FlatPrototypeProductOptionSerializer.class)
	public PrototypeProductOption getPrototypeProductOption() {
		return prototypeProductOption;
	}

	public void setPrototypeProductOption(
			PrototypeProductOption prototypeProductOption) {
		this.prototypeProductOption = prototypeProductOption;
	}
	
	@JsonIgnore
	public Widget getParent() {
		return parent;
	}

	public void setParent(Widget parent) {
		this.parent = parent;
	}

	public List<Widget> getChildren() {
		return children;
	}

	public void setChildren(List<Widget> children) {
		this.children = children;
	}

	@JsonIgnore
	public TResource getLabel() {
		return label;
	}

	public void setLabel(TResource label) {
		this.label = label;
	}
	
	@JsonIgnore
	public TResource getPrompt() {
		return prompt;
	}

	public void setPrompt(TResource prompt) {
		this.prompt = prompt;
	}

	@Transient
	public String getDisplayLabel() {
		return this.getLabel()!=null ? this.getLabel().getTranslatedValue() : null;
	}
	
	@Transient
	public String getDisplayPrompt() {
		return this.getPrompt()!=null ? this.getPrompt().getTranslatedValue() : null;
	}
	
	@Transient
	public Map<String, Object> getParams() {
		if (getPrototypeProductOption()!=null)
			return getPrototypeProductOption().getEffectiveParams();
		return null;
	}
	
	@Transient
	public String getUrl() {
		String label = getDisplayLabel();
		return label!=null ? UrlUtil.slug(label) : null;
	}

	public String render() {
		StringBuilder builder = new StringBuilder();
		for(Widget w:getChildren())
		{
			builder.append(w.render());
		}
		return builder.toString();
	}
	
}
