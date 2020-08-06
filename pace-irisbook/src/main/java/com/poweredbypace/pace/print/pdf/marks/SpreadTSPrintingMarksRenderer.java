package com.poweredbypace.pace.print.pdf.marks;

import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;
import com.poweredbypace.pace.print.pdf.SpreadInfo;
import com.poweredbypace.pace.util.PdfUtils;

public class SpreadTSPrintingMarksRenderer extends SpreadPrintingMarksRenderer {
	
	private String[] topInfo;
	
	
	
	public String[] getTopInfo() {
		return topInfo;
	}

	public void setTopInfo(String[] topInfo) {
		this.topInfo = topInfo;
	}

	@Override
	public void draw(PdfContentByte cb, PdfPageInfo pageInfo,
			Product product, IccProfile profile, int pageIndex, int numPages)
			throws DocumentException, IOException {
	
		SpreadInfo spreadInfo = (SpreadInfo) pageInfo;
		Rectangle slugRect = spreadInfo.getSlugRect();
		
		//draw gray color bar
		cb.saveState();
		cb.concatCTM(1, 0, 0, 1, 30, slugRect.getHeight() - 14.0f);
		PdfUtils.drawGrayColorBar(cb, profile.getColorSpace());
		cb.restoreState();

		//draw color bar
		cb.saveState();
		cb.concatCTM(1, 0, 0, 1, slugRect.getWidth() - 185.0f, slugRect.getHeight() - 14.0f);
		
		PdfUtils.drawColorBar(cb, profile.getColorSpace());
		cb.restoreState();
		
		drawJobInfo(cb, spreadInfo, product, pageIndex, numPages, profile);
	}
	
	@Override
	protected void drawJobInfo(PdfContentByte cb, SpreadInfo pageInfo, 
			Product product, int pageIndex, int numPages, IccProfile profile)
	{
		float fontSize = 12;
		//float textY = 4f;
		
		//Font helveticaBold = new Font(FontFamily.HELVETICA, fontSize, Font.BOLD);
		Font helvetica = new Font(FontFamily.HELVETICA, fontSize);
		
		//BaseFont bfHelvBold = helveticaBold.getCalculatedBaseFont(false);
		BaseFont bfHelv = helvetica.getCalculatedBaseFont(false);
				
		//String shapeSize = bookTemplate.getCode().replaceAll("P-","").substring(0, 1);
		float fs = 8f;
		float pad = 20f;
//		if (shapeSize.equals("L") || shapeSize.equals("T") || shapeSize.equals("X"))
//		{
//			fs = 12f;
//			pad = 30f;
//		} 

		Rectangle slugRect = pageInfo.getSlugRect();
		cb.saveState();
		
		PdfUtils.drawTextJustified(cb, 185f + pad, slugRect.getHeight() - 6.0f - fs/2f , 
				slugRect.getWidth() - 370f - (pad*2f),
				topInfo, bfHelv, fs, profile.getColorSpace());
		
		
		cb.restoreState();
	}

}
