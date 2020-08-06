package com.poweredbypace.pace.print.pdf.marks;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfSpotColor;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;
import com.poweredbypace.pace.print.pdf.PrintingMarksRenderer;

public class HemlockTSPrintingMarksRenderer implements PrintingMarksRenderer {
	
	protected static final PdfSpotColor kSeparationAll = new PdfSpotColor("All", new GrayColor(1.0f));
	protected static final float kPrinterMarkLineWidth = 0.25f;
	protected static final BaseColor RED = new CMYKColor(0.15f, 1f, 1f, 0f);

	@Override
	public void draw(PdfContentByte cb, PdfPageInfo book, Product product, IccProfile iccProfile, int pageIndex, int numPages) 
			throws DocumentException, IOException 
	{
//		//draw gray color bar
//		cb.saveState();
//		cb.concatCTM(1, 0, 0, 1, 30, book.getPageHeight() - 14.0f);
//		drawGrayColorBar(cb);
//		cb.restoreState();
//
//		//draw color bar
//		cb.saveState();
//		cb.concatCTM(1, 0, 0, 1, book.getPageWidth() - 185.0f, book.getPageHeight() - 14.0f);
//		drawColorBar(cb);
//		cb.restoreState();
//
//		drawJobInfo(cb, book, order, bookDetails, batchNumber, pageIndex, numPages);
	}
	
	protected void drawJobInfo(PdfContentByte cb, PdfPageInfo book, 
			Product product, int pageIndex, int numPages)
	{
		float fontSize = 12;
		Font helvetica = new Font(FontFamily.HELVETICA, fontSize);
		
		BaseFont bfHelv = helvetica.getCalculatedBaseFont(false);
		
		//print Batch Number - Job ID
		
		IrisProduct irisProduct = new IrisProduct(product);
		String jobId = product.getId().toString();
		String batchJobId = (product.getBatch()!=null ? product.getBatch().getName() + "-" : "") + jobId;
		String paperType = irisProduct.getPaperTypeLabel();
		
		String[] text = new String[] { 
			(batchJobId!=null ? batchJobId : "") + " " + paperType.toUpperCase(),
		};
		
		float fs = 12f;
		float pad = 30f;
		
		drawTextJustified(cb, 185f + pad, book.getPageHeight() - 6.0f - fs/2f , 
				book.getPageWidth() - 370f - (pad*2f),
				text, bfHelv, fs);
	}
	
	protected void drawTextJustified(PdfContentByte cb, float x, float y, float width, 
			String[] text, BaseFont font, float fontSize)
	{
		cb.saveState();
		
		float textWidth = 0f;
		for(int i=0;i<text.length;i++)
		{
			textWidth += font.getWidthPoint(text[i], fontSize);
		}
		
		cb.setFontAndSize(font, fontSize);
		cb.setColorStroke(kSeparationAll, 1.0f); 
		cb.setColorFill(kSeparationAll, 1.0f);
		
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
	
	protected void drawGrayColorBar(PdfContentByte cb) {
		float [] tints = {1.0f, 0.9f, 0.8f, 0.7f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.1f, 0.0f};
		cb.saveState();
		cb.setColorStroke(kSeparationAll, 0.65f); 
		cb.setLineWidth(1.0f);

		float x = 0.5f;
		for (int i=0; i<tints.length; ++i) {
			cb.setColorFill(kSeparationAll, tints[i]);
			cb.rectangle(x, 0.0f, 14.0f, 14.0f);
			cb.fill();
			x += 14.0;
		}

		x = 0.5f;
		for (int i=0; i<tints.length; ++i) {
			cb.setColorFill(kSeparationAll, tints[i]);
			cb.rectangle(x, -0.5f, 14.0f, 14.5f);
			cb.stroke();
			x += 14.0;
		}

		cb.restoreState();  
	}

	protected void drawColorBar(PdfContentByte cb) {
		CMYKColor [] colors = {
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
		cb.setColorStroke(kSeparationAll, 0.65f); 
		cb.setLineWidth(1.0f);

		float x = 0.5f;
		for (int i=0; i<colors.length; ++i) {
			cb.setColorFill(colors[i]);
			cb.rectangle(x, 0.0f, 14.0f, 14.0f);
			cb.fill();
			x += 14.0;
		}

		x = 0.5f;
		for (int i=0; i<colors.length; ++i) {
			cb.rectangle(x, -0.5f, 14.0f, 14.5f);
			cb.stroke();
			x += 14.0;
		}

		cb.restoreState();  
	}

	
}
