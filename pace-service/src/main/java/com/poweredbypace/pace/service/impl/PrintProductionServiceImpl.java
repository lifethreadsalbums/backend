package com.poweredbypace.pace.service.impl;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.Attachment;
import com.poweredbypace.pace.domain.Attachment.AttachmentType;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.layout.ImageStampElement;
import com.poweredbypace.pace.domain.layout.TextStampElement;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.event.AlbumJpegGeneratedEvent;
import com.poweredbypace.pace.event.AlbumPdfGeneratedEvent;
import com.poweredbypace.pace.event.AlbumPreviewPdfGeneratedEvent;
import com.poweredbypace.pace.event.AlbumTiffGeneratedEvent;
import com.poweredbypace.pace.event.ApplicationEvent;
import com.poweredbypace.pace.event.CameoGeneratedEvent;
import com.poweredbypace.pace.event.CoverJpegGeneratedEvent;
import com.poweredbypace.pace.event.CoverPdfGeneratedEvent;
import com.poweredbypace.pace.event.CoverTiffGeneratedEvent;
import com.poweredbypace.pace.event.DieGeneratedEvent;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.print.LayoutPrintGenerator;
import com.poweredbypace.pace.print.OutputType;
import com.poweredbypace.pace.repository.AttachmentRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.service.PrintProductionService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.ScreenshotService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.util.PaceFileUtils;
import com.poweredbypace.pace.util.HibernateUtil;
import com.poweredbypace.pace.util.URLUtils;
import com.poweredbypace.pace.util.UrlUtil;

@Service
public class PrintProductionServiceImpl implements PrintProductionService {
	
	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	public NotificationBroadcaster notificationBroadcaster;
	
	@Autowired(required=false)
	@Qualifier("pdfGenerator")
	public LayoutPrintGenerator pdfGenerator;
	
	@Autowired(required=false)
	@Qualifier("jpegGenerator")
	public LayoutPrintGenerator jpegGenerator;
	
	@Autowired(required=false)
	@Qualifier("tiffGenerator")
	public LayoutPrintGenerator tiffGenerator;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private AttachmentRepository attachmentRepo;
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private Env env;
	
	@Autowired(required=false)
	private ScreenshotService screenshotService;
	
