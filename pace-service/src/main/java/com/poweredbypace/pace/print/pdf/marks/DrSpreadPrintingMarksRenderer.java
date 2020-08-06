package com.poweredbypace.pace.print.pdf.marks;

import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfSpotColor;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;
import com.poweredbypace.pace.print.pdf.PrintingMarksRenderer;
import com.poweredbypace.pace.print.pdf.SpreadInfo;

public class DrSpreadPrintingMarksRenderer implements PrintingMarksRenderer {
	
	protected static final PdfSpotColor REGISTRATION_COLOR = new PdfSpotColor("All", new GrayColor(1.0f));
	protected static final float PRINTER_MARK_LINE_WIDTH = 0.25f;
	protected static final BaseColor CMYK_RED = new CMYKColor(0.15f, 1f, 1f, 0f);
	
	@Override
	public void draw(PdfContentByte cb, PdfPageInfo pageInfo,
			Product product, IccProfile profile, int pageIndex, int numPages)
			throws DocumentException, IOException {
		
		SpreadInfo spreadInfo = (SpreadInfo) pageInfo;
		drawBleedRect(cb, spreadInfo, profile);
	}
	
	protected void drawBleedRect(PdfContentByte cb, SpreadInfo pageInfo, IccProfile profile)
	{
		cb.saveState();
		
		if (profile.getColorSpace()==ColorSpace.Cmyk)
			cb.setColorStroke(REGISTRATION_COLOR, 1.0f);
		else
			cb.setColorStroke(BaseColor.BLACK);
		
		cb.setLineWidth(PRINTER_MARK_LINE_WIDTH);
		Rectangle bleedRect = pageInfo.getBleedRect(); 
		cb.rectangle(bleedRect.getLeft(), bleedRect.getBottom(), bleedRect.getWidth(), bleedRect.getHeight());
		cb.stroke();
		
		//draw red line in the middle
		Rectangle trimRect = pageInfo.getTrimRect();
		float x = bleedRect.getLeft() + bleedRect.getWidth()/2.0f;
		
		cb.setColorStroke(profile.getColorSpace()==ColorSpace.Rgb ? BaseColor.RED : CMYK_RED);
		cb.setLineWidth(0.15f * ApplicationConstants.POINTS_PER_CM);
		
		float markLen = (bleedRect.getTop() - trimRect.getTop())/2.0f;
		cb.moveTo(x, bleedRect.getTop());
		cb.lineTo(x, bleedRect.getTop() - markLen);
		cb.stroke();
		
		cb.moveTo(x, bleedRect.getBottom());
		cb.lineTo(x, bleedRect.getBottom() + markLen);
		cb.stroke();
		
		cb.restoreState();
	}
	
	
}
