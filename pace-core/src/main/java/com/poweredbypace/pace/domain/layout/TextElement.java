package com.poweredbypace.pace.domain.layout;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.util.JsonUtil;


@Entity
@DiscriminatorValue("TextElement")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class TextElement extends Element {

	private static final long serialVersionUID = 718689138185131052L;
	
	private String htmlText;
	private String text;
	private String fill;
	private String fontFamily;
	private Float fontSize;
	private String fontStyle;
	private String fontWeight;
	private String textAlign;
	private String stylesAsString;
    private String placeholder;
	
	
	@Column(name="PLACEHOLDER") 
	public String getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	
	@Column(name="FILL")
	public String getFill() {
		return fill;
	}

	public void setFill(String color) {
		this.fill = color;
	}

	@Column(name="FONT_FAMILY")
	public String getFontFamily() {
		return fontFamily;
	}

	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}

	@Column(name="FONT_SIZE")
	public Float getFontSize() {
		return fontSize;
	}

	public void setFontSize(Float fontSize) {
		this.fontSize = fontSize;
	}

	@Column(name="FONT_STYLE")
	public String getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}

	@Column(name="FONT_WEIGHT")
	public String getFontWeight() {
		return fontWeight;
	}

	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}
	
	@Column(name="TEXT_ALIGN")
	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	@Column(name="TEXT", columnDefinition="TEXT")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name="STYLES", columnDefinition="TEXT")
	@JsonIgnore
	public String getStylesAsString() {
		return stylesAsString;
	}

	public void setStylesAsString(String styles) {
		this.stylesAsString = styles;
	}
	
	@SuppressWarnings("unchecked")
	@Transient
	public Map<String, Object> getStyles() {
		if(getStylesAsString() != null) {
			return (Map<String, Object>)JsonUtil.deserialize(getStylesAsString(), Map.class);
		} else {
			return new HashMap<String, Object>();
		}
	}
	
	public void setStyles(Map<String, Object> params) {
		setStylesAsString(JsonUtil.serialize(params));
	}

	@Column(name="HTML_TEXT", columnDefinition="TEXT")
	public String getHtmlText() {
		return htmlText;
	}

	public void setHtmlText(String htmlText) {
		this.htmlText = htmlText;
	}
	
	@Override
	public <E extends Element> void copy(E dst) {
		super.copy(dst);
		if (dst instanceof TextElement) {
			TextElement te = (TextElement)dst;
			te.setText(text);
			te.setFill(fill);
			te.setFontFamily(fontFamily);
			te.setFontSize(fontSize);
			te.setFontStyle(fontStyle);
			te.setFontWeight(fontWeight);
			te.setTextAlign(textAlign);
			te.setStylesAsString(stylesAsString);
		}
	}
	
}
