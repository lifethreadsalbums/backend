package com.poweredbypace.pace.binderyform.impl;

import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.binderyform.BinderyFormRenderer;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductPageType;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.PageRangeValue;
import com.poweredbypace.pace.domain.layout.PageRangeValue.PageRangeValueCollection;
import com.poweredbypace.pace.domain.layout.Spread;
import com.poweredbypace.pace.exception.ImageProcessingException;
import com.poweredbypace.pace.irisbook.IrisConstants.BoxStyle;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.print.pdf.HiResPdfRenderer;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.util.Numbers;

@Component
@Qualifier("thumbRenderer")
public class BfThumbRenderer implements BinderyFormRenderer {
	
	protected final Log logger = LogFactory.getLog(getClass());

	private static final float PAGE_THUMB_SIZE = 150.0f;
	private static final float MAX_THUMB_SIZE = 1.8f*72;
	
	@Autowired
	private HiResPdfRenderer pdfRenderer;

	@Autowired
	private StorageService storageService;
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	private List<File> filesToBeDeleted = new ArrayList<File>();
	
	@Override
	public void render(Document document, PdfWriter writer, Product product,
			LayoutSize bookTemplate, LayoutSize coverTemplate,
			Spread firstPage, Spread lastPage, Spread coverPage,
			JobProgressInfo job, int minProgress, int maxProgress) throws IOException, DocumentException,
			ImageProcessingException {
		
		document.add(getThumbTable(writer, product,  bookTemplate,
					 coverTemplate, firstPage, lastPage, coverPage,   
					 job, minProgress, maxProgress));
	}
	
	protected PdfPTable getThumbTable(PdfWriter writer, Product product,
			LayoutSize bookTemplate, LayoutSize coverTemplate,
			Spread firstPage, Spread lastPage, Spread coverPage,
			JobProgressInfo job, int minProgress, int maxProgress) throws DocumentException, IOException, ImageProcessingException
	{
		IrisProduct irisProduct = new IrisProduct(product);
		PdfPTable table = new PdfPTable(2);
		table.setLockedWidth(true);
		table.setTotalWidth(new float[] {2.9f * 72f, 2.56f * 72f});
		int alignment = com.itextpdf.text.Element.ALIGN_CENTER;
		
		if (irisProduct.hasBox() && !irisProduct.getBoxTypeCode().equals(BoxStyle.SLIP_CASE))
			alignment = com.itextpdf.text.Element.ALIGN_LEFT;
		 
		table.setHorizontalAlignment(alignment);
		for(PdfPCell cell:getThumbCells(writer, product, bookTemplate,
				 coverTemplate, firstPage, lastPage, coverPage,   
				 job, minProgress, maxProgress))
		{
			cell.setBorder(0);
			table.addCell(cell);
		}
		
		if (coverTemplate==null)
		{
			float spacing = 0.25f *72f;
			table.setSpacingBefore(spacing);
			table.setSpacingAfter(spacing);
		}
		
		for(File file:filesToBeDeleted) {
			file.delete();
		}
		
		return table;
	}
	
