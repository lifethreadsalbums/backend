package com.poweredbypace.pace.tlfrenderer;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TextLine {

	private List<TextChunk> textChunks;

	public List<TextChunk> getTextChunks() {
		return textChunks;
	}

	public void setTextChunks(List<TextChunk> textChunks) {
		this.textChunks = textChunks;
	}
	
	public TextLine()
	{
		this.textChunks = new ArrayList<TextChunk>();
	}
	
	public float getLineHeight()
	{
		float maxLineHeight = 0;
		for(TextChunk tc:textChunks)
		{
			if (tc.getElementFormat().getLineHeight()>maxLineHeight)
				maxLineHeight = tc.getElementFormat().getLineHeight();
		}
		return maxLineHeight;
	}
	
	public float getTextWidth()
	{
		float width = 0f;
		int n = textChunks.size();
		for(int i=0;i<n;i++)
		{
			TextChunk tc = textChunks.get(i);
			ElementFormat format = tc.getElementFormat();
			width += tc.getTextWidth();
			if (i>0)
				width += format.getTrackingLeft();
			if (i<n-1)
				width += format.getTrackingRight();
		}
		return width;
	}
	
	public float getAscent()
	{
		float maxAscent = 0;
		for(TextChunk tc:textChunks)
		{
			//BaseFont bf = tc.getElementFormat().getFont().getBaseFont();
			
			//float ascent = bf.getAscentPoint(tc.getText(), tc.getElementFormat().getFontSize());
			float ascent = 0f;
			
			//TODO:font caching
			
			try {
				
				BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = bi.createGraphics();
				InputStream is = new FileInputStream(tc.getElementFormat().getFontPath()); 
			    Font font = null;
				font = Font.createFont(Font.TRUETYPE_FONT, is);
				Font font2 = font.deriveFont(tc.getElementFormat().getFontSize());
				
				LineMetrics lm = font2.getLineMetrics(tc.getText(), g2d.getFontRenderContext());
				ascent = lm.getAscent();
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (ascent==0.0f)
			{
				ascent = PdfTextFlowRenderer.getAscent(tc.getElementFormat().getFont().getFamilyname(), tc.getElementFormat().getFontSize());
			}
			
			if (ascent>maxAscent)
				maxAscent = ascent;
		}
		if (maxAscent==0.0f)
		{
			maxAscent = getMaxFontSize() * 0.75f;
		}
		return maxAscent;
	}
	
	public float getDescent()
	{
		float maxDescent = 0;
		for(TextChunk tc:textChunks)
		{
			//BaseFont bf = tc.getElementFormat().getFont().getBaseFont();
			
			//float descent = -bf.getDescentPoint(tc.getText()+"j", tc.getElementFormat().getFontSize());
			float descent = 0;
			
			//TODO:font caching
			try {
				
				BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2d = bi.createGraphics();
				InputStream is = new FileInputStream(tc.getElementFormat().getFontPath()); 
			    Font font = null;
				font = Font.createFont(Font.TRUETYPE_FONT, is);
				Font font2 = font.deriveFont(tc.getElementFormat().getFontSize());
				
				LineMetrics lm = font2.getLineMetrics(tc.getText()+"j", g2d.getFontRenderContext());
				descent = lm.getDescent();
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if (descent>maxDescent)
				maxDescent = descent;
		}
		if (maxDescent==0.0f)
		{
			maxDescent = getMaxFontSize() * 0.25f;
		}
		return maxDescent;
	}
	
	
	
	public float getMaxFontSize()
	{
		float result = 0;
		for(TextChunk tc:textChunks)
		{
			float fontSize = tc.getElementFormat().getFontSize();
			if (fontSize>result)
				result = fontSize;
		}
		return result;
	}
	
	public int getWordCount()
	{
		String text="";
		for(TextChunk tc:textChunks)
		{
			text += tc.getText() + " ";
		}
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
