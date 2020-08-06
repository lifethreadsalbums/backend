package com.poweredbypace.pace.print.pdf.marks;

import java.awt.geom.AffineTransform;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfSpotColor;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;
import com.poweredbypace.pace.print.pdf.PrintingMarksRenderer;

public class CoverPrintingMarksRenderer implements PrintingMarksRenderer {
	
	protected static final PdfSpotColor kSeparationAll = new PdfSpotColor("All", new GrayColor(1.0f));
	protected static final float kPrinterMarkLineWidth = 0.25f;
	
	@Override
	public void draw(PdfContentByte cb, PdfPageInfo book, Product product, IccProfile iccProfile, int pageIndex, int numPages) 
		throws DocumentException, IOException {
		
		if (book.isVertical())
		{
			cb.saveState();
			AffineTransform af = new AffineTransform();
			af.translate(0, book.getPageHeight());
			af.rotate(Math.toRadians(-90f));
			
			cb.concatCTM(af);
		}
		drawTrimMarks(cb, book, pageIndex);
		drawLabels(cb, book, pageIndex, product);
		if (book.isVertical())
			cb.restoreState();
	}
	
	protected float[] getHorizontalMarks(PdfPageInfo book, int pageIndex)
	{
		LayoutSize template = book.getLayoutSize();
		
		float pw = book.getPageWidth();
		float bi = template.getBleedInside().floatValue();
		float bo = template.getBleedOutside().floatValue();
		float si = template.getSlugInside().floatValue();
		float so = template.getSlugOutside().floatValue();
		
		float marks[] = {si, bi+si, pw - bo - so, pw - so};
		
		return marks;
	}
	
	protected BaseColor[] getHorizontalMarksColors(PdfPageInfo book, int pageIndex)
	{
		BaseColor[] colors = {BaseColor.BLACK, BaseColor.BLACK, BaseColor.BLACK, BaseColor.BLACK};
		return colors;
	}
	
	protected float[] getVerticalMarks(PdfPageInfo book, int pageIndex)
	{
		LayoutSize template = book.getLayoutSize();
		
		float ph = book.getPageHeight();
		float bt = template.getBleedTop().floatValue();
		float bb = template.getBleedBottom().floatValue();
		float st = template.getSlugTop().floatValue();
		float sb = template.getSlugBottom().floatValue();
		
		float marks[] = {sb, bb+sb, ph - bt - st, ph - st};
		
		return marks;
	}
	
	protected float[] getHorizontalMarkLengths(PdfPageInfo book, int pageIndex)
	{
		return null;
	}
	
	protected float[] getVerticalMarkLengths(PdfPageInfo book, int pageIndex)
	{
		return null;
	}
	
	
	
	protected BaseColor[] getVerticalMarksColors(PdfPageInfo book, int pageIndex)
	{
		BaseColor[] colors = {BaseColor.BLACK, BaseColor.BLACK, BaseColor.BLACK, BaseColor.BLACK};
		return colors;
	}
	
	
	protected void drawHorizontalTrimMark(PdfContentByte cb, float x, float y, float h)
	{
		cb.moveTo(x,y);
		cb.lineTo(x, y+h);
		cb.stroke();
	}
	
	protected void drawVerticalTrimMark(PdfContentByte cb, float x, float y, float w)
	{
		cb.moveTo(x,y);
		cb.lineTo(x+w, y);
		cb.stroke();
	}
	
	protected void drawTrimMarks(PdfContentByte cb, PdfPageInfo book, int pageIndex) {
		
		LayoutSize template = book.getLayoutSize();
		
		float pw = book.isVertical() ? book.getPageHeight() : book.getPageWidth();
		float ph = book.isVertical() ? book.getPageWidth() : book.getPageHeight();
		
		float si = template.getSlugInside().floatValue();
		float so = template.getSlugOutside().floatValue();
		float st = template.getSlugTop().floatValue();
		float sb = template.getSlugBottom().floatValue();
		
		cb.saveState();
		cb.setColorStroke(kSeparationAll, 1.0f); 
		cb.setColorFill(kSeparationAll, 0.5f);
		cb.setLineWidth(kPrinterMarkLineWidth);
		
		float xmarks[] = getHorizontalMarks(book, pageIndex);
		BaseColor xcolors[] = getHorizontalMarksColors(book, pageIndex);
		float xlen[] = getHorizontalMarkLengths(book, pageIndex);
		int i = 0;
		for(float x:xmarks)
		{
			BaseColor color = xcolors[i];
			float ext = xlen!=null ? xlen[i] : 0f;
			cb.setColorStroke(color);
			drawHorizontalTrimMark(cb, x, 0, sb + ext );
			drawHorizontalTrimMark(cb, x, ph-st - ext, st + ext);
			i++;
		}
		float ymarks[] = getVerticalMarks(book, pageIndex);
		float ylen[] = getVerticalMarkLengths(book, pageIndex);
		BaseColor ycolors[] = getVerticalMarksColors(book, pageIndex);
		i = 0;
		for(float y:ymarks) {
			BaseColor color = ycolors[i];
			cb.setColorStroke(color);
			float ext = ylen!=null ? ylen[i] : 0f;
			if (this instanceof QbicPrintingMarksRenderer && (i==1 || i==2)) {
				if (pageIndex%2==0)
				{
					//back cover
					drawVerticalTrimMark(cb, 0, y, si + ext);
					drawVerticalTrimMark(cb, pw - so, y, si);
				} else {
					drawVerticalTrimMark(cb, 0, y, si);
					drawVerticalTrimMark(cb, pw - so - ext, y, si + ext);
				}
				
			} else {
				drawVerticalTrimMark(cb, 0, y, si + ext);
				drawVerticalTrimMark(cb, pw - so - ext, y, si + ext);
			}
			i++;
		}
		
		cb.restoreState();
	}
	
	protected void drawLabels(PdfContentByte cb, PdfPageInfo book,
			int pageIndex, Product product)
	{
		
	}
	
	protected void drawText(PdfContentByte cb, String text, 
			float x, float y, Font font, Object color)
	{
		BaseFont baseFont = font.getCalculatedBaseFont(false);
		cb.saveState();
		cb.beginText();
		cb.setFontAndSize(baseFont, font.getSize());
		cb.moveText(x,y);
		
		if (color instanceof PdfSpotColor)
		{
			cb.setColorStroke((PdfSpotColor)color, 1.0f); 
			cb.setColorFill((PdfSpotColor)color, 1.0f);
		} else if (color instanceof BaseColor) {
			cb.setColorStroke((BaseColor)color); 
			cb.setColorFill((BaseColor)color);
		}
		cb.showTextAligned(PdfContentByte.ALIGN_CENTER, text, x, y, 0);
		cb.endText();
			
		cb.restoreState();
	}

}
