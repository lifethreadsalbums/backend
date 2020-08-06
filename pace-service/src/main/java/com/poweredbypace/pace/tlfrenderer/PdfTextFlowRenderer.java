package com.poweredbypace.pace.tlfrenderer;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.print.ColorConverter;

@Component
public class PdfTextFlowRenderer {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private ColorConverter colorConverter;
	
	@Autowired
	private FontRegistry fontRegistry;
	
	
	public static float getAscent(String fontFamily, float fontSize)
	{
		float ascent = 0.0f;
		
		if (fontFamily.startsWith("Bickham Script Pro"))
			ascent = fontSize * 0.68f;
		else
			ascent = fontSize * 0.75f;
		
		return ascent;
	}
	
	
	public void render(String textFlowXML, PdfContentByte canvas, PdfWriter writer, Rectangle bounds, 
			float pad, boolean convertRGBtoCMYK, float scale, boolean wrap)
	{
		try {

			TextFlowParser parser = new TextFlowParser(fontRegistry);
			TextFlow textFlow = parser.parse(textFlowXML);
			
			renderTextFlow(textFlow, canvas, writer, bounds, pad, convertRGBtoCMYK, scale, wrap);
			
		} catch(Exception pce) {
			logger.error("", pce);
		}
		
	}
	
	private void renderTextFlow(TextFlow textFlow, PdfContentByte canvas, PdfWriter writer, 
			Rectangle bounds, float pad, boolean convertRGBtoCMYK, float scale, boolean wrap) throws IOException, DocumentException
	{
		canvas.saveState();
		canvas.setTextRenderingMode(PdfContentByte.TEXT_RENDER_MODE_FILL);
		
		float textBoxWidth = bounds.getWidth() * (1f/scale);
		float textBoxHeight = bounds.getHeight() * (1f/scale);
		if (textFlow.getTextRotation().equals(TextFlow.ROTATE_90) || 
			textFlow.getTextRotation().equals(TextFlow.ROTATE_270))
		{
			textBoxWidth = bounds.getHeight() * (1f/scale);
			textBoxHeight = bounds.getWidth() * (1f/scale);
		}
		PdfTemplate textBox = canvas.createTemplate(textBoxWidth, textBoxHeight);
		
		float x = textFlow.getPaddingLeft();
		float y = textBoxHeight - textFlow.getPaddingTop();
		float w = textBoxWidth - (textFlow.getPaddingLeft() + textFlow.getPaddingRight());
		float bottom = textFlow.getPaddingBottom();
		
		float posy = y - pad;
		
		if (textFlow.getVerticalAlign()!=null && textFlow.getVerticalAlign().equals("middle"))
		{
			float textHeight = textFlow.calculateHeight(w);
			posy = textBoxHeight/2.0f + textHeight/2.0f;
		}
		
		int lineIndex=0;
		for(TextBlock textBlock:textFlow.getTextBlocks())
		{
			String textAlign = textBlock.getTextAlign();
			List<TextLine> textLines = textBlock.createTextLines( wrap ? w : Float.MAX_VALUE ); 
			
			int lineInBlockIndex = 0;
			for(TextLine tl: textLines)
			{
				float posx = x + pad;
				float lineWidth = tl.getTextWidth();
				Boolean lastLine = lineInBlockIndex==textLines.size()-1;
				
				float wordSpace = 0.0f;
				textBox.setWordSpacing(0.0f);
				
				if (textAlign!=null)
				{
					if (textAlign.equals(TextBlock.TEXT_ALIGN_CENTER))
						posx = x + (w/2.0f - lineWidth/2.0f);
					else if (textAlign.equals(TextBlock.TEXT_ALIGN_RIGHT))
						posx = x + (w - lineWidth);
					else if (textAlign.equals(TextBlock.TEXT_ALIGN_JUSTIFY) && !lastLine)
					{
						//do justification, calculate word spacing
						int wordCount = tl.getWordCount();
						wordSpace = (w - lineWidth) / (float)(wordCount-1);
						textBox.setWordSpacing(wordSpace);
					}
				}
				
				if (lineIndex==0)
					posy -= tl.getAscent();
				else
					posy -= tl.getLineHeight();
				
				if (posy<=bottom)
					break;
				
				int textChunkIndex = 0;
				for(TextChunk tc:tl.getTextChunks())
				{
					if (textChunkIndex>0 && wordSpace>0)
					{
						String text = tc.getText();
						if (text.length()>0 && text.charAt(0)!=' ')
							posx+= wordSpace;
					}
					
					ElementFormat format = tc.getElementFormat();
					if (textChunkIndex>0)
					{
						posx += format.getTrackingLeft();
					}
					
					BaseFont bf = format.getFont().getBaseFont();
					BaseColor color = new BaseColor(0xff << 24 | format.getColor());
					float fontSize = format.getFontSize();
					float baselineShift = format.getBaselineShift();
					float trackingLeft = format.getTrackingLeft();
					float trackingRight = format.getTrackingRight();
					
					if (convertRGBtoCMYK)
					{
						if (color.equals(BaseColor.BLACK))
							color = new CMYKColor(0f, 0f, 0f, 1f);
						else
							color = colorConverter.toCMYKColor(color);
					}
						
					textBox.beginText();
					textBox.setColorStroke(color);	
					textBox.setColorFill(color);
					textBox.setFontAndSize(bf, fontSize);	
					textBox.setCharacterSpacing(trackingLeft);
					textBox.moveText(posx, posy + baselineShift);
					textBox.showText(tc.getText());	
					textBox.endText();
					
					float textWidth = tc.getTextWidth();	
					textWidth += trackingRight;
					
					posx+=textWidth;
					
					if (wordSpace>0)
					{
						int wc = tc.getWordCount();
						posx += wordSpace * (float)(wc-1);
					}
					textChunkIndex++;
				}
				lineIndex++;
				lineInBlockIndex++;
			}
		}
		
		float boxX = bounds.getLeft();
		float boxY = bounds.getBottom();
		float angle = 0.0f;
		
		String textRotation = textFlow.getTextRotation();
		if (textRotation.equals(TextFlow.ROTATE_90))
		{
			angle = 270f;
			boxY = bounds.getBottom() + bounds.getHeight();
		}
		else if (textRotation.equals(TextFlow.ROTATE_180))
		{
			angle = 180f;
			boxX = bounds.getLeft() + bounds.getWidth();
			boxY = bounds.getBottom() + bounds.getHeight();
		}
		else if (textRotation.equals(TextFlow.ROTATE_270))
		{
			angle = 90f;
			boxX = bounds.getLeft() + bounds.getWidth();
		}
		
		AffineTransform trans = new AffineTransform();
		trans.translate(boxX, boxY);
		trans.rotate(angle*Math.PI / 180.0f);
		trans.scale(scale, scale);
		
		canvas.addTemplate(textBox, trans);
		canvas.restoreState();
		
		writer.releaseTemplate(textBox);
	}
	
	
}
