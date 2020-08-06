package com.poweredbypace.pace.print.pdf;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.FirstPageType;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.LayoutSize.PageOrientation;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.exception.PdfGenerationException;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.util.Numbers;

@Component
public class LowResPdfRenderer extends AbstractPdfRenderer {
	
	static final BaseColor kSpineLineColor = new BaseColor(0x999999);
	static final BaseColor kSpreadSpineLineColor = new BaseColor(0xe4e4e4);
	static final BaseColor kPageNumCircleColor = BaseColor.WHITE;
	static final float kSpineLineWidth = 1.5f;
	static final float kPageNumCircleSize = 9f;
	
	//@Autowired
	//private JobScheduler jobScheduler; 
	
	@Autowired
	private LayoutService layoutService;
	
	@Autowired
	private ImageService imageService;
	
	@SuppressWarnings("resource")
	public File generate(Product product, 
		ProgressListener progressListener) throws PdfGenerationException, InterruptedException, IOException   
	{
		logger.info("Generating low-res PDF...");
		
		HashMap<String, Image> images = new HashMap<String, Image>();
		File pdfTempFile = File.createTempFile("lores-pdf-", ".pdf");
		Document document = new Document();
		
		try {
			Layout layout = layoutService.getEffectiveLayout(product);
			boolean isRPS = layout.getFirstPageType()==FirstPageType.RightPageStart;
			//IrisProduct irisProduct = new IrisProduct(product);
			boolean isTS = false;// irisProduct.isTS();
			boolean isSpreadBased = product.getPrototypeProduct().getProductPageType()==ProductPageType.SpreadBased;
			boolean isLF = BooleanUtils.isTrue(layout.getIsLayFlat());
			
			LayoutSize bookTemplate = layout.getLayoutSize();
	
			//create document
			AddMetadata(document, product);
			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfTempFile));
			writer.setPdfVersion(PdfWriter.VERSION_1_6);
			writer.setCompressionLevel(9);
			document.open();
			logger.debug("Opening PDF doc, file=" + pdfTempFile.getCanonicalPath());
	
			boolean lps = !isRPS;
			int pageIndex = 0;
			int numCoverPages = 0;
			List<Layout> coverLayouts = new ArrayList<Layout>();
			List<Long> coverSizes = new ArrayList<Long>();
			
			for(Product p:product.getProductAndChildren()) {
				Layout coverLayout = p.getCoverLayout();
				if (coverLayout!=null) {
					Long sizeId = coverLayout.getLayoutSize().getId();
					
					if (BooleanUtils.isNotTrue(p.getLinkLayout()) || !coverSizes.contains(sizeId)) {
						coverLayouts.add(coverLayout);
						coverSizes.add(sizeId);
						for(Spread s:coverLayout.getSpreads()) {
							numCoverPages+=s.getNumPages();
						}
					}
				}
			}
			
			//int numSpreads = (product.getPageCount() / 2 + (lps ? 0 : 1)) + coverLayouts.size();
			
			int pageCount = product.getPageCount();
			ProductPageType pageType = product.getPrototypeProduct().getProductPageType();
			int numSpreads = ( pageType==ProductPageType.PageBased ? 
					(pageCount / 2) + (lps ? 0 : 1) : pageCount ) + 
						coverLayouts.size();
			
