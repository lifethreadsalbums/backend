package com.poweredbypace.pace.print.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.layout.CameoElement;
import com.poweredbypace.pace.domain.layout.CameoSetElement;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.util.HibernateUtil;

public class CameoPdfRenderer extends AbstractPdfRenderer {
	
	public File generate(Product product, 
		PrintingMarksRenderer printingMarksRenderer,
		ProgressListener progressListener) throws InterruptedException, IOException, DocumentException, PrintGenerationException  
	{
		logger.info("Generating Cameo PDF...");
		
		//create document
		Document document = new Document();
		AddMetadata(document, product);
		File pdfTempFile = File.createTempFile("pace-pdf-", ".pdf");
		logger.debug("Opening PDF doc, file=" + pdfTempFile.getAbsolutePath());
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfTempFile));
		writer.setPdfVersion(PdfWriter.VERSION_1_6);
		writer.setCompressionLevel(9);
		document.open();
		
		IccProfile productIccProfile = iccProfileService.getIccProfile(product);
		if (ColorSpace.Rgb==productIccProfile.getColorSpace()) {
			writer.setRgbTransparencyBlending(true);
			writer.getDirectContent().setDefaultColorspace(PdfName.CS, PdfName.DEVICERGB);
		}
		List<CameoElement> cameos = new ArrayList<CameoElement>();
		
		for(ProductOption<?> po:product.getProductOptions()) {
			Object value = HibernateUtil.unproxy(po.getValue());
			if (value instanceof CameoSetElement) {
				CameoSetElement el = (CameoSetElement) value;
				cameos.addAll(el.getShapes());
			}
		}
		
		Map<String, Image> imageCache = new HashMap<String, Image>();
		int pageIndex = 0;
		float bleed = 0.125f * ApplicationConstants.PPI;
		for(CameoElement element:cameos) {
			
			LayoutSize layoutSize = new LayoutSize();
			layoutSize.setBleed(bleed);
			layoutSize.setSlug(18f);
			layoutSize.setWidth(element.getWidth()/2f);
			layoutSize.setHeight(element.getHeight());
			
			SpreadInfo spreadInfo = new SpreadInfo(layoutSize, 2);			
			Rectangle bleedRect = spreadInfo.getBleedRect();
			Rectangle slugRect = spreadInfo.getSlugRect();
			
			document.setPageSize(slugRect); 
			document.newPage();
			
			//create page template of size = page area + bleed area;
			PdfTemplate pageTemplate = writer.getDirectContent()
					.createTemplate( bleedRect.getWidth(), bleedRect.getHeight());
			PdfContentByte pageCanvas = writer.getDirectContent();
			
			ImageFile imageFile = element.getImageFile();
			IccProfile iccProfile = iccProfileService.getIccProfile(imageFile, product);
			Image image =  getHiResImage(element, iccProfile, imageCache);
			
			CameoElement el = new CameoElement();
			element.copy(el);
			
			el.setWidth(el.getWidth() + bleed * 2f);
			el.setHeight(el.getHeight() + bleed * 2f);
			el.setImageX(el.getImageX() + bleed);
			el.setImageY(el.getImageY() + bleed);
			
			float offsetLeft = bleed;
			float offsetBottom = bleed;
			float x = -bleed;
			float y = -bleed;
			
			renderElement(el, x, y, offsetLeft, offsetBottom, writer, 
				pageTemplate, layoutSize, image, iccProfile);
			
			pageCanvas.addTemplate(pageTemplate, bleedRect.getLeft(), bleedRect.getBottom());

			//draw printing marks
			if (printingMarksRenderer!=null) {
				printingMarksRenderer.draw(pageCanvas, 
					spreadInfo, 
					product,
					productIccProfile,
					pageIndex, 
					cameos.size());
			}

			writer.releaseTemplate(pageTemplate);

			if (progressListener!=null) {
				int progress = (int) (((float)(pageIndex+1)/(float)cameos.size()) * 100f);
				progressListener.progressChanged(progress);
			}
			pageIndex++;
		}
		
		clearImageCache(imageCache);
		logger.debug("Closing PDF doc");
		
		document.setPageCount(pageIndex);
		try {
			document.close();
		} catch (ExceptionConverter e) {
			if (e.getException() instanceof IOException)
				throw new PrintGenerationException("PDF is empty.");
		}
		return pdfTempFile;
	}

}
