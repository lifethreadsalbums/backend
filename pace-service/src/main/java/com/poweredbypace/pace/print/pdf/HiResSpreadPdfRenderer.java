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

public class HiResSpreadPdfRenderer extends AbstractPdfRenderer {
	
	
	public File generate(Product product, 
		Layout layout,
		List<Integer> pageNumbers,
		PrintingMarksRenderer printingMarksRenderer,
		ProgressListener progressListener)  
			throws InterruptedException, IOException, DocumentException, PrintGenerationException  
	{
		logger.info("Generating HI-RES Spread PDF...");
		Map<String, Image> imageCache = new HashMap<String, Image>();

		LayoutSize layoutSize = layout.getLayoutSize();
		IccProfile productIccProfile = iccProfileService.getIccProfile(product);
		//String batchNumber = orderService.getBatchNumber(order);

		//create document
		Document document = new Document();
		AddMetadata(document, product);
		File pdfTempFile = File.createTempFile("pace-pdf-", ".pdf");
		logger.debug("Opening PDF doc, file=" + pdfTempFile.getAbsolutePath());
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfTempFile));
		writer.setPdfVersion(PdfWriter.VERSION_1_6);
		writer.setCompressionLevel(9);
		document.open();
		
		//tweak book template for FM LP
//		IrisProduct irisProduct = new IrisProduct(product);
//		if (ProductType.FLUSHMOUNT.equals(irisProduct.getProductType()) && 
//				"LP".equals(layoutSize.getCode())) {
//			
//			layoutSize = new LayoutSize(layoutSize);
//			layoutSize.setSlugTop(-1.75f);
//			layoutSize.setSlugBottom(-1.75f);
//		}
		
		if (ColorSpace.Rgb==productIccProfile.getColorSpace()) {
			writer.setRgbTransparencyBlending(true);
			writer.getDirectContent().setDefaultColorspace(PdfName.CS, PdfName.DEVICERGB);
		}
		
		int pageIndex = 0;
		List<Spread> spreads = layout.getSpreads();
		for(int j=0;j<spreads.size();j++)
		{
			Spread page = spreads.get(j);
			if (pageNumbers!=null && pageNumbers.size()>0 && pageNumbers.indexOf(j + 1)==-1) {
				continue;
			}
			
			int numPages = page.getNumPages();
			SpreadInfo spreadInfo = new SpreadInfo(layoutSize, numPages);			
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
					ImageFile imageFile = ((ImageElement)element).getImageFile();
					if (imageFile!=null) {
						iccProfile = iccProfileService.getIccProfile(imageFile, product);
						image = getHiResImage(((ImageElement)element), iccProfile, imageCache);
					}
				}
					
				float offsetLeft = layoutSize.getBleedOutside().floatValue();
				float offsetBottom = layoutSize.getBleedBottom().floatValue();
				
				renderElement(element, x, y, offsetLeft, offsetBottom, writer, 
					pageTemplate, layoutSize, image, iccProfile);
			}
			pageCanvas.addTemplate(pageTemplate, bleedRect.getLeft(), bleedRect.getBottom());

			//draw printing marks
			printingMarksRenderer.draw(pageCanvas, 
					spreadInfo, 
					product,
					productIccProfile,
					j, 
					spreads.size());

			writer.releaseTemplate(pageTemplate);
			
			if (progressListener!=null) {
				int progress = (int) (((float)(j+1)/(float)spreads.size()) * 100f);
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
