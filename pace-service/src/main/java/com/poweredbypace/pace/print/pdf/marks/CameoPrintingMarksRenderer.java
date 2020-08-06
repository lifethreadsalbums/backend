package com.poweredbypace.pace.print.pdf.marks;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfSpotColor;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;
import com.poweredbypace.pace.print.pdf.PrintingMarksRenderer;
import com.poweredbypace.pace.print.pdf.SpreadInfo;
import com.poweredbypace.pace.util.PdfUtils;

public class CameoPrintingMarksRenderer implements PrintingMarksRenderer {
	
	protected static final PdfSpotColor REGISTRATION_COLOR = new PdfSpotColor("All", new GrayColor(1.0f));
	protected static final float PRINTER_MARK_LINE_WIDTH = 0.25f;
	protected static final BaseColor CMYK_RED = new CMYKColor(0.15f, 1f, 1f, 0f);
	
	@Override
	public void draw(PdfContentByte cb, PdfPageInfo pageInfo,
			Product product, IccProfile profile, int pageIndex, int numPages)
			throws DocumentException, IOException {
		
		SpreadInfo spreadInfo = (SpreadInfo) pageInfo;
		//draw trim marks
		drawTrimMarks(cb, spreadInfo, pageIndex, profile);
		drawJobInfo(cb, spreadInfo, product, pageIndex, numPages, profile);
	}
	
	protected void drawText(PdfContentByte cb, String text, float x, float y, BaseFont font, float fontSize, BaseColor color) {
		cb.beginText();
		cb.moveText(x, y);
		cb.setFontAndSize(font, fontSize);
		cb.setColorStroke(color); 
		cb.setColorFill(color);
		cb.showText(text);
		cb.endText();
	}
	
	protected void drawText(PdfContentByte cb, String text, float x, float y, BaseFont font, float fontSize, IccProfile profile) {
		cb.beginText();
		cb.moveText(x, y);
		cb.setFontAndSize(font, fontSize);
		
		if (profile.getColorSpace()==ColorSpace.Cmyk)
		{
			cb.setColorStroke(REGISTRATION_COLOR, 1.0f); 
			cb.setColorFill(REGISTRATION_COLOR, 1.0f);
		} else {
			cb.setColorStroke(BaseColor.BLACK); 
			cb.setColorFill(BaseColor.BLACK);
		}
		cb.showText(text);
		cb.endText();
	}
	
	protected void drawJobInfo(PdfContentByte cb, SpreadInfo pageInfo, 
			Product product, int pageIndex, int numPages, IccProfile profile) {
		float fontSize = 12;
		Font helvetica = new Font(FontFamily.HELVETICA, fontSize);
		
		BaseFont bfHelv = helvetica.getCalculatedBaseFont(false);
				
		String[] text = new String[] { 
			product.getProductNumber(),
			numPages > 1 ? "Position: " + (pageIndex + 1) : ""
		};
		
		float fs = 8f;
		float pad = 20f;
		
		Rectangle slugRect = pageInfo.getSlugRect();
		Rectangle trimRect = pageInfo.getTrimRect();
		cb.saveState();
		
		float left = trimRect.getLeft() + pad;
		float right = trimRect.getRight() - pad;
		PdfUtils.drawTextJustified(cb, left, 
				slugRect.getHeight() - 6.0f - fs/2f, 
				right - left,
				text, bfHelv, fs, profile.getColorSpace());
		
		cb.restoreState();
	}
	
	protected float[] getHorizontalMarks(SpreadInfo pageInfo, int pageIndex) {
		Rectangle trimRect = pageInfo.getTrimRect();
		
		float marks[] = {
			trimRect.getLeft(),
			trimRect.getRight()
		};
		
		return marks;
	}
	
	protected Object[] getHorizontalMarksColors(SpreadInfo pageInfo, int pageIndex, IccProfile profile) {
		Object black = REGISTRATION_COLOR;
		if (profile.getColorSpace()==ColorSpace.Rgb)
			black = BaseColor.BLACK;
		
		Object[] colors =  { black, black};
		return colors;
	}
	
	protected float[] getVerticalMarks(SpreadInfo pageInfo, int pageIndex) {
		Rectangle trimRect = pageInfo.getTrimRect();
		float marks[] = { trimRect.getTop(), trimRect.getBottom(), };
		return marks;
	}
	
	protected Object[] getVerticalMarksColors(SpreadInfo pageInfo, int pageIndex, IccProfile profile) {
		Object black = REGISTRATION_COLOR;
		if (profile.getColorSpace()==ColorSpace.Rgb)
			black = BaseColor.BLACK;
		
		Object[] colors = { black, black };
		return colors;
	}
	
	protected void drawHorizontalTrimMark(PdfContentByte cb, float x, float y, float h) {
		cb.moveTo(x,y);
		cb.lineTo(x, y+h);
		cb.stroke();
	}
	
	protected void drawVerticalTrimMark(PdfContentByte cb, float x, float y, float w) {
		cb.moveTo(x,y);
		cb.lineTo(x+w, y);
		cb.stroke();
	}
	
	protected void drawTrimMarks(PdfContentByte cb, SpreadInfo pageInfo, int pageIndex, IccProfile profile) {
		
		Rectangle slugRect = pageInfo.getSlugRect();
		
		float pw = slugRect.getWidth();
		float ph = slugRect.getHeight();
		
		LayoutSize template = pageInfo.getLayoutSize();
		
		float si = template.getSlugInside().floatValue();
		float so = template.getSlugOutside().floatValue();
		float st = template.getSlugTop().floatValue();
		float sb = template.getSlugBottom().floatValue();
		
		cb.saveState();
		cb.setColorStroke(REGISTRATION_COLOR, 1.0f); 
		cb.setColorFill(REGISTRATION_COLOR, 0.5f);
		cb.setLineWidth(PRINTER_MARK_LINE_WIDTH);
		
		float xmarks[] = getHorizontalMarks(pageInfo, pageIndex);
		Object xcolors[] = getHorizontalMarksColors(pageInfo, pageIndex, profile);
		int i = 0;
		for(float x:xmarks) {
			cb.setLineWidth(PRINTER_MARK_LINE_WIDTH);
			if (xcolors[i] instanceof PdfSpotColor)
				cb.setColorStroke( ((PdfSpotColor)xcolors[i]), 1.0f );
			else
				cb.setColorStroke((BaseColor)xcolors[i]);
			drawHorizontalTrimMark(cb, x, 0, sb);
			drawHorizontalTrimMark(cb, x, ph-st, st);
			i++;
		}
		float ymarks[] = getVerticalMarks(pageInfo, pageIndex);
		Object ycolors[] = getVerticalMarksColors(pageInfo, pageIndex, profile);
		i = 0;
		for(float y:ymarks) {
			if (ycolors[i] instanceof PdfSpotColor)
				cb.setColorStroke( ((PdfSpotColor)ycolors[i]), 1.0f );
			else
				cb.setColorStroke((BaseColor)ycolors[i]);
			drawVerticalTrimMark(cb, 0, y, si);
			drawVerticalTrimMark(cb, pw - so, y, si);
			i++;
		}
		
		cb.restoreState();
	}
	
}
