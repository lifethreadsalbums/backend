package com.poweredbypace.pace.binderyform.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.binderyform.BinderyFormRenderer;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.exception.ImageProcessingException;
import com.poweredbypace.pace.irisbook.IrisConstants.BoxStyle;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.util.DateUtils;

public class BfStampsRenderer implements BinderyFormRenderer {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	@Autowired
	private StorageService storageService;
	
	
	@Override
	public void render(Document document, PdfWriter writer, Product product,
			LayoutSize bookTemplate, LayoutSize coverTemplate,
			Spread firstPage, Spread lastPage, Spread coverPage,
			JobProgressInfo job, int minProgress, int maxProgress)
			throws IOException, DocumentException, ImageProcessingException {
		
		document.add(getStampsTable(document, writer, new IrisProduct(product), coverTemplate!=null));
		
	}


	protected PdfPTable getStampsTable(Document doc, 
			PdfWriter writer,
			IrisProduct product, 
			boolean hasCover) throws DocumentException, IOException
	{
		if (!product.hasStamp())
			return createEmptyTable();
		
		List<StampInfo> stamps = BinderyFormHelper.getStamps(product.getProduct());
		boolean customDie = product.getCustomDie() &&
				product.getCustomDieUrl()!=null;
		
		if (stamps.size()==0 && !customDie)
			return createEmptyTable();
		
		float pos = writer.getVerticalPosition(false);
		
		//boolean hasBox = product.hasBox();
		boolean countLines = true;
		int numBookStamps = 0;
		int numBoxStamps = 0;
		int totalStamps = 0;
		for(StampInfo si:stamps)
		{
			if (si.getBookStamp())
				numBookStamps++;
			if (si.getBoxStamp())
				numBoxStamps++;
			if (si.getBookStamp() || si.getBoxStamp())
				totalStamps++;
			if (si.getBookStamp()!=si.getBoxStamp())
			{
				countLines = false;
				//sbreak;
			}	
		}
	
		
		if (numBookStamps==0 && numBoxStamps==0 && !customDie)
			return createEmptyTable();
		
		float docWidth = doc.right() - doc.left();
		
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100f);
		float[] colWidths = new float[] {95f, 75f, 20f, 20f};
		if (totalStamps==1)
			colWidths[0] = 0;
		
		float maxWidth = 0;
		float w1 = BinderyFormHelper.getFont(14).getCalculatedBaseFont(false).getWidthPoint("Font: ", 14);
		BaseFont bf = BinderyFormHelper.getFont(14, true).getCalculatedBaseFont(false);
		for(StampInfo si:stamps)
		{
			float w = w1+bf.getWidthPoint(si.getFontName(), 14);
			if (w>maxWidth)
				maxWidth = w;
		}
		colWidths[2] = maxWidth + 3f;
		colWidths[3] = docWidth - colWidths[2] - colWidths[1] - colWidths[0];
		table.setTotalWidth(colWidths);
		
		float minFontSize = 72f;
		for(StampInfo si:stamps)
		{
			if (si.getFont()==null)
				continue;
			
			Font stampFont = FontFactory.getFont(si.getFont(), BaseFont.CP1252, true, si.getFontSize());
			if (stampFont==null)
				continue;
				
			Font font = BinderyFormHelper.fitFont(si.getText(), colWidths[3] - 10f, 
					new Font(stampFont), 72);
			if (font.getSize()<minFontSize)
				minFontSize = font.getSize();
			
		}
		//add some space when there's no notes and logos
		boolean hasNotes = product.getAdminNotes()!=null && product.getAdminNotes().length()>0;
		boolean hasLogos = product.getStudioSample() || product.getCustomLogo();
		
		int maxLines = hasCover ? 2 : 2;
		boolean multipleLines = stamps.size() > maxLines;
		
		if (!(hasNotes || hasLogos) || 
			(multipleLines && StringUtils.equals(product.getBoxTypeCode(), BoxStyle.CLAM_SHELL))) 
		{
			table.setSpacingBefore(0.25f * 72f);
			pos -= 0.25f * 72f;
		}
		
		Phrase header = new Phrase();
		header.add(new Chunk("STAMP",BinderyFormHelper.getFont(18, true)));
		
