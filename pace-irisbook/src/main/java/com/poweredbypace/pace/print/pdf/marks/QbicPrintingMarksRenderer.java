package com.poweredbypace.pace.print.pdf.marks;

import java.awt.Color;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfContentByte;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.irisbook.IrisUtils;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;

public class QbicPrintingMarksRenderer extends CoverPrintingMarksRenderer {
	
	private static final BaseColor GREEN = new BaseColor(new Color(0x6ba63b));
	private static final BaseColor BLUE = new BaseColor(new Color(0x00a2df));
	
	@Override
	protected float[] getHorizontalMarks(PdfPageInfo book, int pageIndex) {
		
		LayoutSize template = book.getLayoutSize();
		
		float pw = book.isVertical() ? book.getPageHeight() : book.getPageWidth();
		float bi = template.getBleedInside().floatValue();
		float bo = template.getBleedOutside().floatValue();
		float si = template.getSlugInside().floatValue();
		float so = template.getSlugOutside().floatValue();
		
		float marksFront[] = {si, bi + si, pw - bo - so, pw - so};
		float marksBack[]  = {so, bo + so, pw - bi - si, pw - si};
		
		if (pageIndex%2==0)
			return marksBack;
		else
			return marksFront;
	}
	
	protected BaseColor[] getHorizontalMarksColors(PdfPageInfo book, int pageIndex)
	{
		BaseColor[] colors = { BaseColor.BLACK, BaseColor.RED, BaseColor.RED, BaseColor.BLACK };
		return colors;
	}
	
	@Override
	protected float[] getHorizontalMarkLengths(PdfPageInfo book, int pageIndex)
	{
		float[] len = {0f, 0.125f * 72f, 0.125f * 72f, 0f};
		return len;
	}
	
	protected float[] getVerticalMarks(PdfPageInfo book, int pageIndex)
	{
		LayoutSize template = book.getLayoutSize();
		
		float ph = book.isVertical() ? book.getPageWidth() : book.getPageHeight();
		float bt = template.getBleedTop().floatValue();
		float bb = template.getBleedBottom().floatValue();
		float st = template.getSlugTop().floatValue();
		float sb = template.getSlugBottom().floatValue();
		
		float marks[] = {sb, bb+sb, ph - bt - st, ph - st,
				(float)( (ph - st) + (0.1d * ApplicationConstants.POINTS_PER_CM) ),
				(float)( (ph - st) + (0.2d * ApplicationConstants.POINTS_PER_CM) ),
				(float)( (ph - st) - (0.1d * ApplicationConstants.POINTS_PER_CM) )};
		
		return marks;
	}
	
	protected BaseColor[] getVerticalMarksColors(PdfPageInfo book, int pageIndex)
	{
		BaseColor[] colors = {BaseColor.BLACK, BaseColor.RED, BaseColor.RED, BaseColor.BLACK, 
				BaseColor.RED,
				BLUE,
				GREEN};
		return colors;
	}
	
	@Override
	protected float[] getVerticalMarkLengths(PdfPageInfo book, int pageIndex)
	{
		float[] len = {0f, 0.125f * 72f, 0.125f * 72f, 0f, 0f, 0f, 0f};
		return len;
	}
	
	@Override
	protected void drawLabels(PdfContentByte cb, PdfPageInfo book, int pageIndex, Product product) {
		float pw = book.isVertical() ? book.getPageHeight() : book.getPageWidth();
		float ph = book.isVertical() ? book.getPageWidth() : book.getPageHeight();
		
		Font font = new Font(Font.getFamily("Arial"), 14f);
		Font fontBold = new Font(Font.getFamily("Arial"), 18f, Font.BOLD);
		String text = pageIndex%2==0 ? "BACK" : "FRONT";
		drawText(cb, text, pw*0.5f, ph - 13f, font, kSeparationAll);
		
		IrisProduct irisProduct = new IrisProduct(product);
		
		String shape = IrisUtils.getShapePaperCode(irisProduct.getShapeCode(), 
				irisProduct.getProductType(), irisProduct.getPaperTypeCode());
		
		String info = String.format("%s %s", shape, irisProduct.getJobId());
		drawText(cb, info, pw*0.60f, 2f, fontBold, BaseColor.RED);
	}

}
