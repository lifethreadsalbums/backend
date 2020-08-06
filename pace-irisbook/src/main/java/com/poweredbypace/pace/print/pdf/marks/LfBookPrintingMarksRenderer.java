package com.poweredbypace.pace.print.pdf.marks;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.print.pdf.PdfLfPageInfo;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;
import com.poweredbypace.pace.util.PdfUtils;

public class LfBookPrintingMarksRenderer extends BookPrintingMarksRenderer {

	private static double SPINE_CROP_LINE_OFFSET = 0.1f * ApplicationConstants.CM_TO_INCH * ApplicationConstants.PPI;
	
	public void draw(PdfContentByte cb, PdfPageInfo book, Product product, IccProfile iccProfile, int pageIndex, int numPages) 
			throws DocumentException, IOException {
		cb.saveState();
		
		
		
		if (pageIndex%2==0) {
			float lfShave = 0.25f * ApplicationConstants.PPI;
			float offset = (ApplicationConstants.LF_HIDDEN_AREA - lfShave);
			cb.concatCTM(1, 0, 0, 1, offset, 0);
		}
		
		PdfLfPageInfo lfBook = new PdfLfPageInfo(book.getLayoutSize());
		//super.draw(cb, lfBook, product, iccProfile, pageIndex, numPages);
		
		//draw trim marks
		cb.saveState();
		drawTrimMarks(cb, lfBook);
		cb.restoreState();

		//draw gray color bar
		cb.saveState();
		cb.concatCTM(1, 0, 0, 1, 30, lfBook.getPageHeight() - 14.0f);
		PdfUtils.drawGrayColorBar(cb, ColorSpace.Cmyk);
		cb.restoreState();

		//draw color bar
		cb.saveState();
		cb.concatCTM(1, 0, 0, 1, lfBook.getPageWidth() - 185.0f, lfBook.getPageHeight() - 14.0f);
		PdfUtils.drawColorBar(cb, ColorSpace.Cmyk);
		cb.restoreState();

		drawJobInfo(cb, lfBook, product, pageIndex, numPages);	
		
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