			int currentSpreadIndex = 0;
			for(int i=0;i<numSpreads;i++)
			{
				if (Thread.currentThread().isInterrupted())
				{
					//clean up and throw exception
					document.close();
					pdfTempFile.delete();
					throw new InterruptedException("Task cancelled by user");
				}
				
				logger.info(String.format("Processing spread %d", i));
				Spread spread = null;
				LayoutSize template = bookTemplate;
				boolean isHorizontal = template.getPageOrientation().equals(PageOrientation.Horizontal);
				
				float pageGap = 0f;
				if (pageIndex<numCoverPages)
				{
					Layout coverLayout = coverLayouts.get(currentSpreadIndex);
					template = layoutService.getEffectiveLayoutSize(product, coverLayout);
					pageGap = template.getGapBetweenPages()!=null ? 
							template.getGapBetweenPages().floatValue() : 0f;
							
					spread = coverLayout.getSpreads().get(0);
					currentSpreadIndex++;
				} else {		
					spread = layout.getSpreads().get(currentSpreadIndex - coverLayouts.size());
					currentSpreadIndex++;
				}
				
				float templateWidth = template.getWidth().floatValue();
				float templateHeight = template.getHeight().floatValue();
				
				if (isLF && pageIndex>=numCoverPages)
					templateWidth -= ApplicationConstants.LF_HIDDEN_AREA;
				
				float pageHeight = 0;
				float pageWidth = 0;
				
				if (isHorizontal)
				{
					pageWidth = templateWidth * (float)spread.getNumPages() + pageGap;
					pageHeight = templateHeight;
					
				} else {
					pageWidth = templateWidth;
					pageHeight = templateHeight * (float)spread.getNumPages() + pageGap;
				}
				
				document.setPageSize(new Rectangle(pageWidth, pageHeight)); 
				document.newPage();
	
				PdfTemplate pageTemplate = writer.getDirectContent()
						.createTemplate(pageWidth, pageHeight); 
				PdfContentByte pageCanvas = writer.getDirectContent();
				LayoutSize renderTemplate = new LayoutSize(template);
				if (!isHorizontal && spread.getNumPages()>1) 
					renderTemplate.setHeight(templateHeight*2.0f);
				
				float gapBetweenPages = template.getGapBetweenPages()!=null ? template.getGapBetweenPages().floatValue() : 0f;
				
				
				//add images
				for (Element element : spread.getElements()) {
					if (Thread.currentThread().isInterrupted()) {
						//clean up and throw exception
						writer.releaseTemplate(pageTemplate);
						document.close();
						pdfTempFile.delete();
						throw new InterruptedException("Task cancelled by user");
					}
					
					float x = Numbers.valueOf(element.getX());
					float y = Numbers.valueOf(element.getY());
	
					if (pageIndex<numCoverPages && gapBetweenPages>0) {
						if (isHorizontal &&	x > template.getWidth())
							x -= gapBetweenPages;
						
						if (!isHorizontal && y > template.getHeight())
							y -= gapBetweenPages;
					}
	
					if (spread.getNumPages()==1 && isRPS) {
						if (isHorizontal)
							x -= templateWidth/2;
						else
							y -= templateHeight;
					}
					
					Image image = null;
					ImageElement imageElement = (element instanceof ImageElement) ? (ImageElement) element : null;
					
					if (imageElement!=null && imageElement.getImageFile()!=null) {
						String url = imageElement.getImageFile().getUrl();
						if (!images.containsKey(url)) {
							logger.info("Downloading image "+url);
							try {
								File rgbFile = storageService.getFile(ApplicationConstants.LOW_RES_IMAGE_PATH + url);
								images.put(url, Image.getInstance(rgbFile.getAbsolutePath()));
							} catch (Exception ex) {
								logger.error("Cannot download image "+ url + ". " + ex.getMessage());
								image = regenerateLowResImage(url, images, imageElement);
								if (image!=null) images.put(url, image);
							}
						}
						image = images.get(url);
					}
					
					if (pageIndex<numCoverPages && gapBetweenPages>0) {
						float pagex = 0;
						float pagey = 0;
	
						Rectangle bounds = getBounds(element);
						if (isHorizontal) {
							float centerX = bounds.getLeft() + bounds.getWidth()/2.0f;
							if (centerX>templateWidth)
							{
								x -= templateWidth;
								pagex = templateWidth + gapBetweenPages;
							}
						} else {
							pagey = templateHeight + gapBetweenPages;
							float centerY = bounds.getBottom() + bounds.getHeight()/2.0f;
							if (centerY>templateHeight)
							{
								y -= templateHeight;
								pagey = 0;
							}
						}
	
						PdfTemplate coverFrame = writer.getDirectContent().createTemplate(templateWidth, templateHeight);
						renderElement(element, x, y, 0, 0, writer, coverFrame, template, image, null);
						pageTemplate.addTemplate(coverFrame, pagex, pagey);
					} else
						renderElement(element, x, y, 0, 0, writer, pageTemplate, renderTemplate, image, null);
				}
				//draw spine line and page numbers
				if (isHorizontal) {
					if (pageIndex>=numCoverPages) {
						int index = pageIndex - numCoverPages;
						String spreadNumber = Integer.toString( index / 2 + 1 );
						
						float spreadNumberPos = templateWidth;
						float spineX = templateWidth;
						float lineWidth = kSpineLineWidth;
						boolean drawSpineLine = true;
						if (isRPS && index==0) {
							spineX = kSpineLineWidth/2f;
							lineWidth = kSpineLineWidth/2f;
							spreadNumberPos = templateWidth/2f;
							spreadNumber = "0.5";
							drawSpineLine = !isSpreadBased;
						} else if (isRPS && index==product.getPageCount() - 1) {
							spineX = templateWidth - kSpineLineWidth/2f;
							lineWidth = kSpineLineWidth/2f;
							spreadNumberPos = templateWidth/2f;
							spreadNumber = Integer.toString( index / 2 ) + ".5";
							drawSpineLine = !isSpreadBased;
						}
						
						if (drawSpineLine)
							drawSpineLine(pageTemplate, spineX, templateHeight, lineWidth, !isSpreadBased);
		
						if (!isTS) {
							//draw page numbers
							if (isRPS && index==0)
								drawLowResPageNumber(pageTemplate, pageWidth - 34, 34, Integer.toString(index+1));
							else if (isRPS && index == product.getPageCount() - 1)
								drawLowResPageNumber(pageTemplate, 34, 34, Integer.toString(index+1));
							else {
								drawLowResPageNumber(pageTemplate, pageWidth - 34, 34, Integer.toString(index+2));
								drawLowResPageNumber(pageTemplate, 34, 34, Integer.toString(index+1));
							}
						}
						
						if (isSpreadBased)
						{
							//draw spread number
							drawLowResPageNumber(pageTemplate, spreadNumberPos, 34, spreadNumber, 
									14, 14, BaseColor.RED, kSpreadSpineLineColor, 1f);
						}
					} else {
						
						if (spread.getNumPages()==2) {
							drawLowResPageNumber(pageTemplate, pageWidth - 34, 34, "Fr");
							drawLowResPageNumber(pageTemplate, 34, 34, "Bk");
						}
						
						if (template.getSpineWidth()!=null && template.getSpineWidth().floatValue()>0) {
							float spineWidth = template.getSpineWidth().floatValue() / 2f;
							drawSpineLine(pageTemplate, template.getWidth().floatValue() + spineWidth, templateHeight, kSpineLineWidth, true);
							drawSpineLine(pageTemplate, template.getWidth() .floatValue()- spineWidth, templateHeight, kSpineLineWidth, true);
						}
					}
				
				} else {
					if (pageIndex>=numCoverPages) {
						int index = pageIndex - numCoverPages;
						String spreadNumber = Integer.toString( index / 2 + 1 );
						
						float spineY = templateHeight;
						float lineWidth = kSpineLineWidth;
						float spreadNumberPos = templateHeight;
						boolean drawSpineLine = true;
						if (isRPS && index==0) {
							spineY = kSpineLineWidth/2f;
							lineWidth = kSpineLineWidth/2f;
							spreadNumberPos = templateHeight/2f;
							spreadNumber = "0.5";
							drawSpineLine = !isSpreadBased;
						} else if (isRPS && index==product.getPageCount() - 1) {
							spineY = templateHeight - kSpineLineWidth/2f;
							lineWidth = kSpineLineWidth/2f;
							spreadNumberPos = templateHeight/2f;
							spreadNumber = Integer.toString( index / 2 ) + ".5";
							drawSpineLine = !isSpreadBased;
						}
		
						if (drawSpineLine)
							drawHorizontalSpineLine(pageTemplate, spineY, templateWidth, lineWidth, !isSpreadBased);
		
						if (!isTS) {
							//draw page numbers
							if (isRPS && index==0)
								drawLowResPageNumber(pageTemplate, pageWidth - 34, 34, Integer.toString(index+1));
							else if (isRPS && index == product.getPageCount() - 1)
								drawLowResPageNumber(pageTemplate, pageWidth - 34, pageHeight - 34, Integer.toString(index+1));
							else {
								drawLowResPageNumber(pageTemplate, pageWidth - 34, 34, Integer.toString(index+2));
								drawLowResPageNumber(pageTemplate, pageWidth - 34, pageHeight - 34, Integer.toString(index+1));
							}
						}
						if (isSpreadBased) {
							//draw spread number
							drawLowResPageNumber(pageTemplate, pageWidth - 34, spreadNumberPos, spreadNumber, 
									14, 14, BaseColor.RED, kSpreadSpineLineColor, 1f);
						}
					} else {
						if (spread.getNumPages()==2) {
							drawLowResPageNumber(pageTemplate, pageWidth - 34, 34, "Fr");
							drawLowResPageNumber(pageTemplate, pageWidth - 34, pageHeight - 34, "Bk");
						}
						
						if (template.getSpineWidth()!=null && template.getSpineWidth().floatValue()>0) {
							float spineWidth = template.getSpineWidth().floatValue() / 2f;
							drawHorizontalSpineLine(pageTemplate, template.getHeight().floatValue() - spineWidth, templateWidth, kSpineLineWidth, true);
							drawHorizontalSpineLine(pageTemplate, template.getHeight().floatValue() + spineWidth, templateWidth, kSpineLineWidth,true);
						}
					}
				}
				pageCanvas.addTemplate(pageTemplate, 0, 0);
				writer.releaseTemplate(pageTemplate);
				pageIndex += spread.getNumPages();
				
				if (progressListener!=null) {
					int progress = (int) (((float)(i+1)/(float)numSpreads) * 100f);
					progressListener.progressChanged(progress);
				}
			}
	
