package com.poweredbypace.pace.tlfrenderer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class TextBlock {

	public static String TEXT_ALIGN_CENTER = "center";
	public static String TEXT_ALIGN_LEFT = "left";
	public static String TEXT_ALIGN_RIGHT = "right";
	public static String TEXT_ALIGN_JUSTIFY = "justify";
	
	private ContentElement content;
	private String textAlign;

	
	public String getTextAlign() {
		return textAlign;
	}

	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}

	public ContentElement getContent() {
		return content;
	}

	public void setContent(ContentElement content) {
		this.content = content;
	}
	
	public TextBlock(ContentElement content) 
	{
		this.content = content;
	}
	
	public List<TextLine> createTextLines(float lineLength)
	{
		List<TextElement> textElements = new ArrayList<TextElement>();
		
		extractTextElements(content, textElements);
		
		float pos = 0;
		
		List<TextLine> textLines = new ArrayList<TextLine>();
		TextLine textLine = new TextLine();
		textLines.add(textLine);
		int elementIndex = 0;
		for(TextElement te:textElements)
		{
			Font font = te.getElementFormat().getFont();
			BaseFont bf = font.getBaseFont();
			ElementFormat format = te.getElementFormat();
			
			if (elementIndex>0)
				pos += format.getTrackingLeft();
			
			String textToRender = te.getText();
			String text = "";
			int len = textToRender.length();
			String lastTextChunk = "";
			int numSpaces = 0;
			for (int i=0;i<len;i++)
			{
				char c = textToRender.charAt(i);
				if (c==' ' || i==len-1)
				{
					if (c==' ')
						numSpaces++;
					String textToMeasure = text + c;
					float textWidth = bf.getWidthPoint(textToMeasure, te.getElementFormat().getFontSize());
					
					if (textToMeasure.length()>1)
						textWidth += format.getTrackingLeft() * (float)(textToMeasure.length()-1);
					
					float sepWidth = c==' ' ? bf.getWidthPoint(c, te.getElementFormat().getFontSize()) : 0;
					if (textAlign!=null && textAlign.equals(TEXT_ALIGN_JUSTIFY))
						textWidth -= (float)numSpaces * sepWidth * 0.5f;
					
					if ( (pos+textWidth) - lineLength > sepWidth)
					{
						//new line
						if (lastTextChunk.length()>0)
						{
							TextChunk textChunk = new TextChunk(lastTextChunk, te.getElementFormat());
							textLine.getTextChunks().add(textChunk);
						}
						textLine = new TextLine();
						textLines.add(textLine);
						pos = 0;
						text = text.substring(lastTextChunk.length()) + c;
						numSpaces = 0;
					} else {
						text = new String(textToMeasure);
					}
					lastTextChunk = new String(text);
				} else 
					text += c;
			}
			
			if (text.length()>0 || textToRender.equals(text))
			{
				float textWidth = bf.getWidthPoint(text, te.getElementFormat().getFontSize());
				pos += textWidth;
				pos += format.getTrackingRight();
				
				TextChunk textChunk = new TextChunk(text, te.getElementFormat());
				
				textLine.getTextChunks().add(textChunk);
			}
			elementIndex++;
		}
		
		//we need to rtrim last text chunk of each line 
		for(TextLine tl:textLines)
		{
			if (tl.getTextChunks().size()>0)
			{
				TextChunk tc = tl.getTextChunks().get(tl.getTextChunks().size()-1);
				tc.setText( StringUtils.trimTrailingWhitespace(tc.getText()) );
			}
		}
		
		return textLines;
	}
	
	private void extractTextElements(ContentElement element, List<TextElement> textElements)
	{
		if (element.getClass().equals(TextElement.class))
			textElements.add((TextElement)element);
		else if (element.getClass().equals(GroupElement.class))
		{
			for(ContentElement ce:((GroupElement)element).getElements())
			{
				extractTextElements(ce, textElements);
			}
		}
	}
	
}
