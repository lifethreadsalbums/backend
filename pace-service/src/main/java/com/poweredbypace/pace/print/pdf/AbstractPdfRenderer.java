package com.poweredbypace.pace.print.pdf;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfSpotColor;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.Element;
import com.poweredbypace.pace.domain.layout.ImageElement;
import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.domain.layout.SpineTextElement;
import com.poweredbypace.pace.domain.layout.TextElement;
import com.poweredbypace.pace.exception.ImageProcessingException;
import com.poweredbypace.pace.print.ColorConverter;
import com.poweredbypace.pace.service.IccProfileService;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.tlfrenderer.FabricToTlfConverter;
import com.poweredbypace.pace.tlfrenderer.JpegTextFlowRenderer;
import com.poweredbypace.pace.tlfrenderer.PdfTextFlowRenderer;
import com.poweredbypace.pace.util.Numbers;
import com.poweredbypace.pace.util.PaceFileUtils;


public class AbstractPdfRenderer {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	public static final float LF_HIDDEN_AREA = 0.3937f * 72f;
	public static final double CM_TO_INCH = 0.393700787402f;
	public static final double PPI = 72f;
	public static final double POINTS_PER_CM = 28.3464f;
	public static final double DEFAULT_HINGE_GAP = 1.1; //1.1cm
	
	static final float TEXT_PAD = 72f * 0.25f;
	static final long TRANSPARENT_COLOR = 0xffffffffL; 
	static final PdfSpotColor kSeparationAll = new PdfSpotColor("All", new GrayColor(1.0f));
	static final float kMinVisibleWidth = 20f;
	
	protected boolean cropImages = false;
	protected boolean useTiffImages = false;
	
	@Autowired
	protected IccProfileService iccProfileService;
	
	@Autowired
	protected StorageService storageService;
	
	@Autowired
	protected LayoutService layoutService;
	
	@Autowired
	protected ImageService imageService;
	
	@Autowired
	protected PdfTextFlowRenderer tlfRenderer;
	
	@Autowired
	protected FabricToTlfConverter fabricToTlfConverter;
	
	@Autowired
	protected ColorConverter colorConverter;
	
	@Autowired
	protected JpegTextFlowRenderer jpegTlfRenderer;
	
	
	public boolean isCropImages() {
		return cropImages;
	}

	public void setCropImages(boolean cropImages) {
		this.cropImages = cropImages;
	}

	public boolean isUseTiffImages() {
		return useTiffImages;
	}

	public void setUseTiffImages(boolean useTiffImages) {
		this.useTiffImages = useTiffImages;
	}