	protected List<PdfPCell> getThumbCells(PdfWriter writer, Product product,
			LayoutSize bookTemplate, LayoutSize coverTemplate,
			Spread firstPage, Spread lastPage, Spread coverPage,
			JobProgressInfo job, int minProgress, int maxProgress) throws IOException, DocumentException, ImageProcessingException
	{
		List<PdfPCell> cells = new ArrayList<PdfPCell>();
		
		job.setProgressPercent(minProgress);
		int progressStep = (maxProgress-minProgress)/5;
		
		float pageWidth = 0f;
		
		if (bookTemplate!=null) {
			Image firstImage = null;
			Image lastImage = null;
			
			boolean isLf = BooleanUtils.isTrue(firstPage.getLayout().getIsLayFlat());
			LayoutSize bt = bookTemplate;
			String first = "FIRST PAGE";
			String last = "LAST PAGE";
			float templateWidth = bt.getWidth();
			if (isLf)
				templateWidth -= ApplicationConstants.LF_HIDDEN_AREA;
			
			float offset = -templateWidth * 0.5f;
			
			if (ProductPageType.SpreadBased==product.getPrototypeProduct().getProductPageType())
			{
				bt = new LayoutSize(bookTemplate);
				bt.setWidth( bt.getWidth() * 2.0f );
				first = "FIRST SPREAD";
				last = "LAST SPREAD";
				offset = 0;
			}
			
			firstImage = renderPageThumbnail(writer, firstPage, bt, 
					isLf, offset, PAGE_THUMB_SIZE, first, false);
			lastImage = renderPageThumbnail(writer, lastPage, bt, 
					isLf, offset, PAGE_THUMB_SIZE, last, false);
			
			PdfPCell cell = new PdfPCell(firstImage);
			cell.setBorder(0);
			cell.setFixedHeight(2.1f * 72);
			cells.add(cell);
			incrementProgress(job, progressStep);
			
			cell = new PdfPCell(lastImage);
			cell.setBorder(0);
			cell.setFixedHeight(2.1f * 72);
			cells.add(cell);
			incrementProgress(job, progressStep);
			
			pageWidth = firstImage.getWidth();
		}
		
		LayoutSize ct = (coverTemplate!=null) ? new LayoutSize(coverTemplate) : null;
		if (ct!=null) {
			if (BooleanUtils.isTrue(ct.getDynamicSpineWidth())) {
				
				List<PageRangeValue> values = ruleService.getRuleCollectionValue(product, 
						GenericRule.SPINE_WIDTH, PageRangeValue.class);
				PageRangeValueCollection valCol = new PageRangeValueCollection(values);
				float spineWidthInCm = valCol.getValue(product.getPageCount());
				float spineWidth = spineWidthInCm * ApplicationConstants.POINTS_PER_CM;
				
				ct.setWidth(ct.getWidth()*2.0f + spineWidth);
				float thumbWidth = 2.9f * 72f + pageWidth;
				PdfPCell cell = getThumbCell(writer, coverPage, ct, false, 0, thumbWidth, "COVER", true);
				cell.setColspan(2);
				cells.add(cell);
				incrementProgress(job, progressStep*2);
			} else {
				PdfPCell frontPageCell = null;
				PdfPCell backPageCell = null;
				frontPageCell = getThumbCell(writer, coverPage, ct,
						false, -(ct.getWidth()+ct.getGapBetweenPages()),
						PAGE_THUMB_SIZE, "FRONT COVER", false);
				incrementProgress(job, progressStep);
				backPageCell = getThumbCell(writer, coverPage, ct, false, "BACK COVER", false);
				
				cells.add(frontPageCell);
				cells.add(backPageCell);
				incrementProgress(job, progressStep);
			} 
			
		} else {
			incrementProgress(job, progressStep*2);
		}
		
		
		return cells;
	}
	
	private PdfPCell getThumbCell(PdfWriter writer, Spread page, LayoutSize template,
			boolean lf, String label, boolean crop) throws IOException, DocumentException
	{
		return getThumbCell(writer, page, template, lf, 0, PAGE_THUMB_SIZE, label, crop);
	}
	
	private PdfPCell getThumbCell(PdfWriter writer, Spread page, LayoutSize template,
			boolean lf, float offsetX, float thumbSize, String label, boolean crop) 
					throws IOException, DocumentException
	{
		PdfPCell cell = new PdfPCell(
				renderPageThumbnail(writer, page, template, lf, offsetX, thumbSize, label, crop));
		cell.setBorder(0);
		cell.setFixedHeight(2.1f * 72);
			
		return cell;
	}
	
	
	private Image renderPageThumbnail(PdfWriter writer, Spread page, LayoutSize template, 
			boolean lf, float offsetX, float thumbSize, String label, boolean fitToFrame) throws IOException, DocumentException
	{
		float templateWidth = template.getWidth().floatValue();
		float templateHeight = template.getHeight().floatValue();
		if (lf)
			templateWidth -= ApplicationConstants.LF_HIDDEN_AREA;
		
		float scale = thumbSize / templateWidth;
		float h = scale * templateHeight;		
		float w = scale * templateWidth;
		
		float imageH = h;
		float imageW = w;
		
		float thumbH = h;
		
		if (h > MAX_THUMB_SIZE)
		{
			if (fitToFrame)
			{
				thumbH = MAX_THUMB_SIZE;
				scale = MAX_THUMB_SIZE / templateHeight;
				imageW = scale * templateWidth;
				imageH = scale * templateHeight;
			} else {
				scale = MAX_THUMB_SIZE / templateHeight;
				thumbH = h = scale * templateHeight;
				w = scale * templateWidth;
				imageH = h;
				imageW = w;
				
				w = Math.max(85f, w);
			}
		}
		
		
		PdfTemplate thumbTemplate = writer.getDirectContent().createTemplate(w, thumbH + 20f);
		
		PdfTemplate pageTemplate = writer.getDirectContent().createTemplate(
				templateWidth, templateHeight);
		boolean isBlank = true;
		if (page!=null)
		{
			for (Element placedElement : page.getElements()) 
			{
				isBlank = false;
				float x = Numbers.valueOf(placedElement.getX()) + offsetX;
				float y = Numbers.valueOf(placedElement.getY());
	
				String url = null;
				try {
					Image image = null;
					if (placedElement.getClass().isAssignableFrom(ImageElement.class)) {
						url = ((ImageElement)placedElement).getImageFile().getUrl();
						File imgFile = storageService.getFile( ApplicationConstants.LOW_RES_IMAGE_PATH + url);
						filesToBeDeleted.add(imgFile);
						image = Image.getInstance(imgFile.getAbsolutePath());
					}
					pdfRenderer.renderElement(placedElement, x, y, 0, 0, 
							writer, pageTemplate, template, image, null);
				} catch(Exception ex) { 
					logger.error("Cannot download image, url="+url, ex);
				}
			}
		}
		Image img = Image.getInstance(pageTemplate);
		img.setAbsolutePosition(0,0);
		//img.scaleAbsolute(w, h);
		img.scaleToFit(imageW, imageH);
		img.setAbsolutePosition(w/2f - imageW/2f, thumbH/2f - imageH/2f);
		PdfTemplate imgWrapper = writer.getDirectContent().createTemplate(w, thumbH);
		imgWrapper.addImage(img);
		BinderyFormHelper.drawThumbBorder(imgWrapper, img.getAbsoluteX(), img.getAbsoluteY(),  imageW, imageH);

		AffineTransform t = new AffineTransform();
		thumbTemplate.addTemplate(imgWrapper, t);
		writer.releaseTemplate(imgWrapper);
		writer.releaseTemplate(pageTemplate);
		
		BinderyFormHelper.drawThumbLabels(thumbTemplate, w, thumbH, label, isBlank);
		Image res = Image.getInstance(thumbTemplate);
		writer.releaseTemplate(thumbTemplate);
		return res;
	}
	
