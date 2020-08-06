package com.poweredbypace.pace.print.pdf.marks;

import java.awt.geom.AffineTransform;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

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
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.irisbook.IrisUtils;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;
import com.poweredbypace.pace.print.pdf.PrintingMarksRenderer;
import com.poweredbypace.pace.util.PdfUtils;

public class BookPrintingMarksRenderer implements PrintingMarksRenderer {
	

	protected static final PdfSpotColor kSeparationAll = new PdfSpotColor("All", new GrayColor(1.0f));
	protected static final float kPrinterMarkLineWidth = 0.25f;
	protected static final BaseColor RED = new CMYKColor(0.15f, 1f, 1f, 0f);

	@Override
	public void draw(PdfContentByte cb, PdfPageInfo book, Product product, IccProfile iccProfile, int pageIndex, int numPages) 
		throws DocumentException, IOException {
		
		//draw trim marks
		float offset = 0f;
		boolean isPortfolio = book.getLayoutSize().getCode().indexOf("P-")==0;
		
		cb.saveState();
		if (isPortfolio && pageIndex%2==0) {
			offset = 10.3464f;
			cb.concatCTM(1, 0, 0, 1, offset, 0);
		}
		drawTrimMarks(cb, book);
		cb.restoreState();

		//draw gray color bar
		cb.saveState();
		cb.concatCTM(1, 0, 0, 1, 30 + offset, book.getPageHeight() - 14.0f);
		PdfUtils.drawGrayColorBar(cb, ColorSpace.Cmyk);
		cb.restoreState();

		//draw color bar
		cb.saveState();
		cb.concatCTM(1, 0, 0, 1, offset + book.getPageWidth() - 185.0f, book.getPageHeight() - 14.0f);
		PdfUtils.drawColorBar(cb, ColorSpace.Cmyk);
		cb.restoreState();

		drawJobInfo(cb, book, product, pageIndex, numPages);
		
	}
	