	public void renderElement(Element element, float x, float y, float offsetLeft, float offsetBottom, 
			PdfWriter writer, PdfTemplate pageTemplate, LayoutSize bookTemplate, Image img,
			IccProfile profile) throws DocumentException, IOException
	{
		if (element.getWidth()==null || element.getHeight()==null)
			return;
		
		ImageElement imageElement = element instanceof ImageElement ?
				(ImageElement) element : null;
				
		TextElement textElement = element instanceof TextElement ?
				(TextElement) element : null;
				
		boolean isTextBox = textElement!=null;
		float stroke = Numbers.valueOf(element.getStrokeWidth());
		
		boolean isBackgroundTransparent = false;
				
		//do not render empty frame
		if ( (imageElement!=null && imageElement.getImageFile()==null && stroke==0.0f && isBackgroundTransparent) ||
			 (textElement!=null && stroke==0.0f && StringUtils.isEmpty(textElement.getText())) )
			return;
		
		boolean convertRGBtoCMYK = profile!=null && ColorSpace.Cmyk==profile.getColorSpace();
		
		float width = Numbers.valueOf(element.getWidth()) - (isTextBox ? 0 : stroke * 2f);
		float height = Numbers.valueOf(element.getHeight()) - (isTextBox ? 0 : stroke * 2f);
		float opacity = element.getOpacity()!=null ? element.getOpacity().floatValue() : 1f;
		
		boolean flipX = imageElement!=null && BooleanUtils.isTrue(imageElement.getFlipX());
		boolean flipY = imageElement!=null && BooleanUtils.isTrue(imageElement.getFlipY());
		
		float textPad = TEXT_PAD;
		
		if (element instanceof SpineTextElement) {
			textPad = 0f;
		}
		
		if (stroke>0.0f && !isTextBox) {
			double angleRad = Math.toRadians( Numbers.valueOf(element.getRotation()) );
			AffineTransform af = new AffineTransform();
			af.rotate(angleRad);
			Point2D.Float dest = new Point2D.Float();
			af.transform(new Point2D.Float(stroke, stroke), dest);
			
			x += dest.x;
			y += dest.y;
		}
		
		if (img!=null) {
			double angleRad = Math.toRadians( Numbers.valueOf(element.getRotation()) );
			AffineTransform af = new AffineTransform();
			af.translate(x, y);
			af.rotate(angleRad);
			Point2D bottomLeft = af.transform(new Point2D.Double(0, height), null);

			AffineTransform trans = new AffineTransform();
			trans.translate((float)bottomLeft.getX() + offsetLeft, 
					bookTemplate.getHeight().floatValue() - (float)bottomLeft.getY() + offsetBottom);
			trans.rotate(-angleRad);
			
			//calculate frame bounds
			Point2D.Float p1 = new Point2D.Float(), 
					p2 = new Point2D.Float(), 
					p3 = new Point2D.Float(), 
					p4 = new Point2D.Float();
			trans.transform(new Point2D.Float(0, 0), p1);
			trans.transform(new Point2D.Float(width, 0), p2);
			trans.transform(new Point2D.Float(width, height), p3);
			trans.transform(new Point2D.Float(0, height), p4);
			
			float left = Math.min(Math.min(p1.x, p2.x), Math.min(p3.x, p4.x));
			float right = Math.max(Math.max(p1.x, p2.x), Math.max(p3.x ,p4.x));
			
			float x1 = Math.max(0, left);
			float x2 = Math.min(pageTemplate.getBoundingBox().getWidth(), right);
			float visibleBoxWidth = x2 - x1;
			if (!(visibleBoxWidth>kMinVisibleWidth || width<=kMinVisibleWidth)) {
				logger.debug("Skipping frame "+imageElement.getImageFile().getFilename());
				return;
			}
		}
		if (isTextBox) {
			//add padding to fix issues with some script fonts being cut off
			width += textPad * 2f;
			height += textPad * 2f;
			
			double angleRad = Math.toRadians( Numbers.valueOf(element.getRotation()) );
			AffineTransform af = new AffineTransform();
			af.rotate(angleRad);
			Point2D.Float dest = new Point2D.Float();
			af.transform(new Point2D.Float(textPad, textPad), dest);
			
			x -= dest.x;
			y -= dest.y;
		}
		
		//create a frame to hold text or image 
		PdfTemplate frame = writer.getDirectContent().createTemplate(width, height);

		PdfGState gs = new PdfGState();
		gs.setFillOpacity(opacity);
		gs.setStrokeOpacity(opacity);
		frame.setGState(gs);

		if (isTextBox) {
			//render text
			String tlfXml = fabricToTlfConverter.convert(textElement);
			logger.debug("Rendering text " + tlfXml);
			tlfRenderer.render(tlfXml, frame, writer, 
				new Rectangle(0, 0, width, height), textPad, convertRGBtoCMYK, 1.0f, false);
			
			if (element instanceof SpineTextElement) {
				//center spine box
				//measure height
				Rectangle2D.Float textHeight = jpegTlfRenderer.measureTextHeight(tlfXml, new Rectangle2D.Float(0f, 0f, width, height), false);
				float midRect = height / 2f;
				float midText = textHeight.y + (textHeight.height/2f);
				float offset = midRect - midText;
				
				if (Numbers.valueOf(element.getRotation())==0f) {
					y += offset;
				} else {
					x -= offset;
				}
			}

		} else if (imageElement!=null && imageElement.getImageFile()!=null) {
			
			//render image
			float imageWidth = Numbers.valueOf(imageElement.getImageWidth());
			float imageHeight = Numbers.valueOf(imageElement.getImageHeight());
			float imageX = Numbers.valueOf(imageElement.getImageX());
			float imageY = Numbers.valueOf(imageElement.getImageY());
			
			if (cropImages) {
				imageX = 0f;
				imageY = 0f;
				imageWidth = Numbers.valueOf(imageElement.getWidth());
				imageHeight = Numbers.valueOf(imageElement.getHeight());
			}
			
			double imageAngleRad = Math.toRadians(Numbers.valueOf(imageElement.getImageRotation()));
			
			if (img==null) {
				//render image placeholder
				img = Image.getInstance( getClass().getResource("/default-placeholder.png") );
				imageAngleRad = 0;
				
				float fileWidth = 1500;
				float scale1 = width / fileWidth;
				float scale2 = height / fileWidth;
				float scale = Math.max(scale1, scale2);

	            imageWidth = fileWidth * scale;
	            imageHeight = fileWidth * scale;
	            imageX = width/2f - imageWidth/2f;
	            imageY = height/2f - imageHeight/2f;
			}

			img.scaleAbsolute(imageWidth, imageHeight);

			AffineTransform af = new AffineTransform();
			af.translate(imageX, imageY);
			af.rotate(imageAngleRad);
			Point2D imagePos = af.transform(new Point2D.Double(0, imageHeight), null);

			AffineTransform imageTrans = new AffineTransform();
			imageTrans.translate((float)imagePos.getX(), height - (float)imagePos.getY());
			imageTrans.rotate(-imageAngleRad);
			img.setAbsolutePosition(0, 0);

			PdfTemplate imgWrapper = writer.getDirectContent().createTemplate(imageWidth, imageHeight);
			imgWrapper.addImage(img);
			frame.addTemplate(imgWrapper, imageTrans);
			
			writer.releaseTemplate(imgWrapper);
		} else if (!isBackgroundTransparent && element.getBackgroundColor()!=null) {
			//render background
			frame.saveState();
			int color = Integer.parseInt(element.getBackgroundColor().replaceAll("#", ""), 16);
			BaseColor fillColor = new BaseColor(0xff << 24 | color);
			if (convertRGBtoCMYK)
				fillColor = colorConverter.toCMYKColor(fillColor);
			frame.setColorFill(fillColor);
			frame.rectangle(0,0, width, height);
			frame.fill();
			frame.restoreState();
		}
		
		//rotate box
		//in Flash an element is rotated around top left corner, while in iText around bottom left corner
		//we need to find a coordinates of the bottom left corner and rotate the box around that point
		double angleRad = Math.toRadians( Numbers.valueOf(element.getRotation()) );
		AffineTransform af = new AffineTransform();
		af.translate(x, y);
		af.rotate(angleRad);
		Point2D bottomLeft = af.transform(new Point2D.Double(0, height), null);

		AffineTransform trans = new AffineTransform();
		if (flipX) {
			AffineTransform tx = new AffineTransform();
			tx.scale(-1, 1);
			tx.translate(-width, 0);
			trans.concatenate(tx);
		}
		if (flipY) {
			AffineTransform tx = new AffineTransform();
			tx.scale(1, -1);
			tx.translate(0, -height);
			trans.concatenate(tx);
		}
		trans.translate((float)bottomLeft.getX() + offsetLeft, 
				bookTemplate.getHeight().floatValue() - (float)bottomLeft.getY() + offsetBottom);
		trans.rotate(-angleRad);
		
		//calculate frame bounds
		Point2D.Float p1 = new Point2D.Float(), 
				p2 = new Point2D.Float(), 
				p3 = new Point2D.Float(), 
				p4 = new Point2D.Float();
		trans.transform(new Point2D.Float(0, 0), p1);
		trans.transform(new Point2D.Float(width, 0), p2);
		trans.transform(new Point2D.Float(width, height), p3);
		trans.transform(new Point2D.Float(0, height), p4);
		
		float left = Math.min(Math.min(p1.x, p2.x), Math.min(p3.x, p4.x));
		float right = Math.max(Math.max(p1.x, p2.x), Math.max(p3.x ,p4.x));
		
		float x1 = Math.max(0, left);
		float x2 = Math.min(pageTemplate.getBoundingBox().getWidth(), right);
		float visibleBoxWidth = x2 - x1;
		if (visibleBoxWidth>kMinVisibleWidth || width<=kMinVisibleWidth)
		{
			pageTemplate.addTemplate(frame, trans);
			
			//draw stroke;
			if (stroke > 0.0f && element.getStrokeColor()!=null)
			{
				pageTemplate.saveState();
				pageTemplate.concatCTM(trans);
				
				int strokeColorInt = Integer.parseInt(element.getStrokeColor().replaceAll("#", ""), 16);
				BaseColor strokeColor = new BaseColor(0xff << 24 | strokeColorInt);
				if (convertRGBtoCMYK)
					strokeColor = colorConverter.toCMYKColor(strokeColor);
					
				double strokeOpacity = 100d;
				//placedElement.getStrokeOpacity()!=null ?
				//		placedElement.getStrokeOpacity().doubleValue() : 0d;
				PdfGState gs2 = new PdfGState();
				gs2.setStrokeOpacity((float)(strokeOpacity/100.0));
				pageTemplate.setGState(gs2);
				
				pageTemplate.setColorStroke(strokeColor);
				pageTemplate.setLineWidth(stroke);
				
				if (isTextBox) {
					pageTemplate.rectangle(textPad, textPad, width - (textPad * 2f), height - (textPad * 2f));
				} else {
					pageTemplate.rectangle(-stroke/2.0f, -stroke/2.0f, width + stroke, height + stroke);
				}
				pageTemplate.stroke();
				pageTemplate.restoreState();
			}
		}

		writer.releaseTemplate(frame);
	}
	
	
	private File cropImage(File file, ImageElement element) throws ImageProcessingException {
		
		float scaleX = element.getImageFile().getWidth().floatValue() / element.getImageWidth().floatValue();
		float scaleY = element.getImageFile().getHeight().floatValue() / element.getImageHeight().floatValue();
		int x = Math.round(-element.getImageX().floatValue() * scaleX);
		int y = Math.round(-element.getImageY().floatValue() * scaleY);
		int w = Math.round(element.getWidth().floatValue() * scaleX);
		int h = Math.round(element.getHeight().floatValue() * scaleY);
		File croppedFile = null;
		int dpi = 72 * w/element.getWidth().intValue();
		
		if (dpi>300) {
			logger.debug("Cropping and downsampling image " + element.getImageFile().getFilename() +", dpi=" + dpi);
			int w2 = Math.round(300.0f/72.0f * element.getWidth().floatValue());
			int h2 = Math.round(300.0f/72.0f * element.getHeight().floatValue());
			croppedFile = imageService.cropAndResize(file, w, h, x, y, w2, h2, useTiffImages);
		} else {
			logger.debug("Cropping image " + element.getImageFile().getFilename() +", dpi=" + dpi);
			croppedFile = imageService.cropAndResize(file, w, h, x, y, w, h, useTiffImages);
		}
		
		return croppedFile;
	}	
	
	
	protected Image getHiResImage(ImageElement el, IccProfile profile, Map<String, Image> imageCache) throws IOException, DocumentException 
	{
		ImageFile img = el.getImageFile();
		String imageKey = el.getId()!=null ? el.getId().toString() : el.getImageFile().getId().toString();
		
		if (imageCache.containsKey(imageKey)) {
			logger.debug("Image "+img.getFilename() + " from cache.");
			return imageCache.get(imageKey);
		}
		
		if (cropImages) {
			File rgbFile = storageService.getFile(img.getOriginalImageUrl());
			
			//crop image
			File croppedFile = cropImage(rgbFile, el);
				
			//convert to CMYK
			File cmykFile = iccProfileService.convert(croppedFile, profile);
			Image image = Image.getInstance(cmykFile.getAbsolutePath());
			croppedFile.delete();
			rgbFile.delete();
			
			imageCache.put(imageKey, image);
			return image;
		}
		
		File cmykFile = iccProfileService.getImage(img, profile);
		
		if (el.getFilter()!=null) {
			File filteredFile = null;
			if (ImageElement.SEPIA_FILTER.equals(el.getFilter())) {
				filteredFile = imageService.sepia(cmykFile);
			} else if (ImageElement.BW_FILTER.equals(el.getFilter())) {
				filteredFile = imageService.blackAndWhite(cmykFile);
			}
			cmykFile.delete();
			cmykFile = filteredFile;
		}
		
		String filename = FilenameUtils.getName(img.getUrl());
		if (FilenameUtils.isExtension(filename, "png"))
		{
			File maskFile = storageService
				.getFile("images/" + profile.getCode() + "/" +
					PaceFileUtils.appendStringBeforeExtension(filename, "-mask"));
			
			Image cmykImage = Image.getInstance(cmykFile.getAbsolutePath());
			
			Image mask = Image.getInstance(maskFile.getAbsolutePath());	
			mask.makeMask();	
			mask.setInverted(true);
			
			cmykImage.setImageMask(mask);
			imageCache.put(imageKey, cmykImage);
			return cmykImage;
		} else {
		
			Image cmykImage = null;
			try {
				cmykImage = Image.getInstance(cmykFile.getAbsolutePath());
			} catch (IOException ex) {
				logger.warn("Cannot instantiate image downloaded from "+img.getUrl()+
						". Trying to regenerate the CMYK file", ex);
				cmykFile = iccProfileService.getImage(img, profile);
				cmykImage = Image.getInstance(cmykFile.getAbsolutePath());
			}
		
			imageCache.put(imageKey, cmykImage);
			return cmykImage;
		}
		
	}

	protected void AddMetadata(Document document, Product p) {
		document.addAuthor( ApplicationConstants.PACE );
		document.addCreationDate();
		document.addCreator( ApplicationConstants.PACE );
		String productName = p.getName();
		if (productName!=null) {
			document.addTitle(productName);
			document.addSubject(productName);
		}
	}
	
	protected void clearImageCache(Map<String,Image> images)
	{
		for(Image image:images.values())
		{
			URL url = image.getUrl();
			File file = new File(url.getFile());
			file.delete();
		}
		images.clear();
	}
	
	
}
