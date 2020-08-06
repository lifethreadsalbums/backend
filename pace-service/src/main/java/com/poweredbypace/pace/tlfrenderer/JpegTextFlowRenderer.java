package com.poweredbypace.pace.tlfrenderer;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.poweredbypace.pace.ApplicationConstants;

public class JpegTextFlowRenderer {
	
	private final Log logger = LogFactory.getLog(getClass());
	private final static double RESOLUTION = 300f / ApplicationConstants.PPI; //300dpi
	
	@Autowired
	private FontRegistry fontRegistry;
	
	public Rectangle2D.Float measureTextHeight(String textFlowXML, Rectangle2D.Float bounds, boolean wrap) {
		try {
			double res = RESOLUTION;
			int w = (int) (bounds.width * res);
			int h = (int) (bounds.height * res);
			BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			
			TextFlowParser parser = new TextFlowParser(fontRegistry);
			TextFlow tf = parser.parse(textFlowXML);
			renderTextFlow(tf, bi, bounds, 0f, 1.0f, wrap, res);
			
			int minY = Integer.MAX_VALUE;
			int maxY = 0;
			for(int y=0;y<h;y++) {
				for(int x=0;x<w;x++)
				{
					int color = bi.getRGB(x, y);
					if (color!=0) {
						minY = Math.min(y, minY);
						maxY = Math.max(y, maxY);
					}
				}
			}
			return new Rectangle2D.Float(0, (float) (minY / res), 0, (float) ((maxY - minY) / res));
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	public void render(String textFlowXML, float pageWidth, float pageHeight, Rectangle2D.Float bounds, 
			float rotation, float scale, boolean wrap, File output) throws TextFlowException, IOException
	{
		int w = (int) (pageWidth * RESOLUTION);
		int h = (int) (pageHeight * RESOLUTION);
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		TextFlowParser parser = new TextFlowParser(fontRegistry);
		TextFlow tf = parser.parse(textFlowXML);
		renderTextFlow(tf, bi, bounds, rotation, scale, wrap, RESOLUTION);
		ImageIO.write(bi, "png", output);
	}
	
	private void renderTextFlow(TextFlow textFlow, BufferedImage canvas, 
			Rectangle2D.Float bounds, float rotation, float scale, boolean wrap, double res) throws IOException
	{
		Graphics2D g2d = canvas.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
				RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		
		
		float boxX = bounds.x;
		float boxY = bounds.y;
		float angle = 0.0f;
		
		//TODO:scale parameter doesn't take effect - not implemented yet, we won't probably need it 
		
		String textRotation = textFlow.getTextRotation();
		if (textRotation.equals(TextFlow.ROTATE_90))
		{
			angle = 90f;
			boxX = bounds.x + bounds.width;
			
		}
		else if (textRotation.equals(TextFlow.ROTATE_180))
		{
			angle = 180f;
			boxX = bounds.x + bounds.width;
			boxY = bounds.y + bounds.height;
		}
		else if (textRotation.equals(TextFlow.ROTATE_270))
		{
			angle = 270f;
			boxY = bounds.y + bounds.height;
		}
		
		
		AffineTransform trans = new AffineTransform();
		trans.scale(res, res);
		trans.translate(boxX, boxY);
		trans.rotate( (angle + rotation) * Math.PI / 180.0f);
		
		g2d.setTransform(trans);
		
		float textBoxWidth = bounds.width * (1f/scale);
		float textBoxHeight = bounds.height * (1f/scale);
		if (textFlow.getTextRotation().equals(TextFlow.ROTATE_90) || 
			textFlow.getTextRotation().equals(TextFlow.ROTATE_270))
		{
			textBoxWidth = bounds.height * (1f/scale);
			textBoxHeight = bounds.width * (1f/scale);
		}
		
		float x = textFlow.getPaddingLeft();
		float y = textFlow.getPaddingTop();
		float w = textBoxWidth - (textFlow.getPaddingLeft() + textFlow.getPaddingRight());
		float bottom = textBoxHeight - textFlow.getPaddingBottom();
		float right = textBoxWidth - textFlow.getPaddingRight();
		
		float posy = y;
		
		if (textFlow.getVerticalAlign()!=null && textFlow.getVerticalAlign().equals("middle"))
		{
			float textHeight = textFlow.calculateHeight(w);
			posy = textBoxHeight/2.0f + textHeight/2.0f;
		}
		
		int lineIndex=0;
		for(TextBlock textBlock:textFlow.getTextBlocks())
		{
			String textAlign = textBlock.getTextAlign();
			List<TextLine> textLines = textBlock.createTextLines(wrap ? w : Float.MAX_VALUE); 
			
			int lineInBlockIndex = 0;
			for(TextLine tl: textLines)
			{
				float posx = x;
				float lineWidth = tl.getTextWidth();
				Boolean lastLine = lineInBlockIndex==textLines.size()-1;
				
				float wordSpace = 0.0f;
				
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
					}
				}
				
				if (lineIndex==0)
					posy += tl.getAscent();
				else
					posy += tl.getLineHeight();
				
				if (posy>=bottom)
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
					
					float fontSize = format.getFontSize();
					float baselineShift = format.getBaselineShift();
					float trackingLeft = format.getTrackingLeft();
					
					//TODO:font caching
					InputStream is = new FileInputStream(format.getFontPath()); 
				    Font font = null;
					try {
						font = Font.createFont(Font.TRUETYPE_FONT, is);
					} catch (FontFormatException e) {
						logger.error("", e);
					}
					
					g2d.setFont( font.deriveFont(fontSize) );
					g2d.setColor( new Color(format.getColor()) );
					
					String text = tc.getText(); 
					
					float charSpacing = trackingLeft;
					for (int c1=0 ; c1 < text.length() ; c1++) {

						float ty = posy - baselineShift;
						
				        char ch = text.charAt(c1); 
				        g2d.drawString(ch+"", posx, ty) ;
				        posx+= g2d.getFontMetrics().charWidth(ch) + charSpacing ;
				        if (posx>=right)
				        {
				        	posy += tl.getLineHeight();
				        	posx = x;
				        }
				    }
					
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
		
		g2d.dispose();
	}


}
