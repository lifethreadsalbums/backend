package com.poweredbypace.pace.tlfrenderer;

import com.itextpdf.text.Font;

public class ElementFormat {

	private Font font;
	private float fontSize = 14f;
	private int color;
	private float lineHeight = 0f;
	private float baselineShift = 0f;
	private float trackingLeft = 0f;
	private float trackingRight = 0f;
	private String fontPath;
	
	public String getFontPath() {
		return fontPath;
	}
	public void setFontPath(String fontFilename) {
		this.fontPath = fontFilename;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public Font getFont() {
		return font;
	}
	public void setFont(Font font) {
		this.font = font;
	}
	public float getFontSize() {
		return fontSize;
	}
	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
	
	public float getLineHeight() {
		return lineHeight;
	}
	public void setLineHeight(float lineHeight) {
		this.lineHeight = lineHeight;
	}
	public float getBaselineShift() {
		return baselineShift;
	}
	public void setBaselineShift(float baselineShift) {
		this.baselineShift = baselineShift;
	}
	
	public float getTrackingLeft() {
		return trackingLeft;
	}
	public void setTrackingLeft(float trackingLeft) {
		this.trackingLeft = trackingLeft;
	}
	public float getTrackingRight() {
		return trackingRight;
	}
	public void setTrackingRight(float trackingRight) {
		this.trackingRight = trackingRight;
	}
	public ElementFormat() {
		super();
		color = 0x000000;
	}
	
	public ElementFormat(float fontSize) {
		super();
		this.fontSize = fontSize;
	}
	
	public ElementFormat(Font font, float fontSize, int color) {
		super();
		this.color = color;
		this.font = font;
		this.fontSize = fontSize;
	}
	
}
