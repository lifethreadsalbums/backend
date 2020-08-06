package com.poweredbypace.pace.print.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.TextElement;
import com.poweredbypace.pace.domain.layout.TextStampElement;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.exception.PrintGenerationException;

public class DiePdfRenderer extends AbstractPdfRenderer {
	
	public File generate(Product product, 
		TextStampElement element, 
		ProgressListener progressListener) throws InterruptedException, IOException, DocumentException, PrintGenerationException  
	{
		logger.info("Generating Die PDF...");
		
		//create document
		Document document = new Document();
		AddMetadata(document, product);
		File pdfTempFile = File.createTempFile("pace-pdf-", ".pdf");
		logger.debug("Opening PDF doc, file=" + pdfTempFile.getAbsolutePath());
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfTempFile));
		writer.setPdfVersion(PdfWriter.VERSION_1_6);
		writer.setCompressionLevel(9);
		document.open();
		
		writer.setRgbTransparencyBlending(true);
		writer.getDirectContent().setDefaultColorspace(PdfName.CS, PdfName.DEVICERGB);
		
		float bleed = 0.125f * ApplicationConstants.PPI;
		float pad = 1.0f * ApplicationConstants.PPI;
			
		LayoutSize layoutSize = new LayoutSize();
		layoutSize.setBleed(bleed);
		layoutSize.setSlug(18f);
		layoutSize.setWidth(element.getWidth()/2f + pad );
		layoutSize.setHeight(element.getHeight() + pad);
			
		SpreadInfo spreadInfo = new SpreadInfo(layoutSize, 2);			
		Rectangle bleedRect = spreadInfo.getBleedRect();
		Rectangle slugRect = spreadInfo.getSlugRect();
		
		document.setPageSize(slugRect); 
		document.newPage();
			
		//create page template of size = page area + bleed area;
		PdfTemplate pageTemplate = writer.getDirectContent()
				.createTemplate( bleedRect.getWidth(), bleedRect.getHeight());
		PdfContentByte pageCanvas = writer.getDirectContent();
			
		float offsetLeft = layoutSize.getBleedOutside().floatValue();
		float offsetBottom = layoutSize.getBleedBottom().floatValue();
		float x = -layoutSize.getBleedOutside();
		float y = -layoutSize.getBleedTop();
		
		TextElement textElement = new TextElement();
		element.copy(textElement);
		textElement.setHeight(textElement.getHeight() + pad);
		textElement.setWidth(textElement.getWidth() + pad);
		
		renderElement(textElement, x, y, offsetLeft, offsetBottom, writer, 
			pageTemplate, layoutSize, null, null);
		
		pageCanvas.addTemplate(pageTemplate, bleedRect.getLeft(), bleedRect.getBottom());

		writer.releaseTemplate(pageTemplate);

		if (progressListener!=null) {
			progressListener.progressChanged(100);
		}
		
		logger.debug("Closing PDF doc");
		
		document.setPageCount(1);
		try {
			document.close();
		} catch (ExceptionConverter e) {
			if (e.getException() instanceof IOException)
				throw new PrintGenerationException("PDF is empty.");
		}
		return pdfTempFile;
	}

}
