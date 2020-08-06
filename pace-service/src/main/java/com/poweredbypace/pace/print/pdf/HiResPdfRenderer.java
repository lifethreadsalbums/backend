package com.poweredbypace.pace.print.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

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
import com.poweredbypace.pace.domain.PrototypeProduct.FirstPageType;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.util.Numbers;

@Component
public class HiResPdfRenderer extends AbstractPdfRenderer {
	protected final Log logger = LogFactory.getLog(getClass());
	
	// Marks
	static final float kPrinterMarkLineWidth = 0.25f;
	static final float kRegMarkSize = 12f;

	static final float DPS_LF_OFFSET = 0.2f; //2mm
	static final float SPS_LF_OFFSET = 0.1f; //1mm
	static final float DPS_TOLERANCE = 0.125f; //0.125"

	@SuppressWarnings("resource")
	public File generate(
		Product product, 
		Layout layout,
		List<Integer> pageNumbers,
		PrintingMarksRenderer printingMarksRenderer,
		ProgressListener progressListener) 
			throws InterruptedException, IOException, DocumentException, PrintGenerationException {
		
		Map<String, Image> imageCache = new HashMap<String, Image>();
		
		LayoutSize layoutSize = layout.getLayoutSize();
		boolean isSpreadBased = product.getPrototypeProduct().getProductPageType()==ProductPageType.SpreadBased;
		
		//create document
		PdfPageInfo pdfPageInfo = new PdfPageInfo(layoutSize);
		Document document = new Document(
				new Rectangle(pdfPageInfo.getPageWidth(), 
						pdfPageInfo.getPageHeight()), 
						0, 0, 0, 0);

		AddMetadata(document, product);

		File pdfTempFile = File.createTempFile("pace-pdf-", ".pdf");
		logger.debug("Opening PDF doc, file=" + pdfTempFile.getAbsolutePath());
		
		float lfShave = 0.25f * ApplicationConstants.PPI;
		boolean isPortfolio = layoutSize.getCode().indexOf("P-")==0;

		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfTempFile));
		writer.setBoxSize("trim", new Rectangle(PdfPageInfo.kCropMarkOffset, PdfPageInfo.kCropMarkOffset, 
				layoutSize.getWidth().floatValue() + PdfPageInfo.kCropMarkOffset, 
				layoutSize.getHeight().floatValue() + PdfPageInfo.kCropMarkOffset));

		writer.setPdfVersion(PdfWriter.VERSION_1_6);
		writer.setCompressionLevel(9);
		
		document.open();
		IccProfile productIccProfile = iccProfileService.getIccProfile(product);
		assert(productIccProfile!=null);
		
		if (ColorSpace.Rgb==productIccProfile.getColorSpace()) {
			writer.setRgbTransparencyBlending(true);
			writer.getDirectContent().setDefaultColorspace(PdfName.CS, PdfName.DEVICERGB);
		}
		
		double dps_lf_offset = DPS_LF_OFFSET * POINTS_PER_CM;
		double sps_lf_offset = SPS_LF_OFFSET * POINTS_PER_CM;
		
		int pageIndex = 0;
		boolean isLayFlat = BooleanUtils.isTrue(layout.getIsLayFlat());
		
		List<Spread> spreads = layout.getSpreads();
		for(int j=0;j<spreads.size();j++) {
			
			if (Thread.currentThread().isInterrupted()) {
				//clean up and throw exception
				document.close();
				pdfTempFile.delete();
				throw new InterruptedException("Task cancelled by user");
			}
			
			Spread page = spreads.get(j);
			int numPages = page.getNumPages();
			
			for (int i = 0; i < numPages; i++) {
				if (pageNumbers!=null) {
					boolean skip = (isSpreadBased && pageNumbers.indexOf(i)==-1) || 
						(!isSpreadBased && pageNumbers.indexOf(pageIndex+1)==-1);
					
					if (skip) {
						pageIndex++;
						if (progressListener!=null) {
							int progress = (int) (((float)(j+1)/(float)spreads.size()) * 100f);
							progressListener.progressChanged(progress);
						}
						continue;
					}
				}
				//logger.info(String.format("Processing page %d", pageIndex+1));
				boolean isLPS = layout.getFirstPageType()==FirstPageType.LeftPageStart;
				boolean isLeftPage = (isLPS && pageIndex%2==0) || (!isLPS && pageIndex%2!=0);
				boolean isRightPage = !isLeftPage;
				
				document.setPageSize(new Rectangle(pdfPageInfo.getPageWidth(), pdfPageInfo.getPageHeight())); 
				
				if (isLayFlat) {
					document.setPageSize(new Rectangle(pdfPageInfo.getPageWidth() - lfShave, pdfPageInfo.getPageHeight())); 
				}
				if (isPortfolio && !isLayFlat) {
					document.setPageSize(new Rectangle(pdfPageInfo.getPageWidth() + 10.3464f, pdfPageInfo.getPageHeight()));
				}
				
				writer.setBoxSize("trim", new Rectangle(
					PdfPageInfo.kCropMarkOffset, 
					PdfPageInfo.kCropMarkOffset, 
					layoutSize.getWidth().floatValue() + PdfPageInfo.kCropMarkOffset, 
					layoutSize.getHeight().floatValue() + PdfPageInfo.kCropMarkOffset));
				document.newPage();

				//create page template of size = page area + bleed area;
				float templateWidth = layoutSize.getWidth().floatValue();
				float templateHeight = layoutSize.getHeight().floatValue();
				float lfExtension = 0.0f;
				if (isLayFlat) {
					templateWidth -= LF_HIDDEN_AREA;
					lfExtension = (float) dps_lf_offset;
				}
				
				float pageWidth = 0f;
				float pageHeight = 0f;
				float offsetBottom = 0f;
				float offsetLeft = 0f;
				float gap = layoutSize.getGapBetweenPages()!=null ? layoutSize.getGapBetweenPages().floatValue() : 0f;
				
				if (pdfPageInfo.isVertical()) {
					pageWidth = templateWidth + 
							layoutSize.getBleedTop().floatValue() +
							layoutSize.getBleedBottom().floatValue();
					
					pageHeight = templateHeight + lfExtension +
							layoutSize.getBleedInside().floatValue() +
							layoutSize.getBleedOutside().floatValue();
					
					offsetLeft = layoutSize.getBleedBottom().floatValue();
					offsetBottom = isLeftPage ? layoutSize.getBleedInside().floatValue() :
						layoutSize.getBleedOutside().floatValue();
				
				} else {
					pageWidth = templateWidth + 
							layoutSize.getBleedInside().floatValue() +
							layoutSize.getBleedOutside().floatValue();
					
					pageHeight = templateHeight +
							layoutSize.getBleedTop().floatValue() +
							layoutSize.getBleedBottom().floatValue();
					
					offsetLeft = isLeftPage ? 
						layoutSize.getBleedOutside().floatValue() : 
							layoutSize.getBleedInside().floatValue();
					
					offsetBottom = layoutSize.getBleedBottom().floatValue();
				}
				
				
				PdfTemplate pageTemplate = writer.getDirectContent().createTemplate(pageWidth, pageHeight);
				PdfContentByte pageCanvas = writer.getDirectContent();
					
				//check if any of the images lands on spine
				boolean frameOnSpine = false;
				boolean isDPS = false;
				for (Element element : page.getElements()) {
					if (Thread.currentThread().isInterrupted()) {
						//clean up and throw exception
						writer.releaseTemplate(pageTemplate);
						document.close();
						pdfTempFile.delete();
						throw new InterruptedException("Task cancelled by user");
					}
					
					float x = Numbers.valueOf(element.getX());
					float y = Numbers.valueOf(element.getY());
					float w = Numbers.valueOf(element.getWidth());
					float h = Numbers.valueOf(element.getHeight());
					float rot = Numbers.valueOf(element.getRotation());
					
					if (numPages==1 && BooleanUtils.isFalse(layoutSize.getDynamicSpineWidth())) {
						x+= templateWidth * 0.5f;
					} else if (isRightPage)	{
						if (pdfPageInfo.isVertical())
							y -= templateHeight + gap;
						else
							x -= templateWidth + gap;
					}
					
					if (pdfPageInfo.isVertical()) {
						float spineEdge = isRightPage ? y : y + element.getHeight().floatValue();
						float spine = isRightPage ? 0f : templateHeight;
						
						if (Math.abs(spine - spineEdge)<1.0f)
							frameOnSpine = true;
						
						float spreadY = element.getY().floatValue();
						
						float leftSideWidthIn = (templateHeight - spreadY) / 72.0f;
						float rightSideWidthIn = ((spreadY + h) - templateHeight) / 72.0f;
						if (rot==0.0f && numPages==2 && 
							leftSideWidthIn > DPS_TOLERANCE &&
							rightSideWidthIn > DPS_TOLERANCE)
							isDPS = true;
					} else {
						float spineEdge = isRightPage ? x : x + element.getWidth().floatValue();
						float spine = isRightPage ? 0f : templateWidth;
						
						if (Math.abs(spine - spineEdge)<1.0f)
							frameOnSpine = true;
						
						float spreadX = element.getX().floatValue();
						
						float leftSideWidthIn = (templateWidth - spreadX) / 72.0f;
						float rightSideWidthIn = ((spreadX + w) - templateWidth) / 72.0f;
						if (rot==0.0f && numPages==2 && 
							leftSideWidthIn > DPS_TOLERANCE &&
							rightSideWidthIn > DPS_TOLERANCE)
							isDPS = true;
					}
				}
				logger.debug(String.format("page=%d, DPS pushout=%b, bleed push out=%b", pageIndex + 1, isDPS, frameOnSpine));
				
				//add images
				for (Element element : page.getElements()) {
					float x = element.getX().floatValue();
					float y = element.getY().floatValue();
					
					if (numPages==1 && BooleanUtils.isFalse(layoutSize.getDynamicSpineWidth())) {
						x-= templateWidth * 0.5f;
					} else if (isRightPage) {
						if (pdfPageInfo.isVertical())
							y -= templateHeight + gap;
						else
							x -= templateWidth + gap;
					}
					
					//special logic for DPS on LF paperstocks
					//left page image/page will move left on the X axis
					//by 2mm and the right page image/page will move right on the X axis by 2mm.
					//Bug 1747 
					//------------------------------------------------------
					
					boolean isBackgroundFrame = false;
					
					if (isLayFlat && !isBackgroundFrame) {
						float offsetSign = isRightPage ? 1 : -1;
						if (isDPS)
							x += dps_lf_offset * offsetSign; 
						else if (frameOnSpine)
							x += sps_lf_offset * offsetSign;
					}
					
					//------------------------------------------------------
					Image image = null;
					IccProfile iccProfile = productIccProfile;
					if (element.getClass().isAssignableFrom(ImageElement.class)) {
						ImageFile imageFile = ((ImageElement)element).getImageFile();
						if (imageFile!=null) {
							iccProfile = iccProfileService.getIccProfile(imageFile, product);
							image = getHiResImage(((ImageElement)element), iccProfile, imageCache);
						}
					}
					
					renderElement(element, x, y, offsetLeft, offsetBottom, writer, 
							pageTemplate, layoutSize, image, iccProfile);
				}
				
				float pageLeftOffset = pdfPageInfo.getPageLeftOffset(pageIndex);
				
				if (isLayFlat && isRightPage) {
					pageLeftOffset += (LF_HIDDEN_AREA - lfShave);
				}
				if (isPortfolio && !isLayFlat && isRightPage) {
					pageLeftOffset += 10.3464f;
				}
				
				pageCanvas.addTemplate(pageTemplate,
					pageLeftOffset,
					pdfPageInfo.getPageBottomOffset(pageIndex));

				//draw printing marks
				if (printingMarksRenderer!=null) {
					printingMarksRenderer.draw(pageCanvas, 
						pdfPageInfo, 
						product,
						productIccProfile,
						pageIndex, 
						(spreads.size()-1)*2);
				}

				writer.releaseTemplate(pageTemplate);
				pageIndex++;
				
				if (progressListener!=null) {
					int progress = (int) (((float)(j+1)/(float)spreads.size()) * 100f);
					progressListener.progressChanged(progress);
				}
				writer.flush();
			}
			clearImageCache(imageCache);
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