	protected void drawJobInfo(PdfContentByte cb, PdfPageInfo book, Product product, int pageIndex, int numPages)
	{
		float fontSize = 12;
		float textY = 4f;
		
		Font helveticaBold = new Font(FontFamily.HELVETICA, fontSize, Font.BOLD);
		Font helvetica = new Font(FontFamily.HELVETICA, fontSize);
		
		BaseFont bfHelvBold = helveticaBold.getCalculatedBaseFont(false);
		BaseFont bfHelv = helvetica.getCalculatedBaseFont(false);
		
		//print job info on top
		//In order from left to right:

		//1: Shape Code
		//2: Batch Number - Job ID
		//3: Color
		//4: Endpaper
		
		LayoutSize bookTemplate = book.getLayoutSize();
		
		IrisProduct irisProduct = new IrisProduct(product);
		String shapeCode = IrisUtils.getShapePaperCode(irisProduct.getShapeCode(), 
				irisProduct.getProductType(), irisProduct.getPaperTypeCode());
		
		String color = product.getProductOptionDisplayValue("bookColour");
		String endPaper = product.getProductOptionDisplayValue("endPapersColour");
		
		String coverType = product.getProductOptionCode("bookMaterial");
		if (coverType!=null) {
			if ("qbic".equals(coverType))
				color = "1/4 IC "+color;
			else if ("fic".equals(coverType))
				color = "FIC";
		}
		
		String jobId = product.getProductNumber();
		
		String[] text = new String[] { 
				shapeCode!=null ? shapeCode : "",
				jobId!=null ? jobId : "",
				color!=null ? color : "",
				endPaper!=null ? "EP:"+endPaper : ""};
		
		String shapeSize = StringUtils.isNotEmpty(bookTemplate.getCode()) ? 
				bookTemplate.getCode().replaceAll("P-","").substring(0, 1) : "";
		float fs = 8f;
		float pad = 20f;
		if (shapeSize.equals("L") || shapeSize.equals("T") || shapeSize.equals("X"))
		{
			fs = 12f;
			pad = 30f;
		} else if (shapeSize.equals("M")) {
			fs = 8f;
			pad = 20f;
		} else if (shapeSize.equals("S")) {
			fs = 6f;
			pad = 2f;
		}
		
		if (shapeSize.equals("S")) {
			cb.saveState();
			
			if ((pageIndex+1)%2!=0)
			{
				AffineTransform af = new AffineTransform();
				af.translate(book.getPageWidth(),0);
				af.rotate(Math.toRadians(90f));
				
				cb.concatCTM(af);
			} else {
				AffineTransform af = new AffineTransform();
				af.translate(PdfPageInfo.kCropMarkLength + 1,0);
				af.rotate(Math.toRadians(90f));
				cb.concatCTM(af);
			}
			
			PdfUtils.drawTextJustified(cb, 80f + pad, 4f , 
					book.getPageHeight() - 160f - (pad*2f),
					text, bfHelv, fontSize, ColorSpace.Cmyk);
			
			cb.restoreState();
		} else {
			
			PdfUtils.drawTextJustified(cb, 185f + pad, book.getPageHeight() - 6.0f - fs/2f , 
					book.getPageWidth() - 370f - (pad*2f),
					text, bfHelv, fs, ColorSpace.Cmyk);
		}
		
		cb.saveState();
		if ((pageIndex+1)%2!=0) {
			float textOffset = PdfPageInfo.kCropMarkOffset + 72.0f;
			
			cb.beginText();
			String pnum = String.format("p. %d   ", pageIndex+1);
			cb.moveText(textOffset, textY);
			cb.setFontAndSize(bfHelvBold, fontSize);
			cb.setColorStroke(RED); 
			cb.setColorFill(RED);
			cb.showText(pnum);
			cb.endText();
			textOffset += bfHelv.getWidthPoint(pnum, fontSize);
			
			//print job info: {Shape - in bold} {Batch No - Order ID} {Overall Book Page Count}
			cb.beginText();
			cb.setFontAndSize(bfHelvBold, fontSize);
			
			cb.moveText(textOffset, textY);
			String shape = shapeCode + " ";
			cb.setColorStroke(kSeparationAll, 1.0f); 
			cb.setColorFill(kSeparationAll, 1.0f);
			cb.showText(shape);
			textOffset += bfHelvBold.getWidthPoint(shape, fontSize);
			cb.endText();
			
			cb.beginText();
			cb.setFontAndSize(bfHelv, fontSize);
			cb.moveText(textOffset, textY);
			
			String info = String.format("%s pp. %d", jobId, numPages);
			
			cb.setColorStroke(kSeparationAll, 1.0f); 
			cb.setColorFill(kSeparationAll, 1.0f);
			cb.showText(info);
			cb.endText();
			
			cb.beginText();
			pnum = String.format("p. %d ", pageIndex+1);
			float w = bfHelv.getWidthPoint(pnum, fontSize);
			cb.moveText(book.getPageWidth() - PdfPageInfo.kCropMarkOffset - w, textY);
			cb.setFontAndSize(bfHelvBold, fontSize);
			cb.setColorStroke(RED); 
			cb.setColorFill(RED);
			cb.showText(pnum);
			cb.endText();
		} else {
			//However on all EVEN pages you need to flip this order to:
			// {actual page}    {SHAPE} {BATCH-ORDER ID} pp {total number of pages}
			
			float textOffset = book.getPageWidth() - (PdfPageInfo.kCropMarkOffset + 72.0f);
			
			cb.beginText();
			String pnum = String.format("   p. %d", pageIndex+1);
			textOffset -= bfHelv.getWidthPoint(pnum, fontSize);
			cb.moveText(textOffset, textY);
			cb.setFontAndSize(bfHelvBold, fontSize);
			cb.setColorStroke(RED); 
			cb.setColorFill(RED);
			cb.showText(pnum);
			cb.endText();
			
			cb.beginText();
			cb.setFontAndSize(bfHelv, fontSize);
			String info = String.format("%s pp. %d", jobId, numPages);
			textOffset -= bfHelv.getWidthPoint(info, fontSize);
			cb.moveText(textOffset, textY);
			cb.setColorStroke(kSeparationAll, 1.0f); 
			cb.setColorFill(kSeparationAll, 1.0f);
			cb.showText(info);
			cb.endText();
			
			cb.beginText();
			cb.setFontAndSize(bfHelvBold, fontSize);
			String shape = shapeCode + " ";
			textOffset -= bfHelvBold.getWidthPoint(shape, fontSize);
			cb.moveText(textOffset, textY);
			cb.setColorStroke(kSeparationAll, 1.0f); 
			cb.setColorFill(kSeparationAll, 1.0f);
			cb.showText(shape);
			cb.endText();
			
			cb.beginText();
			pnum = String.format(" p. %d", pageIndex+1);
			cb.moveText(PdfPageInfo.kCropMarkOffset, textY);
			cb.setFontAndSize(bfHelvBold, fontSize);
			cb.setColorStroke(RED); 
			cb.setColorFill(RED);
			cb.showText(pnum);
			cb.endText();
		}
		
		cb.restoreState();
		
	}
	
	
	protected void drawTrimMarks(PdfContentByte cb, PdfPageInfo book) {
		float kCropMarkLength = PdfPageInfo.kCropMarkLength;
		float kCropMarkOffset = PdfPageInfo.kCropMarkOffset;

		cb.saveState();
		cb.setColorStroke(kSeparationAll, 1.0f); 
		cb.setColorFill(kSeparationAll, 0.5f);
		cb.setLineWidth(kPrinterMarkLineWidth);

		// LL
		cb.moveTo(0f, kCropMarkOffset);
		cb.lineTo(kCropMarkLength, kCropMarkOffset);
		cb.stroke();
		cb.moveTo(kCropMarkOffset, 0f);
		cb.lineTo(kCropMarkOffset, kCropMarkLength);
		cb.stroke();

		// UL
		cb.moveTo(0, book.getPageHeight() - kCropMarkOffset);
		cb.lineTo(kCropMarkLength, book.getPageHeight() - kCropMarkOffset);
		cb.stroke();
		cb.moveTo(kCropMarkOffset, book.getPageHeight());
		cb.lineTo(kCropMarkOffset, book.getPageHeight() - kCropMarkLength);
		cb.stroke();

		// UR
		cb.moveTo(book.getPageWidth() - kCropMarkOffset, book.getPageHeight());
		cb.lineTo(book.getPageWidth() - kCropMarkOffset, book.getPageHeight() - kCropMarkLength);
		cb.stroke();
		cb.moveTo(book.getPageWidth(), book.getPageHeight() - kCropMarkOffset);
		cb.lineTo(book.getPageWidth() - kCropMarkLength, book.getPageHeight() - kCropMarkOffset);
		cb.stroke();

		// LR
		cb.moveTo(book.getPageWidth(), kCropMarkOffset);
		cb.lineTo(book.getPageWidth() - kCropMarkLength, kCropMarkOffset);
		cb.stroke();
		cb.moveTo(book.getPageWidth() - kCropMarkOffset, 0);
		cb.lineTo(book.getPageWidth() - kCropMarkOffset, kCropMarkLength);
		cb.stroke();
		cb.restoreState();
	}

