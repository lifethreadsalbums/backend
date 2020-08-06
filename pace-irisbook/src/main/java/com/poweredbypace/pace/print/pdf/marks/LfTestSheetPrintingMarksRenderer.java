package com.poweredbypace.pace.print.pdf.marks;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.print.pdf.PdfLfPageInfo;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;

public class LfTestSheetPrintingMarksRenderer extends TestSheetPrintingMarksRenderer {

	private static double SPINE_CROP_LINE_OFFSET = 0.1f * ApplicationConstants.CM_TO_INCH * ApplicationConstants.PPI;
	
	public void draw(PdfContentByte cb, PdfPageInfo book, Product product, IccProfile iccProfile, int pageIndex, int numPages) 
			throws DocumentException, IOException 
	{
		cb.saveState();
		if (pageIndex%2==0)
			cb.concatCTM(1, 0, 0, 1, ApplicationConstants.LF_HIDDEN_AREA, 0);
		
		PdfLfPageInfo lfBook = new PdfLfPageInfo(book.getLayoutSize());
		super.draw(cb, lfBook, product, iccProfile, pageIndex, numPages);
		drawSpineMark(cb, lfBook, pageIndex);
		cb.restoreState();
	}
	
	private void drawSpineMark(PdfContentByte cb, PdfPageInfo book, int pageIndex)
	{
		float kCropMarkOffset = PdfPageInfo.kCropMarkOffset;
		
		cb.saveState();
		cb.setColorStroke(kSeparationAll, 1.0f); 
		cb.setColorFill(kSeparationAll, 0.5f);
		cb.setLineWidth(kPrinterMarkLineWidth);
		
		float x = pageIndex%2==0 ? 
				kCropMarkOffset - ApplicationConstants.LF_HIDDEN_AREA:
				(book.getPageWidth() - kCropMarkOffset) + ApplicationConstants.LF_HIDDEN_AREA;
		
		//Bug 1758 - Long Black Crop Line on all Lay Flats needs to move towards hinge by 0.1CM
		x += pageIndex%2==0 ? SPINE_CROP_LINE_OFFSET : -SPINE_CROP_LINE_OFFSET; 
		
		cb.moveTo(x, 0);
		cb.lineTo(x, book.getPageHeight());
		cb.stroke();
		
		cb.restoreState();
	}
	
}