		if (customDie)
		{
			header.add(new Chunk(" - ", BinderyFormHelper.getFont(18, true)));
			Chunk c = new Chunk("CUSTOM DIE", BinderyFormHelper.getFont(18, true));
			//c.setBackground(new BaseColor(0xfff101));
			header.add(c);
		}
		
//		if (hasBox)
//		{
//			if (numBookStamps>0 && numBoxStamps==0)
//			{
//				header.add(new Chunk(" - ", BinderyFormHelper.getFont(18, true)));
//				Chunk c = new Chunk("BOOK ONLY", BinderyFormHelper.getFont(18, true));
//				c.setBackground(new BaseColor(0xfff101));
//				header.add(c);
//				
//			} else if (numBookStamps==0 && numBoxStamps>0)
//			{
//				header.add(new Chunk(" - ", BinderyFormHelper.getFont(18, true)));
//				Chunk c = new Chunk("BOX ONLY", BinderyFormHelper.getFont(18, true));
//				c.setBackground(new BaseColor(0xfff101));
//				header.add(c);
//			} else if (numBookStamps>0 && numBoxStamps>0 && !countLines)
//			{
//				Chunk c = new Chunk(" - ", BinderyFormHelper.getFont(14, true));
//				c.setTextRise(2f);
//				header.add(c);
//				c = new Chunk("BOOK & BOX ARE NOT THE SAME STAMP", BinderyFormHelper.getFont(14, true));
//				c.setBackground(new BaseColor(0xfff101));
//				c.setTextRise(2f);
//				header.add(c);
//			}
//		}
		
		
		PdfPCell cell = new PdfPCell(header);
		cell.setUseAscender(true);
		cell.setUseDescender(true);
		cell.setColspan(4);
		cell.setBorder(0);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		table.addCell(cell);
		pos -= header.getLeading();
		
		int i=1;
		int bookCount = 1;
		int boxCount = 1;
		for(StampInfo si:stamps)
		{
			if (!si.getBookStamp() && !si.getBoxStamp())
				continue;
			
			PdfPCell c=null;
			if (countLines)
			{
				c = new PdfPCell(
						new Phrase(String.format("%d%s line", i, DateUtils.getOrdinalFor(i)),
								BinderyFormHelper.getFont(12, true)));
				i++;
			} else {
				String label = si.getType();
				float fontSize = 14f;
				if (si.getBookStamp() && si.getBoxStamp() && 
					numBookStamps + numBoxStamps>1)
				{
					label += String.format(" %d%s:", bookCount, DateUtils.getOrdinalFor(bookCount));
					bookCount++;
					boxCount++;
					fontSize = 11f;
				} else if (si.getBookStamp() && numBookStamps>1)
				{
					label += String.format(" %d%s:", bookCount, DateUtils.getOrdinalFor(bookCount));
					bookCount++;
					fontSize = 11f;
				} else if (si.getBoxStamp() && numBoxStamps>1)
				{
					label += String.format(" %d%s:", boxCount, DateUtils.getOrdinalFor(boxCount));
					boxCount++;
					fontSize = 11f;
				} else 
					label += ":";
				c = new PdfPCell(new Phrase(label, BinderyFormHelper.getFont(fontSize, true)));
			}
			
			c.setBorder(0);
			c.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c.setUseAscender(true);
			c.setUseDescender(true);
			table.addCell(c);
			
			c = new PdfPCell(BinderyFormHelper.createTitleValueParagraph("Foil", si.getFoil()));
			c.setBorder(0);
			c.setVerticalAlignment(Element.ALIGN_MIDDLE);
			c.setUseAscender(true);
			c.setUseDescender(true);
			if (customDie)
				c.setColspan(multipleLines ? 4 : 3);
			table.addCell(c);
			
			pos -= c.getPhrase().getLeading() + 4;
			
			if (!customDie)
			{
				c = new PdfPCell(BinderyFormHelper.createTitleValueParagraph("Font", si.getFontName()));
				c.setBorder(0);
				c.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c.setUseAscender(true);
				c.setUseDescender(true);
				c.setPadding(0);
				if (!multipleLines)
					c.setColspan(2);
				table.addCell(c);
			}
			
			if (multipleLines)
			{
				Font font = FontFactory.getFont(si.getFont(), BaseFont.CP1252, true, si.getFontSize());
				font.setSize(minFontSize);
				c = new PdfPCell(new Phrase(si.getText(), font));
				c.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setBorder(0);
				c.setUseAscender(true);
				c.setUseDescender(true);
				table.addCell(c);
			}
		}
		
