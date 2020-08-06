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

public class FicPrintingMarksRenderer extends CoverPrintingMarksRenderer {
	
	private static final BaseColor GREEN = new BaseColor(new Color(0x6ba63b));
	private static final BaseColor BLUE = new BaseColor(new Color(0x00a2df));
	
	public FicPrintingMarksRenderer() { }
	
	@Override
	protected float[] getHorizontalMarks(PdfPageInfo book, int pageIndex) {
		
		LayoutSize template = book.getLayoutSize();
		
		float pw = book.isVertical() ? book.getPageHeight() : book.getPageWidth();
		float bi = template.getBleedInside().floatValue();
		float bo = template.getBleedOutside().floatValue();
		float si = template.getSlugInside().floatValue();
		float so = template.getSlugOutside().floatValue();
		
		float spine1 = pw/2.0f - template.getSpineWidth().floatValue()/2.0f;
		float spine2 = pw/2.0f + template.getSpineWidth().floatValue()/2.0f;
		float hingeGap = template.getHingeGap();
		
		float marks[] = {si, 
				bi+si, 
				pw - bo - so, 
				pw - so, 
				spine1, spine2, //spine crops
				spine1 - hingeGap, spine2 + hingeGap //hinge crops
			};
		
		return marks;
		
	}
	
	protected BaseColor[] getHorizontalMarksColors(PdfPageInfo book, int pageIndex)
	{
		BaseColor[] colors = {BaseColor.BLACK, BaseColor.RED, BaseColor.RED, BaseColor.BLACK, 
				BaseColor.RED, BaseColor.RED, //spine crops 
				GREEN, GREEN //hinge crops 
			};
		return colors;
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
				(float)( (ph - st) - (0.1d * ApplicationConstants.POINTS_PER_CM) ),
				};
		
		return marks;
	}
	
	protected float[] getHorizontalMarkLengths(PdfPageInfo book, int pageIndex)
	{
		float[] len = {0f, 0.125f * 72f, 0.125f * 72f, 0f, 
				0.125f * 72f, 0.125f * 72f, 0.125f * 72f, 0.125f * 72f};
		return len;
	}
	
	protected float[] getVerticalMarkLengths(PdfPageInfo book, int pageIndex)
	{
		float[] len = {0f, 0.125f * 72f, 0.125f * 72f, 0f, 0f, 0f, 0f};
		return len;
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
	protected void drawLabels(PdfContentByte cb, PdfPageInfo book, int pageIndex, Product product) {
		float pw = book.isVertical() ? book.getPageHeight() : book.getPageWidth();
		float ph = book.isVertical() ? book.getPageWidth() : book.getPageHeight();
		
		Font font = new Font(Font.getFamily("Arial"), 14f);
		Font fontBold = new Font(Font.getFamily("Arial"), 18f, Font.BOLD);
		drawText(cb, "BACK", pw*0.25f, ph - 13f, font, kSeparationAll);
		drawText(cb, "FRONT", pw*0.75f, ph - 13f, font, kSeparationAll);
		
		IrisProduct irisProduct = new IrisProduct(product);
		
		String shape = IrisUtils.getShapePaperCode(irisProduct.getShapeCode(), 
				irisProduct.getProductType(), irisProduct.getPaperTypeCode());
		
		String jobId = irisProduct.getJobId();
		if (jobId==null)
			jobId = product.getName();
		
		String info = String.format("%s %s", shape, jobId);
		drawText(cb, info, pw*0.80f, 2f, fontBold, BaseColor.RED);
	}
	

}
