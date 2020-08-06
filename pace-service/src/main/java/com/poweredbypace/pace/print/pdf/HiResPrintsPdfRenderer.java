package com.poweredbypace.pace.print.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.util.Numbers;

public class HiResPrintsPdfRenderer extends AbstractPdfRenderer {
	
	public File generate(Product product, 
			Layout layout,
			List<Integer> pageNumbers,
			PrintingMarksRenderer printingMarksRenderer,
			ProgressListener progressListener)  
					throws InterruptedException, IOException, DocumentException, PrintGenerationException  
	{
		logger.info("Generating HI-RES Prints PDF...");
		Map<String, Image> imageCache = new HashMap<String, Image>();

		LayoutSize layoutSize = layout.getLayoutSize();
		IccProfile productIccProfile = iccProfileService.getIccProfile(product);
		
		//create document
		Document document = new Document();
		AddMetadata(document, product);
		File pdfTempFile = File.createTempFile("pace-pdf-", ".pdf");
		logger.debug("Opening PDF doc, file=" + pdfTempFile.getAbsolutePath());
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfTempFile));
		writer.setPdfVersion(PdfWriter.VERSION_1_6);
		writer.setCompressionLevel(9);
		document.open();
		
		if (ColorSpace.Rgb==productIccProfile.getColorSpace()) {
			writer.setRgbTransparencyBlending(true);
			writer.getDirectContent().setDefaultColorspace(PdfName.CS, PdfName.DEVICERGB);
		}
		
		int pageIndex = 0;
		List<Spread> pages = layout.getSpreads();
		boolean isGridOrTemplateBasedLayout = (layoutSize.getGridX()!=null && layoutSize.getGridX()>0) ||
			layoutSize.getTemplateSpread()!=null;
		for(int j=0;j<pages.size();j++)
		{
			Spread page = pages.get(j);
			if (pageNumbers!=null && pageNumbers.size()>0 && pageNumbers.indexOf(j + 1)==-1) {
				continue;
			}
			if (page.getElements().size()==0) continue;
			
			LayoutSize ls = new LayoutSize(layoutSize);
			Element el = page.getElements().get(0);
			
			if (!isGridOrTemplateBasedLayout) {
				ls.setWidth(el.getWidth() - (ls.getBleedOutside().floatValue() * 2.0f));
				ls.setHeight(el.getHeight() - (ls.getBleedTop().floatValue() + ls.getBleedBottom().floatValue()) );
			}
			
			SpoPageInfo spreadInfo = new SpoPageInfo(ls, page);			
			Rectangle bleedRect = spreadInfo.getBleedRect();
			Rectangle slugRect = spreadInfo.getSlugRect();
			
			document.setPageSize(slugRect); 
			document.newPage();

			//create page template of size = page area + bleed area;
			PdfTemplate pageTemplate = writer.getDirectContent()
					.createTemplate( bleedRect.getWidth(), bleedRect.getHeight());
			PdfContentByte pageCanvas = writer.getDirectContent();

			//render elements
			for (Element element : page.getElements()) 
			{
				float x = Numbers.valueOf(element.getX());
				float y = Numbers.valueOf(element.getY());
				
				Image image = null;
				IccProfile iccProfile = productIccProfile;
				if (element.getClass().isAssignableFrom(ImageElement.class)) {
					if (!isGridOrTemplateBasedLayout) {
						x = -ls.getBleedOutside().floatValue();
						y = -ls.getBleedTop().floatValue();
					}
					ImageFile imageFile = ((ImageElement)element).getImageFile();
					if (imageFile!=null) {
						iccProfile = iccProfileService.getIccProfile(imageFile, product);
						image = getHiResImage(((ImageElement)element), iccProfile, imageCache);
					}
				}
					
				float offsetLeft = ls.getBleedOutside().floatValue();
				float offsetBottom = ls.getBleedBottom().floatValue();
				
				renderElement(element, x, y, offsetLeft, offsetBottom, writer, 
					pageTemplate, ls, image, iccProfile);
			}
			pageCanvas.addTemplate(pageTemplate, bleedRect.getLeft(), bleedRect.getBottom());

			//draw printing marks
			if (printingMarksRenderer!=null) {
				printingMarksRenderer.draw(pageCanvas, 
					spreadInfo, 
					product,
					productIccProfile,
					j, 
					pages.size());
			}

			writer.releaseTemplate(pageTemplate);

			if (progressListener!=null) {
				int progress = (int) (((float)(j+1)/(float)pages.size()) * 100f);
				progressListener.progressChanged(progress);
			}
			
			clearImageCache(imageCache);
			pageIndex++;
		}

		logger.debug("Closing PDF doc");
		
		document.setPageCount(pageIndex);
		try {
			document.close();
		} catch (ExceptionConverter e) {
			if (e.getException() instanceof IOException)
				throw new PrintGenerationException("Book PDF is empty.");
		}
		return pdfTempFile;
	}

}
