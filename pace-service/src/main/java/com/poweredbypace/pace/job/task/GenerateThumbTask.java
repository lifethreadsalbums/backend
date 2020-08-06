package com.poweredbypace.pace.job.task;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.exception.ImageProcessingException;
import com.poweredbypace.pace.repository.ImageFileRepository;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.service.StorageService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GenerateThumbTask extends AbstractTask {
	
	public static class Params {
		public long imageId;
	}
	
	public static final String DATA_IMAGE_ID = "imageId";

	private static final String LOW_RES_IMG_PREFIX = "pace-img-lowres-";
	private static final String LOW_RES_IMG_DIR = "images/lowres/";
	private static final int LOW_RES_IMG_WIDTH = 1000;
	private static final int LOW_RES_IMG_HEIGHT = 1000;
	
	private static final String THUMBNAIL_PREFIX = "pace-img-thumb-";
	private static final String THUMBNAIL_DIR = "images/thumbnail/";
	private static final int THUMBNAIL_WIDTH = 240;
	private static final int THUMBNAIL_HEIGHT = 240;
	
	private static final int TIMEOUT = 60 * 10; // 10 minutes
	
	@Autowired
	private ImageService imageService;
	
	@Autowired
	private ImageFileRepository imageRepo;
	
	@Autowired
	private StorageService storageService;
	
	@Override
	public void run() {
		final Params params = (Params)job.getParams();
		final long imgId = params.imageId;
		final ImageFile img = imageRepo.getOne(imgId);
		if(img != null) {
			try {
				File originalImg = storageService.getFile(img.getUrl());
				File lowResImg = File.createTempFile(LOW_RES_IMG_PREFIX, "");
				File thumbnail = File.createTempFile(THUMBNAIL_PREFIX, "");
				
				log.debug("Generating thumb and low-res images, image: " + img.getUrl());
				
				resizeImages(originalImg, lowResImg, thumbnail);
				putImagesToStorage(img, lowResImg, thumbnail);
				cleanup(originalImg, lowResImg, thumbnail);
				
				log.debug("Done, image: " + img.getUrl());
			} catch(IOException e) {
				log.error("Error while resizing the image", e);
				throw new RuntimeException();
			}
		} else {
			log.error("Unable to find image: " + imgId);
			return;
		}
	}

	@Override
	public int getTimeout() {
		return TIMEOUT;
	}
	
	private void resizeImages(File originalImg, File lowResImg, File thumbnail) throws ImageProcessingException {
		imageService.resize(
				originalImg,
				lowResImg, LOW_RES_IMG_WIDTH, LOW_RES_IMG_HEIGHT,
				thumbnail, THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
	}
	
	private void putImagesToStorage(ImageFile img, File lowResImg, File thumbnail) {
		log.debug("Saving thumb and low-res images on S3, image: " + img.getUrl());
		final String imgFilename = String.format("img-%s-%s",
				UUID.randomUUID(),
				img.getFilename().replaceAll("", "_"));
		
		storageService.putFile(
				lowResImg,
				LOW_RES_IMG_DIR + imgFilename);
		
		storageService.putFile(
				thumbnail,
				THUMBNAIL_DIR + imgFilename);
	}
	
	private void cleanup(File originalImg, File lowResImg, File thumbnail) {
		originalImg.delete();
		lowResImg.delete();
		thumbnail.delete();
	}

}