	protected void drawRegistrationMarks(PdfContentByte cb, float x, float y, float kRegMarkLength) {
		cb.saveState();
		cb.setColorStroke(kSeparationAll, 1.0f); 
		cb.setColorFill(kSeparationAll, 1.0f);
		cb.setLineWidth(kPrinterMarkLineWidth);

		float x_mid = x;
		float y_mid = y;
		float offset = -kRegMarkLength/2.0f;

		// Big Black Vertical Line
		cb.moveTo(x_mid, y_mid -kRegMarkLength/2.0f);
		cb.lineTo(x_mid, y_mid + kRegMarkLength/2.0f);
		cb.stroke();

		// Big Black Horizontal Line
		cb.moveTo(x_mid + offset, y_mid);
		cb.lineTo(x_mid + offset + kRegMarkLength, y_mid);
		cb.stroke();

		// Big Circle
		cb.circle(x_mid, y_mid, 5.0f);
		cb.stroke();

		// Small Circle
		cb.circle(x_mid, y_mid, 3.0f);
		cb.fill();

		// White overlayed vertical 
		cb.setColorStroke(kSeparationAll, 0.0f); 
		cb.moveTo(x_mid, y_mid - 3.0f);
		cb.lineTo(x_mid, y_mid + 3.0f);
		cb.stroke();

		// White overlayed horizontal
		cb.moveTo(x_mid - 3.0f, y_mid);
		cb.lineTo(x_mid + 3.0f, y_mid);
		cb.stroke();    
		cb.restoreState();
	}

}