	@Autowired
	private ProductService productService;
	
	
	public PrintProductionServiceImpl() { }
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void generateAlbum(long productId, OutputType outputType, JobProgressInfo jobInfo) 
			throws InterruptedException, PrintGenerationException {
		
		Product p = productRepo.findOne(productId);
		this.generateAlbum(p, outputType, jobInfo);
		
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void generateAlbumPreview(long productId, OutputType outputType, JobProgressInfo jobInfo) 
			throws InterruptedException, PrintGenerationException {
		
		Product p = productRepo.findOne(productId);
		this.generateAlbumPreview(p, outputType, jobInfo);
		
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void generateCover(long productId, OutputType outputType, JobProgressInfo jobInfo) 
			throws InterruptedException, PrintGenerationException {
		
		Product p = productRepo.findOne(productId);
		this.generateCover(p, outputType, jobInfo);
		
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void generateCameos(long productId, OutputType outputType,
			JobProgressInfo jobInfo) throws InterruptedException,
			PrintGenerationException {
		
		Product p = productRepo.findOne(productId);
		this.generateCameos(p, outputType, jobInfo);
		
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void generateDies(long productId, OutputType outputType,
			JobProgressInfo jobInfo) throws PrintGenerationException {
		
		Product p = productRepo.findOne(productId);
		this.generateDies(p, outputType, jobInfo);
		
	}
	
	@Override
	public void generateDies(Product product, OutputType outputType, JobProgressInfo jobInfo) throws PrintGenerationException {
		
		String filename = null;
		String path = null;
		
		for(Product p:product.getProductAndChildren()) {
			for(ProductOption<?> po:product.getProductOptions()) {
				Object value = HibernateUtil.unproxy(po.getValue());
				if (value instanceof ImageStampElement ||
					value instanceof TextStampElement) {
					
					boolean isLogoOption = po.getPrototypeProductOption().getProductOptionType().getSystemAttribute()==SystemAttribute.CustomLogo; 
					AttachmentType attachmentType = AttachmentType.valueOf( 
						(isLogoOption ? "Logo" : "Die")+outputType.name());
					
					if (log.isDebugEnabled())
						log.debug("Generating DIE " + outputType + " for " + product.getName());
					
					jobInfo.setJobName("Generating " + outputType + " for " + p.getName());
					
					try {
						File dieFile = null;
						if (value instanceof ImageStampElement) {
							ImageStampElement el = (ImageStampElement) value;
							
							File originalFile = storageService.getFile(el.getImageFile().getOriginalImageUrl());
							
							//gm convert customdie.png -density 300x300 -units PixelsPerInch -resample 600x600 -resize 2400 out.png
							//TODO: DPI company specific
							int dpi = 600;
							int width = Math.round(el.getWidth() / ApplicationConstants.PPI * dpi);
							int height = Math.round(el.getHeight() / ApplicationConstants.PPI * dpi);
							
							String ext = outputType==OutputType.Bmp ? ".bmp" : ".png";
							
							dieFile = File.createTempFile("pace-image-", ext);
							imageService.resize(originalFile, dieFile, width, height, dpi);
							originalFile.delete();
							
							if (log.isDebugEnabled())
								log.debug("Saving " +outputType +" file for " + p.getName());
						} else {
							TextStampElement el = (TextStampElement) value;
							dieFile = jpegGenerator.generateDie(product, el, null);
						}
						//save die
						path = ApplicationConstants.DIE_PATH + outputType.name().toLowerCase() + "/";
						
						filename = getFilename(p, dieFile);
						
						if (isLogoOption) {
							String ext = FilenameUtils.getExtension(filename);
							filename = filename.replaceAll("."+ext, "-logo."+ext);
						}
						
						String contentDisposition = String.format("attachment; filename=\"%s\"", filename);
						jobInfo.setJobId(UUID.randomUUID().toString());
						storageService.putFile(dieFile, path + filename, contentDisposition, jobInfo);
						saveAttachment(p, path + filename, attachmentType, jobInfo);
						
						//make screenshot
						String section = po.getPrototypeProductOption().getEffectiveGroup().getUrl() +
							"/" + po.getPrototypeProductOption().getUrl();
						String url = "https://"+env.getStore().getDomainName() + "/#/build/" + 
							product.getId() + "/" + section + "?screenshot=true";
						File screenshot = screenshotService.screenshot(url);
						
						File outputDir = PaceFileUtils.createTempDir();
						File dieFile2 = new File(outputDir.getAbsolutePath()+"/"+filename);
						org.apache.commons.io.FileUtils.copyFile(dieFile, dieFile2);
						
						String screenshotFilename = FilenameUtils.getBaseName(filename) + "-preview.jpg";
						File screenshot2 = new File(outputDir.getAbsolutePath()+"/"+screenshotFilename);
						org.apache.commons.io.FileUtils.copyFile(screenshot, screenshot2);
						
						//zip it up
						File zip = PaceFileUtils.zip(dieFile2, screenshot2);
						
						//save zip
						filename = getFilename(p, zip);
						contentDisposition = String.format("attachment; filename=\"%s\"", filename);
						jobInfo.setJobId(UUID.randomUUID().toString());
						storageService.putFile(zip, path + filename, contentDisposition, jobInfo);
						saveAttachment(p, path + filename,
							isLogoOption ? AttachmentType.LogoZip : AttachmentType.DieZip, jobInfo);
						
						jobInfo.setProgressPercent(100);
						jobInfo.setIsCompleted(true);
						notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
						
						if (log.isDebugEnabled())
							log.debug(outputType +" for " + p.getName() + " stored at " + path + filename);
						
						dieFile.delete();
						dieFile2.delete();
						zip.delete();
						screenshot.delete();
						screenshot2.delete();
						outputDir.delete();
						
						
						eventService.sendEvent(new DieGeneratedEvent(p, po.getPrototypeProductOption().getEffectiveCode()));
					} catch(Exception ex ) {
						throw new PrintGenerationException("Cannot generate die file", ex);
					}
				}
			}
		}
	}

	@Override
	public void generateCameos(Product product, OutputType outputType,
			JobProgressInfo jobInfo) throws InterruptedException,
			PrintGenerationException {
		
		String filename = null;
		String path = null;
		
		AttachmentType attachmentType = AttachmentType.valueOf("Cameo"+outputType.name());
		for(Product p:product.getProductAndChildren()) {
			
			if (log.isDebugEnabled())
				log.debug("Generating " + outputType + " for " + p.getName());
			
			jobInfo.setJobName("Generating " + outputType + " for " + p.getName());
			
			LayoutPrintGenerator gen = getGenerator(outputType);
			File file = gen.generateCameos(p, getProgressListener(jobInfo, 0, 90));
			
			if (log.isDebugEnabled())
				log.debug("Saving " +outputType +" file for " + p.getName());
			
			path = ApplicationConstants.CAMEO_PATH + outputType.name().toLowerCase() + "/";
			//filename = getFilename(p, file);
			String ext = FilenameUtils.getExtension(file.getName());
			filename = p.getProductNumber() + "_Cameo_jpg" + ext;
			
			String contentDisposition = String.format("attachment; filename=\"%s\"", filename);
			
			jobInfo.setJobId(UUID.randomUUID().toString());
			storageService.putFile(file, path + filename, contentDisposition, jobInfo);
			
			jobInfo.setProgressPercent(100);
			jobInfo.setIsCompleted(true);
			notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
			
			if (log.isDebugEnabled())
				log.debug(outputType +" for " + p.getName() + " stored at " + path + filename);
			
			file.delete();
			saveAttachment(p, path + filename, attachmentType, jobInfo);
			
			eventService.sendEvent(new CameoGeneratedEvent(p));
		}
		
	}

	@Override
	public void generateAlbum(Product product, OutputType outputType, final JobProgressInfo jobInfo) 
			throws InterruptedException, PrintGenerationException {
		
		String filename = null;
		String path = null;
		
		AttachmentType attachmentType = AttachmentType.valueOf("HiRes"+outputType.name());
		saveAttachment(product, null, attachmentType, jobInfo);
		
		try {
			Product p = product;
				
			if (log.isDebugEnabled())
				log.debug("Generating " + outputType + " for " + p.getName());
			
			jobInfo.setJobName("Generating " + outputType + " for " + p.getName());
			
			LayoutPrintGenerator gen = getGenerator(outputType);
			File file = gen.generateAlbum(p, getProgressListener(jobInfo, 0, 50));
			
			if (log.isDebugEnabled())
				log.debug("Saving " +outputType +" file for " + p.getName());
			
			path = ApplicationConstants.ALBUM_PATH + outputType.name().toLowerCase() + "/";
			filename = getFilename(p, file);
			
			String ext = FilenameUtils.getExtension(file.getName());
			String s3Filename = UrlUtil.slug(String.format("%d-%s_%s.%s",
				product.getId(),
				product.getProductNumber()!=null ? product.getProductNumber() : product.getId().toString(),
				product.getName(),
				ext));
			
			String contentDisposition = String.format("attachment; filename=\"%s\"", filename);
			
			storageService.putFile(file, path + s3Filename, contentDisposition, new ProgressListener() {
				@Override
				public void progressChanged(int progressPercent) {
					jobInfo.setProgressPercent(50 + (progressPercent/2));
					notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));						
				}
			});
			
			jobInfo.setProgressPercent(100);
			jobInfo.setIsCompleted(true);
			notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
			
			if (log.isDebugEnabled())
				log.debug(outputType +" for " + p.getName() + " stored at " + path + filename);
			
			file.delete();
			saveAttachment(p, path + s3Filename, attachmentType, jobInfo);
			
			ApplicationEvent e = null;
			if (outputType==OutputType.Jpeg)
				e = new AlbumJpegGeneratedEvent(p);
			else if (outputType==OutputType.Tiff)
				e = new AlbumTiffGeneratedEvent(p);
			else
				e = new AlbumPdfGeneratedEvent(p);
			
			eventService.sendEvent(e);
		
		} catch (InterruptedException e) {
			deleteAttachments(product, attachmentType);
			throw e;
		} catch (PrintGenerationException e) {
			deleteAttachments(product, attachmentType);
			throw e;
		}
	}

	@Override
	public void generateAlbumPreview(Product product, OutputType outputType, JobProgressInfo jobInfo) 
			throws InterruptedException, PrintGenerationException {
		
		LayoutPrintGenerator gen = getGenerator(outputType);
		File file = gen.generateAlbumPreview(product, getProgressListener(jobInfo, 0, 90));
		
		if (log.isDebugEnabled())
			log.debug("Saving low-res " +outputType +" file for " + product.getName());
		
		String path =  ApplicationConstants.LOW_RES_PDF_PATH;
		String filename = getFilename(product, file);
		String contentDisposition = String.format("attachment; filename=\"%s\"", filename);
		
		storageService.putFile(file, path + filename, contentDisposition);
		
		if (log.isDebugEnabled())
			log.debug("Low res " + outputType + " for " + product.getName() + " stored at " + path + filename);
		
		file.delete();
		saveAttachment(product, path + filename, AttachmentType.LowResPdf, jobInfo);
		
		jobInfo.setProgressPercent(100);
		jobInfo.setIsCompleted(true);
		notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
		
		AlbumPreviewPdfGeneratedEvent e = new AlbumPreviewPdfGeneratedEvent(product);
		e.setCurrentUser(jobInfo.getUser());
		eventService.sendEvent(e); 
	}
	
	@Override
	public void generateCover(Product product, OutputType outputType, JobProgressInfo jobInfo) throws InterruptedException, PrintGenerationException {
		
		String filename = null;
		String path = null;
		AttachmentType attachmentType = AttachmentType.valueOf("Cover"+outputType.name());
		Product p = product;
			
		if (log.isDebugEnabled())
			log.debug("Generating " + outputType + " cover for " + p.getName() + ", ID=" + p.getId());
		
		LayoutPrintGenerator gen = getGenerator(outputType);
		File file = gen.generateCover(p, getProgressListener(jobInfo, 0, 90));
		
		if (log.isDebugEnabled())
			log.debug("Saving " +outputType +" file for " + p.getName());
		
		path = ApplicationConstants.COVER_PATH + outputType.name().toLowerCase() + "/";
		filename = getFilename(product, file);
		String contentDisposition = String.format("attachment; filename=\"%s\"", filename);
		
		storageService.putFile(file, path + filename, contentDisposition, jobInfo);
		
		if (log.isDebugEnabled())
			log.debug(outputType +" cover for " + p.getName() + " stored at " + path + filename);
		
		file.delete();
		
		saveAttachment(p, path + filename, attachmentType, jobInfo);
		jobInfo.setProgressPercent(100);
		jobInfo.setIsCompleted(true);
		notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
		
		ApplicationEvent e = null;
		if (outputType==OutputType.Jpeg)
			e = new CoverJpegGeneratedEvent(p);
		else if (outputType==OutputType.Tiff)
			e = new CoverTiffGeneratedEvent(p);
		else
			e = new CoverPdfGeneratedEvent(p);
		
		eventService.sendEvent(e);
			
		jobInfo.setProgressPercent(100);
		jobInfo.setIsCompleted(true);
		notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
	}

	private LayoutPrintGenerator getGenerator(OutputType outputType) {
		if (outputType==OutputType.Pdf) {
			return pdfGenerator;
		} else if (outputType==OutputType.Jpeg) {
			return jpegGenerator;
		} else if (outputType==OutputType.Tiff) {
			return tiffGenerator;
		}
		throw new IllegalArgumentException("Wrong output type"); 
	}
	
	private String getFilename(Product product, File file) {
		String ext = FilenameUtils.getExtension(file.getName());
		String filename = URLUtils.slug( String.format("%s%s_%s",
				product.getBatch()!=null ? product.getBatch().getName() + "-" : "",
				product.getProductNumber()!=null ? product.getProductNumber() : product.getId(),
				product.getName()));
		
		try {
			GenericRule rule = ruleService.findRule("PRINT_FILENAME");
			if (rule!=null) {
				filename = URLUtils.slug( expressionEvaluator.evaluate(
					new ProductContext(product), rule.getJsonData(), String.class) );
			}
			
		} catch(Exception ex) {
			log.warn("Cannot eval PRINT_FILENAME. " + ex.getMessage());
		}
		
		return filename + "." + ext;
	}
	
	private void saveAttachment(Product product, String url, AttachmentType attachmentType,
			JobProgressInfo jobInfo) {
		productService.saveAttachment(product, url, attachmentType, jobInfo.getUser());
	}

	private void deleteAttachments(Product product, AttachmentType attachmentType) {
		List<Attachment> attachments = attachmentRepo.findByProductAndType(product, attachmentType);
		if (attachments.size()>0) {
			attachmentRepo.delete(attachments);
			product.getAttachments().removeAll(attachments);
		}
	}
	
	private ProgressListener getProgressListener(final JobProgressInfo job, final int minProgress, final int maxProgress) {
		return new ProgressListener() {
			
			@Override
			public void progressChanged(int progressPercent) {
				job.setIsWaiting(false);
				job.setProgressPercent( (int) (minProgress + (progressPercent/100f * (maxProgress-minProgress))));
				notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, job));
			}
		};
	}

}
