package com.poweredbypace.pace.util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfSpotColor;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;

public class PdfUtils {
	protected static final PdfSpotColor kSeparationAll = new PdfSpotColor("All", new GrayColor(1.0f));

	public static void drawGrayColorBar(PdfContentByte cb, ColorSpace colorSpace) {
		
		boolean cmyk = ColorSpace.Cmyk==colorSpace;
		
		float [] tints = {1.0f, 0.9f, 0.8f, 0.7f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.1f, 0.0f};
		cb.saveState();
		if (cmyk)
			cb.setColorStroke(kSeparationAll, 0.65f);
		else
			cb.setColorStroke(new BaseColor(0.35f, 0.35f, 0.35f));
		
		cb.setLineWidth(1.0f);

		float x = 0.5f;
		for (int i=0; i<tints.length; ++i) {
			
			if (cmyk)
				cb.setColorFill(kSeparationAll, tints[i]);
			else {
				float tint = 1.0f - tints[i];
				cb.setColorFill(new BaseColor(tint, tint, tint));
			}
			cb.rectangle(x, 0.0f, 14.0f, 14.0f);
			cb.fill();
			cb.rectangle(x, -0.5f, 14.0f, 14.5f);
			cb.stroke();
			x += 14.0;
		}

		cb.restoreState();  
	}
	
	public static void drawColorBar(PdfContentByte cb, ColorSpace colorSpace) {
		
		boolean cmyk = ColorSpace.Cmyk==colorSpace;
		BaseColor [] rgbColors = {
				
				new BaseColor(1.0f, 0.94f, 0.0f),
				new BaseColor(0.9f, 0f, 0.49f),
				new BaseColor(0f, 0.64f, 0.89f),
				new BaseColor(0.15f, 0.05f, 0.47f),
				new BaseColor(0f, 0.6f, 0.23f),
				new BaseColor(0.94f, 0f, 0f),
				new BaseColor(0.07f, 0.07f, 0.05f),
				new BaseColor(1f, 0.97f, 0.62f),
				new BaseColor(0.98f, 0.61f, 0.76f),
				new BaseColor(0.46f, 0.82f, 0.95f),
				new BaseColor(0.64f, 0.64f, 0.64f)
		};
		
		BaseColor [] cmykColors = {
				new CMYKColor(0.0f, 0.0f, 1.0f, 0.0f),
				new CMYKColor(0.0f, 1.0f, 0.0f, 0.0f),
				new CMYKColor(1.0f, 0.0f, 0.0f, 0.0f),  
				new CMYKColor(1.0f, 1.0f, 0.0f, 0.0f),
				new CMYKColor(1.0f, 0.0f, 1.0f, 0.0f),
				new CMYKColor(0.0f, 1.0f, 1.0f, 0.0f),
				new CMYKColor(0.0f, 0.0f, 0.0f, 1.0f),
				new CMYKColor(0.0f, 0.0f, 0.5f, 0.0f),
				new CMYKColor(0.0f, 0.5f, 0.0f, 0.0f),
				new CMYKColor(0.5f, 0.0f, 0.0f, 0.0f),
				new CMYKColor(0.0f, 0.0f, 0.0f, 0.5f),
		};
		
		
		cb.saveState();
		
		if (cmyk)
			cb.setColorStroke(kSeparationAll, 0.65f);
		else
			cb.setColorStroke(new BaseColor(0.35f, 0.35f, 0.35f));
		cb.setLineWidth(1.0f);

		BaseColor[] colors = cmyk ? cmykColors : rgbColors;
		
		float x = 0.5f;
		for (int i=0; i<colors.length; ++i) {
			cb.setColorFill(colors[i]);
			cb.rectangle(x, 0.0f, 14.0f, 14.0f);
			cb.fill();
			cb.rectangle(x, -0.5f, 14.0f, 14.5f);
			cb.stroke();
			x += 14.0;
		}
		
		cb.restoreState();  
	}
	
	public static void drawTextJustified(PdfContentByte cb, float x, float y, float width, 
			String[] text, BaseFont font, float fontSize, ColorSpace colorSpace)
	{
		boolean cmyk = ColorSpace.Cmyk==colorSpace;
		cb.saveState();
		
		float textWidth = 0f;
		for(int i=0;i<text.length;i++)
		{
			textWidth += font.getWidthPoint(text[i], fontSize);
		}
		
		cb.setFontAndSize(font, fontSize);
		if (cmyk)
		{
			cb.setColorStroke(kSeparationAll, 1.0f); 
			cb.setColorFill(kSeparationAll, 1.0f);
		} else {
			cb.setColorStroke(BaseColor.BLACK); 
			cb.setColorFill(BaseColor.BLACK);
		}
		
		float s = (width - textWidth) / ((float)text.length-1);
		float xx = x;
		for(int i=0;i<text.length;i++)
		{
			cb.beginText();
			cb.moveText(xx, y);
			cb.showText(text[i]);
			cb.endText();
			xx += font.getWidthPoint(text[i], fontSize) + s;
		}
		cb.restoreState();
	}
}