	/*
	private PdfPCell getThumbCell(PdfWriter writer, File page, LayoutSize template,
			boolean lf, float thumbSize, String label, boolean fitToFrame) 
					throws IOException, DocumentException, ImageProcessingException
	{
		PdfPCell cell = new PdfPCell(
				renderPageThumbnail(writer, page, template, lf, thumbSize, label, fitToFrame));
		cell.setBorder(0);
		cell.setFixedHeight(2.1f * 72);
			
		return cell;
	}
	*/
	
	/*
	private Image renderPageThumbnail(PdfWriter writer, File image, 
			LayoutSize template, boolean lf, 
			float thumbSize, String label, boolean crop) throws IOException, DocumentException, 
			ImageProcessingException
	{
		float templateWidth = template.getWidth().floatValue();
		float templateHeight = template.getHeight().floatValue();
		if (lf)
			templateWidth -= IrisConstants.LF_HIDDEN_AREA;
		
		float scale = thumbSize / templateWidth;
		float h = scale * templateHeight;		
		float w = scale * templateWidth;
		
		float thumbH = h;
		
		if (h > MAX_THUMB_SIZE)
		{
			if (crop)
				thumbH = MAX_THUMB_SIZE;
			else {
				scale = MAX_THUMB_SIZE / templateHeight;
				thumbH = h = scale * templateHeight;
				w = scale * templateWidth;
			}
		}

		PdfTemplate thumbTemplate = writer.getDirectContent().createTemplate(w, thumbH + 20f);
		Image img = Image.getInstance(image.getAbsolutePath());
		img.setAbsolutePosition(0,0);
		img.scaleAbsolute(w, h);
		PdfTemplate imgWrapper = writer.getDirectContent().createTemplate(w, thumbH);
		imgWrapper.addImage(img);

		AffineTransform t = new AffineTransform();
		thumbTemplate.addTemplate(imgWrapper, t);
		writer.releaseTemplate(imgWrapper);
		
		BinderyFormHelper.drawThumbBorderAndLabels(thumbTemplate, w, thumbH, label, 
				imageService.isImageBlank(image));

		Image res =  Image.getInstance(thumbTemplate);
		writer.releaseTemplate(thumbTemplate);
		return res;
	}
	*/
	
	/*
	private List<File> getFirstAndLastPage(String pdfUrl) throws IOException, ImageProcessingException 
	{
		String pdfFilename = FilenameUtils.getBaseName(pdfUrl);
		String firstPageURL = pdfFilename+"-first-page.png";
		String lastPageURL = pdfFilename+"-last-page.png";
		
		File firstPage = null;
		File lastPage = null;
		
		try {
			
			firstPage = storageProvider.getFileAsTempFile("userspdf/"+firstPageURL);
			lastPage = storageProvider.getFileAsTempFile("userspdf/"+lastPageURL);
			
		} catch (Exception ex) {
			
			logger.debug(String.format("Downloading PDF from %s", pdfUrl));
			File pdfFile = storageProvider.getFileAsTempFile(pdfUrl);
			
			firstPage = File.createTempFile("irisbook-image-", ".png");
			lastPage = File.createTempFile("irisbook-image-", ".png");
			
			imageProcessor.extractFirstAndLastPageFromPDF(pdfFile, firstPage, lastPage);
			storageProvider.putFile(firstPage, S3Utils.urlDecode("userspdf/"+firstPageURL));
			storageProvider.putFile(lastPage, S3Utils.urlDecode("userspdf/"+lastPageURL));
			
			pdfFile.delete();
		}
		
		List<File> result = new ArrayList<File>();
		result.add(firstPage);
		result.add(lastPage);
		
		return result;
	}
	*/
	
	private void incrementProgress(JobProgressInfo job, Integer inc)
	{
		job.setProgressPercent(job.getProgressPercent() + inc);
		notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, job));
	}

}
