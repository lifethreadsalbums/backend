package com.poweredbypace.pace.tlfrenderer;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class TextChunk {
	
	private ElementFormat elementFormat;
	private String text;
	
	public ElementFormat getElementFormat() {
		return elementFormat;
	}
	
	public void setElementFormat(ElementFormat elementFormat) {
		this.elementFormat = elementFormat;
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}

	
	public TextChunk(String text, ElementFormat elementFormat) {
		this.elementFormat = elementFormat;
		this.text = text;
	}
	
	public float getTextWidth()
	{
		Font font = elementFormat.getFont();
		BaseFont bf = font.getBaseFont();
		
		float width = bf.getWidthPoint(text, elementFormat.getFontSize());
		if (text!=null && text.length()>1)
			width += elementFormat.getTrackingLeft() * (float)(text.length() - 1);
		return width;
	}
	
	public int getWordCount()
	{
		String[] words = text.split(" ");
		int count=0;
		for(String w:words)
		{
			if (w.length()>0)
				count++;
		}
		return count++;
	}

}
