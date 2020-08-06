package com.poweredbypace.pace.print.pdf.marks;

import java.awt.geom.AffineTransform;
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
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.irisbook.IrisConstants.CoverType;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.irisbook.IrisUtils;
import com.poweredbypace.pace.print.pdf.PdfPageInfo;
import com.poweredbypace.pace.print.pdf.PrintingMarksRenderer;
import com.poweredbypace.pace.print.pdf.SpreadInfo;
import com.poweredbypace.pace.util.PdfUtils;

public class SpreadPrintingMarksRenderer implements PrintingMarksRenderer {
	
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
		drawBleedRect(cb, spreadInfo, profile);

		Rectangle slugRect = spreadInfo.getSlugRect();
		
		if (!shouldHaveVerticalMarks(spreadInfo, product))
		{
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
		}

		drawJobInfo(cb, spreadInfo, product, pageIndex, numPages, profile);
	}

	
	protected boolean shouldHaveVerticalMarks(SpreadInfo pageInfo, Product product) {
		
//		LayoutSize bookTemplate = pageInfo.getLayoutSize();
//		IrisProduct irisProduct = new IrisProduct(product);
//		return (bookTemplate.getCode().equals("SS") || 
//				(irisProduct.getProductType().equals(ProductType.FLUSHMOUNT) && 
//					"LP".equals(bookTemplate.getCode())));
		
		return false;
	}
	
	protected void drawText(PdfContentByte cb, String text, float x, float y, BaseFont font, float fontSize, BaseColor color)
	{
		cb.beginText();
		cb.moveText(x, y);
		cb.setFontAndSize(font, fontSize);
		cb.setColorStroke(color); 
		cb.setColorFill(color);
		cb.showText(text);
		cb.endText();
	}
	
	protected void drawText(PdfContentByte cb, String text, float x, float y, BaseFont font, float fontSize, IccProfile profile)
	{
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
			Product product, int pageIndex, int numPages, IccProfile profile)
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
		
		LayoutSize bookTemplate = pageInfo.getLayoutSize();
		IrisProduct irisProduct = new IrisProduct(product);
		
		String shapeCode = IrisUtils.getShapePaperCode(irisProduct.getShapeCode(), 
				irisProduct.getProductType(), irisProduct.getPaperTypeCode());
		shapeCode = shapeCode + " " + irisProduct.getPageStyleLabel().toUpperCase();
		
		String color = irisProduct.getBookColourLabel();
		String endPaper = irisProduct.getBookEndPapersLabel();
		
		String coverType = irisProduct.getCoverType();
		if (coverType!=null) {
			if (coverType.equals(CoverType.QBIC))
				color = "1/4 IC "+color;
			else 
				color = "FIC";
		}
		
		String jobId = irisProduct.getJobId();
		
		String[] text = new String[] { 
			shapeCode!=null ? shapeCode : "",
			jobId!=null ? jobId : "",
			color!=null ? color : "",
			endPaper!=null ? "EP:"+endPaper : ""};
		
		String shapeSize = bookTemplate.getCode().replaceAll("P-","").substring(0, 1);
		float fs = 8f;
		float pad = 20f;
		if (shapeSize.equals("L") || shapeSize.equals("T") || shapeSize.equals("X")) {
			fs = 12f;
			pad = 30f;
		} 

		Rectangle slugRect = pageInfo.getSlugRect();
		Rectangle bleedRect = pageInfo.getBleedRect();
		
		cb.saveState();
		boolean verticalMarks = shouldHaveVerticalMarks(pageInfo, product);
			
		if (verticalMarks) {
			cb.saveState();
			AffineTransform af = new AffineTransform();
			af.translate(slugRect.getWidth(),0);
			af.rotate(Math.toRadians(90f));
			
			cb.concatCTM(af);
			Rectangle trimRect = pageInfo.getTrimRect();
			pad = 8f;
			PdfUtils.drawTextJustified(cb, trimRect.getLeft() + pad, slugRect.getWidth() - 6.0f - fs/2f , 
					slugRect.getHeight() - ((pad + trimRect.getLeft()) * 2f),
					text, bfHelv, fs, profile.getColorSpace());
			String shape = shapeCode + " ";
			float w = bfHelvBold.getWidthPoint(shape, fontSize);
			w+= bfHelv.getWidthPoint(jobId, fontSize);
			
			float textX = trimRect.getTop() - w - 15f;
			drawText(cb, shape, textX, textY, bfHelvBold, fontSize, profile);
			textX += bfHelvBold.getWidthPoint(shape, fontSize);
				
			drawText(cb, jobId, textX, textY, bfHelv, fontSize, profile);
			
			cb.restoreState();
			
			String info = String.format("%d", pageIndex + 1);
			w = bfHelvBold.getWidthPoint(info, fontSize);
			textX = bleedRect.getRight() + ((slugRect.getRight() - bleedRect.getRight()) /2f) - w/2f;
			textY = trimRect.getBottom() + 4f;
			drawText(cb, info, textX, textY, bfHelvBold, fontSize, profile.getColorSpace()==ColorSpace.Cmyk ? CMYK_RED : BaseColor.RED);
			
			drawText(cb, "*", slugRect.getRight() - 13f, slugRect.getTop() - 20f, bfHelvBold, 26f, new BaseColor(0x00,0xa2, 0xdf));
			
		} else {
			PdfUtils.drawTextJustified(cb, 185f + pad, slugRect.getHeight() - 6.0f - fs/2f , 
					slugRect.getWidth() - 370f - (pad*2f),
					text, bfHelv, fs, profile.getColorSpace());
		
			float textX = bleedRect.getLeft() + (72.0f *2);
			
			String shape = shapeCode + " ";
			drawText(cb, shape, textX, textY, bfHelvBold, fontSize, profile);
			textX += bfHelvBold.getWidthPoint(shape, fontSize) + 10f;
				
			drawText(cb, jobId, textX, textY, bfHelv, fontSize, profile);
			
			textX += bfHelv.getWidthPoint(jobId, fontSize) + 30f;
			drawText(cb, String.format("%d ss.", product.getPageCount()), textX, textY, bfHelv, fontSize, profile);
			
			String info = String.format("sp. %d", pageIndex + 1);
			textX = bleedRect.getRight() - (72.0f + bfHelvBold.getWidthPoint(info, fontSize));
			drawText(cb, info, textX, textY, bfHelvBold, fontSize, profile.getColorSpace()==ColorSpace.Cmyk ? CMYK_RED : BaseColor.RED);
			textX = bleedRect.getLeft() + 72f;
			drawText(cb, info, textX, textY, bfHelvBold, fontSize, profile.getColorSpace()==ColorSpace.Cmyk ? CMYK_RED : BaseColor.RED);
			
			
			//drawText(cb, "*", slugRect.getRight() - 15f, slugRect.getTop() - 26f, bfHelvBold, 26f, new BaseColor(0x00,0xa2, 0xdf));
			drawText(cb, "*", 5f, -1f, bfHelvBold, 26f, new BaseColor(0x00,0xa2, 0xdf));
		}
		
		cb.restoreState();
		
	}
	
	protected void drawBleedRect(PdfContentByte cb, SpreadInfo pageInfo, IccProfile profile)
	{
		cb.saveState();
		
		if (profile.getColorSpace()==ColorSpace.Cmyk)
			cb.setColorStroke(REGISTRATION_COLOR, 1.0f);
		else
			cb.setColorStroke(BaseColor.BLACK);
		
		cb.setLineWidth(PRINTER_MARK_LINE_WIDTH);
		Rectangle r = pageInfo.getBleedRect(); 
		cb.rectangle(r.getLeft(), r.getBottom(), r.getWidth(), r.getHeight());
		cb.stroke();
		cb.restoreState();
	}

	protected float[] getHorizontalMarks(SpreadInfo pageInfo, int pageIndex)
	{
		Rectangle bleedRect = pageInfo.getBleedRect();
		Rectangle trimRect = pageInfo.getTrimRect();
		
		float marks[] = {
				trimRect.getLeft(),
				bleedRect.getLeft() + bleedRect.getWidth()/2.0f,
				trimRect.getRight()
			};
		
		return marks;
	}
	
	protected Object[] getHorizontalMarksColors(SpreadInfo pageInfo, int pageIndex, IccProfile profile)
	{
		Object black = REGISTRATION_COLOR;
		Object red = CMYK_RED;
		
		if (profile.getColorSpace()==ColorSpace.Rgb)
		{
			black = BaseColor.BLACK;
			red = BaseColor.RED;
		}
		Object[] colors =  { black, red, black};
		return colors;
	}
	
	protected float[] getVerticalMarks(SpreadInfo pageInfo, int pageIndex)
	{
		Rectangle trimRect = pageInfo.getTrimRect();
		float marks[] = { trimRect.getTop(), trimRect.getBottom(), };
		return marks;
	}
	
	protected Object[] getVerticalMarksColors(SpreadInfo pageInfo, int pageIndex, IccProfile profile)
	{
		Object black = REGISTRATION_COLOR;
		
		if (profile.getColorSpace()==ColorSpace.Rgb)
		{
			black = BaseColor.BLACK;
		}
		Object[] colors = { black, black };
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
		for(float x:xmarks)
		{
			cb.setLineWidth(i==1 ? 0.15f * ApplicationConstants.POINTS_PER_CM  : PRINTER_MARK_LINE_WIDTH);
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
		for(float y:ymarks)
		{
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