			document.setPageCount(layout.getSpreads().size());
			
		} catch (Exception ex) {
			pdfTempFile.delete();
			logger.error("", ex);
			throw new PdfGenerationException("Error while generating low res PDF", ex);
		} finally {
			logger.debug("Closing PDF doc");
			document.close();
			clearImageCache(images);
			logger.info("Finished.");
		}
		return pdfTempFile;
	}

	private Image regenerateLowResImage(String url, Map<String, Image>images, ImageElement placedElement) {
		
		try {
			logger.info("Trying to regenerate a low res image");
			File originalImage = storageService.getFile( ApplicationConstants.ORIGINAL_IMAGE_PATH + placedElement.getImageFile().getUrl());
			File lowResImage = File.createTempFile("irisbook-image-", "");
			
			logger.debug("Generating thumb and low-res images");
			imageService.resize(originalImage, lowResImage, 1000, 1000);
			Image image = Image.getInstance(lowResImage.getAbsolutePath());
			
			return image;
		} catch (Exception ex) {
			//return 
			return null;
		}
	}
	
	private void drawSpineLine(PdfContentByte cb, float x, float height, float lineWidth, boolean dashed)
	{
		cb.saveState();
		cb.setColorStroke(dashed ? kSpineLineColor : kSpreadSpineLineColor); 
		cb.setLineWidth(dashed ? lineWidth : lineWidth * 0.75f);
		if (dashed)
			cb.setLineDash(10,0);
		cb.moveTo(x, 0);
		cb.lineTo(x, height);
		cb.stroke();
		cb.restoreState();
	}
	
	private void drawHorizontalSpineLine(PdfContentByte cb, float y, float width, float lineWidth, boolean dashed)
	{
		cb.saveState();
		cb.setColorStroke(dashed ? kSpineLineColor : kSpreadSpineLineColor); 
		cb.setLineWidth(dashed ? lineWidth : lineWidth * 0.75f);
		if (dashed)
			cb.setLineDash(10,0);
		cb.moveTo(0, y);
		cb.lineTo(width, y);
		cb.stroke();
		cb.restoreState();
	}
	
	private void drawLowResPageNumber(PdfContentByte cb, float x, float y, String pageNumber) throws DocumentException, IOException
	{
		drawLowResPageNumber(cb, x, y, pageNumber, kPageNumCircleSize, 9f, BaseColor.BLACK, kPageNumCircleColor, 0f);
	}

	private void drawLowResPageNumber(PdfContentByte cb, float x, float y, String pageNum, float circleSize, float fontSize, 
			BaseColor color, BaseColor circleStrokeColor, float circleStrokeWidth) throws DocumentException, IOException
	{
		cb.saveState();

		cb.setColorStroke(circleStrokeColor);
		cb.setColorFill(kPageNumCircleColor);
		cb.setLineWidth(circleStrokeWidth);

		//draw circle
		cb.circle(x, y, circleSize);
		cb.fill();
		if (circleStrokeWidth>0)
		{
			cb.circle(x, y, circleSize);
			cb.stroke();
		}

		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA_BOLD, BaseFont.CP1252, BaseFont.EMBEDDED);
		cb.beginText();

		float textWidth = bf.getWidthPoint(pageNum, fontSize);
		while (textWidth > circleSize * 2f - 6f)
		{
			fontSize -= 1f;
			textWidth = bf.getWidthPoint(pageNum, fontSize);
		}
		float ascent = bf.getAscentPoint(pageNum, fontSize);

		cb.moveText(x - textWidth/2.0f, y - ascent/2f);
		cb.setFontAndSize(bf, fontSize);
		cb.setColorStroke(color); 
		cb.setColorFill(color);
		cb.showText(pageNum);
		cb.endText();

		cb.restoreState();
	}
	
	private Rectangle getBounds(Element element)
	{
		float x = Numbers.valueOf(element.getX());
		float y = Numbers.valueOf(element.getY());
		float width = Numbers.valueOf(element.getWidth());
		float height = Numbers.valueOf(element.getHeight());
		float rotation = Numbers.valueOf(element.getRotation());
		
		double angleRad = Math.toRadians(rotation);
		AffineTransform af = new AffineTransform();
		af.translate(x, y);
		af.rotate(angleRad);
		
		Point2D p1 = af.transform(new Point2D.Double(0, 0), null);
		Point2D p2 = af.transform(new Point2D.Double(width, 0), null);
		Point2D p3 = af.transform(new Point2D.Double(width, height), null);
		Point2D p4 = af.transform(new Point2D.Double(0, height), null);

		double left = Math.min(Math.min(p1.getX(), p2.getX()), Math.min(p3.getX(), p4.getX()));
		double top = Math.min(Math.min(p1.getY(), p2.getY()), Math.min(p3.getY(), p4.getY()));
		double right = Math.max(Math.max(p1.getX(), p2.getX()), Math.max(p3.getX() ,p4.getX()));
		double bottom = Math.max(Math.max(p1.getY(), p2.getY()), Math.max(p3.getY(), p4.getY()));
		
		Rectangle bounds = new Rectangle((float)left, (float)top, (float)right, (float)bottom);
		bounds.normalize();
		
		return bounds;
	}
	
}
