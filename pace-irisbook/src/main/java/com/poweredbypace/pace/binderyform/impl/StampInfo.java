package com.poweredbypace.pace.binderyform.impl;

public class StampInfo {
	
	private String font;
	private String text;
	private String foil;
	private String fontName;
	private Boolean bookStamp;
	private Boolean boxStamp;
	private int fontSize;
	
	public String getFoil() {
		return foil;
	}
	public void setFoil(String foil) {
		this.foil = foil;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public String getFont() {
		return font;
	}
	public void setFont(String font) {
		this.font = font;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Boolean getBookStamp() {
		return bookStamp;
	}
	public void setBookStamp(Boolean bookStamp) {
		this.bookStamp = bookStamp;
	}
	public Boolean getBoxStamp() {
		return boxStamp;
	}
	public void setBoxStamp(Boolean boxStamp) {
		this.boxStamp = boxStamp;
	}
	
	public int getFontSize() {
		return fontSize;
	}
	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public String getType() {
		if (getBookStamp() && getBoxStamp())
			return "Book & Box";
		else if (getBookStamp())
			return "Book ONLY";
		else if (getBoxStamp())
			return "Box ONLY";
		else
			return "";
	}
	
	public StampInfo() { }
}