		if (!multipleLines)
		{
			if (StringUtils.equals(product.getBoxTypeCode(), BoxStyle.CLAM_SHELL)) {
				float spacer = 0.1f * 72f;
				PdfPCell spacerCell = new PdfPCell();
				spacerCell.setFixedHeight(spacer);
				spacerCell.setBorder(0);
				spacerCell.setColspan(4);
				table.addCell(spacerCell);
				pos -= spacer;
			}
			
//			PdfContentByte canvas = writer.getDirectContent();
//			canvas.saveState();
//			canvas.setLineWidth(1f);
//			canvas.moveTo(0, pos);
//			canvas.lineTo(docWidth, pos);
//			canvas.stroke();
//			canvas.restoreState();
			
			if (customDie) {
				float pos2 = writer.getVerticalPosition(false);
				
				float customDieHeight = pos2 - (1.35f * 72f);
				
				if (hasNotes && hasLogos)
					customDieHeight -= 1.47 * 72f;
				else if (hasNotes)
					customDieHeight -= 0.67f * 72f;
				else if (hasLogos)
					customDieHeight -= 1f * 72f;
				
				PdfPCell c = new PdfPCell(renderImage(writer, 
						ApplicationConstants.ORIGINAL_IMAGE_PATH + product.getCustomDieUrl(), 4.79f*72, customDieHeight, Element.ALIGN_CENTER));
				c.setBorder(0);
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setColspan(4);
				c.setPaddingTop(0.125f * 72);
				c.setPaddingBottom(0.125f * 72);
				table.addCell(c);
			}
			
			for(StampInfo si:stamps) {
				if (!si.getBookStamp() && !si.getBoxStamp())
					continue;
				
				float maxH = pos - 0.35f;
				
				if (hasLogos)
					maxH -= 1.0f * 72f;
				if (hasNotes)
					maxH -= 0.67f * 72f;
				
				float h = (maxH / stamps.size()) - (0.3f * 72f);
			
				Font stampFont = FontFactory.getFont(si.getFont(), BaseFont.CP1252, true, si.getFontSize());
				Font font = BinderyFormHelper.fitFont(si.getText(), docWidth, stampFont, h);
				
				PdfPCell c = new PdfPCell(new Phrase(si.getText(), font));
				c.setBorder(0);
				c.setColspan(4);
				c.setUseAscender(true);
				c.setUseDescender(true);
				c.setHorizontalAlignment(Element.ALIGN_CENTER);
				c.setVerticalAlignment(Element.ALIGN_MIDDLE);
				c.setPadding(0);
				c.setPaddingTop(9f);
				table.addCell(c);
			}
		}
		
		return table;	
	}
	
	private PdfPTable createEmptyTable()
	{
		PdfPTable t = new PdfPTable(1);
		PdfPCell c = new PdfPCell();
		c.setFixedHeight(96);
		c.setBorder(0);
		t.addCell(c);
		return t;
	}
	
	private Image renderImage(final PdfWriter writer, final String url, 
			final float thumbWidth, final float thumbHeight,
			int alignment) throws IOException, DocumentException
	{
		File imgFile = storageService.getFile(url);
		
		String ext = FilenameUtils.getExtension(imgFile.getName());
		Image image = null;
		if (ext!=null && ext.toLowerCase().equals("pdf"))
		{
			image = extractFirstPageFromPdf(writer, imgFile);
		} else
			image = Image.getInstance(imgFile.getAbsolutePath());
		
		image.setAbsolutePosition(0,0);
		image.scaleToFit(thumbWidth, thumbHeight);
		image.setAlignment(alignment);

		return image;
	}
	
	private Image extractFirstPageFromPdf(final PdfWriter writer, File pdf) throws FileNotFoundException, IOException, BadElementException
	{
		Image result = null;
		PdfReader reader = new PdfReader(new FileInputStream(pdf)); 
		if (reader.getNumberOfPages()>0)
		{
			PdfImportedPage page = writer.getImportedPage(reader, 1);
			result = Image.getInstance(page);
		} 
		reader.close();
		
		return result;
	}
	
	
}
