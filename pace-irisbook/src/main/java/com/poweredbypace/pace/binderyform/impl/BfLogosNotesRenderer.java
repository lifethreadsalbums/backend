package com.poweredbypace.pace.binderyform.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.binderyform.BinderyFormRenderer;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.exception.ImageProcessingException;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.service.StorageService;

public class BfLogosNotesRenderer implements BinderyFormRenderer {
	
	@Autowired
	private StorageService storageService;

	private String studioLogoUrl;
	
	public void setStudioLogoUrl(String studioLogoUrl) {
		this.studioLogoUrl = studioLogoUrl;
	}


	@Override
	public void render(Document document, PdfWriter writer,
			Product product, LayoutSize layoutSize,
			LayoutSize coverLayoutSize, Spread firstPage, Spread lastPage,
			Spread coverPage, JobProgressInfo job, int minProgress,
			int maxProgress) throws IOException, DocumentException,
			ImageProcessingException {
		
		IrisProduct irisProduct = new IrisProduct(product);
		String adminNotes = irisProduct.getAdminNotes();
		String customerNotes = irisProduct.getCustomerNotes();
		
		boolean hasLogos = irisProduct.getCustomLogo() || irisProduct.getStudioSample();
		
		if (StringUtils.isNotEmpty(adminNotes) || StringUtils.isNotEmpty(customerNotes)) 
			document.add(getNotesTable(writer, irisProduct));
		
		if (hasLogos)
			document.add(getLogosTable(writer, irisProduct));
	}
	

	protected PdfPTable getLogosTable(PdfWriter writer, IrisProduct product) throws DocumentException, IOException
	{
		PdfPTable table = new PdfPTable(2);
		table.setSpacingBefore(1f);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100f);
		table.setLockedWidth(true);
		table.setTotalWidth(new float[] {3f*72, 4.79f*72});
		
		float pos = writer.getVerticalPosition(false);
		if (pos > 1.33f*72)
		{
			PdfPCell c = new PdfPCell();
			c.setFixedHeight(pos - 1.33f*72);
			c.setBorder(0);
			c.setColspan(2);
			table.addCell(c);
		}
		
		if (product.getStudioSample())
		{
			int alignment = Element.ALIGN_LEFT;
			int colspan = 1;
			if (!product.getCustomLogo())
			{
				alignment = Element.ALIGN_CENTER;
				colspan = 2;
			}
			
			PdfPCell c = new PdfPCell(renderImage(writer, 
					studioLogoUrl, 3f*72f, 0.7f*72f, alignment));
			c.setBorder(0);
			c.setHorizontalAlignment(alignment);
			c.setColspan(colspan);
			table.addCell(c);
		}
		
		if (product.getCustomLogo())
		{
			int alignment = Element.ALIGN_RIGHT;
			int colspan = 1;
			if (!product.getStudioSample())
			{
				alignment = Element.ALIGN_CENTER;
				colspan = 2;
			}
			
			PdfPCell c = new PdfPCell(renderImage(writer, 
					ApplicationConstants.ORIGINAL_IMAGE_PATH + product.getCustomLogoUrl(), 4.79f*72f, 0.7f*72f, alignment));
			c.setBorder(0);
			c.setHorizontalAlignment(alignment);
			c.setColspan(colspan);
			table.addCell(c);
		}
		
		return table;
	}
	
	protected PdfPTable getNotesTable(PdfWriter writer, IrisProduct product) throws DocumentException, IOException
	{
		PdfPTable table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.setWidthPercentage(100f);
		
		float pos = writer.getVerticalPosition(false);
		float vpos = 2 * 72f;
		if (pos > vpos)
		{
			PdfPCell c = new PdfPCell();
			c.setFixedHeight(pos - vpos);
			c.setBorder(0);
			table.addCell(c);
		}
		
		
		
		String adminNotes = product.getAdminNotes();
		String customerNotes = product.getCustomerNotes();
		
		if (StringUtils.isNotEmpty(adminNotes)) {
			Phrase p = new Phrase();
			p.add(new Chunk("ADMIN NOTES: ", BinderyFormHelper.getFont(11, true)));
			p.add(new Chunk(adminNotes, BinderyFormHelper.getFont(11)));
			PdfPCell c = new PdfPCell(p);
			//c.setFixedHeight(0.87f * 72);
			c.setBorderWidth(0.25f);
			c.setPaddingBottom(5f);
			table.addCell(c);
		}
		if (StringUtils.isNotEmpty(customerNotes)) {
			Phrase p = new Phrase();
			p.add(new Chunk("CUSTOMER NOTES: ", BinderyFormHelper.getFont(11, true)));
			p.add(new Chunk(customerNotes, BinderyFormHelper.getFont(11)));
			PdfPCell c = new PdfPCell(p);
			//c.setFixedHeight(0.87f * 72);
			c.setBorderWidth(0.25f);
			c.setPaddingBottom(5f);
			
			table.addCell(c);
		}
//		else
//			c.setBorder(1);
		
		
		return table;
	}
	
	private Image renderImage(final PdfWriter writer, final String url, 
			final float thumbWidth, final float thumbHeight,
			int alignment) throws IOException, DocumentException
	{
		Image image = null; 
		
		try {
			File imgFile = storageService.getFile(url);
			image = Image.getInstance(imgFile.getAbsolutePath());
		} catch (Exception ex) {
			image = Image.getInstance( getClass().getResource("/default-placeholder.png") );
		}
		
		image.setAbsolutePosition(0,0);
		image.scaleToFit(thumbWidth, thumbHeight);
		image.setAlignment(alignment);

		return image;
	}

}
