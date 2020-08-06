package com.poweredbypace.pace.controller;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.File;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.ImageFile.ImageFileStatus;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.dto.ThumbnailDto;
import com.poweredbypace.pace.repository.FileRepository;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.util.UrlUtil;
import com.sun.jersey.core.util.Base64;

@Controller
public class FileUploadController {

	private final Log log = LogFactory.getLog(FileUploadController.class);
	
	@Autowired
	private FileRepository fileRepo;
	
	@Autowired
	private StorageService storageService;
	
	
	@RequestMapping(value = "/api/upload", method = RequestMethod.POST)
	@ResponseBody
	public File handleUpload(
			@AuthenticationPrincipal User user,
			@RequestParam("file") MultipartFile multipartFile, 
			@RequestParam String fileSetCode) {
		
		log.info("File uploaded " + multipartFile.getName());
			
		File file = new File();
		file.setUser(user);
		file.setFilename(multipartFile.getOriginalFilename());
		file = fileRepo.save(file);
		
		return file;
	}
			
	@RequestMapping(value = "/api/imageupload", method = RequestMethod.POST)
	@ResponseBody
	public File handleImageUpload(
			@AuthenticationPrincipal User user,
			@RequestParam("file") MultipartFile multipartFile, 
			@RequestParam("imageId") Long imageId) throws IOException {
		
		log.info("File uploaded " + multipartFile.getOriginalFilename());
		
		if (multipartFile.getSize()==0) {
			log.info("File " + multipartFile.getOriginalFilename() +" has zero bytes. User " + user.getEmail());
			throw new IOException("Error while uploading file " + multipartFile.getOriginalFilename());
		}
		
		ImageFile file = (ImageFile) fileRepo.findOne(imageId);
		file.setSize(multipartFile.getSize());
		file.setCreationDate(new Date());
		file.setFilename(multipartFile.getOriginalFilename());
		file.setStatus(ImageFileStatus.Uploaded);
		file.setErrorMessage(null);
		file.setUser(user);
			
		//store original file
		String path = ApplicationConstants.ORIGINAL_IMAGE_PATH + file.getUrl();
		java.io.File tempFile = java.io.File.createTempFile("pace-image-", "");
		multipartFile.transferTo(tempFile);
		storageService.putFile(tempFile, path);
		
		tempFile.delete();
		fileRepo.save(file);
		
		return file;
	}
	
	
	@RequestMapping(value = "/api/imagefile/thumb", method = RequestMethod.POST)
	@ResponseBody
	public File saveThumbnail(
			@AuthenticationPrincipal User user,
			@RequestBody ThumbnailDto thumb) throws IOException {
		
		ImageFile file = fileRepo.save(thumb.getImageFile());
		
		byte[] thumbnailBytes = Base64.decode(thumb.getThumbnailAsBase64());
		java.io.File thumbnail = java.io.File.createTempFile("pace-thumb-", ".jpg");
		FileUtils.writeByteArrayToFile(thumbnail, thumbnailBytes);
		String filename = UrlUtil.slug(file.getId() + "-" + file.getFilename());
		String path = ApplicationConstants.THUMB_IMAGE_PATH + filename;
		storageService.putFile(thumbnail, path);
		
		thumbnail.delete();
		
		file.setUrl(filename);
		
		//ImageFileStatus status = file.getStatus();
		//String errorMessage = file.getErrorMessage();
		
		//file.setStatus(ImageFileStatus.Rejected);
		//file.setErrorMessage(ImageFile.GENERIC_UPLOAD_ERROR_MESSAGE);
		if (file.getStatus()==ImageFileStatus.Preflighted)
			file.setStatus(ImageFileStatus.UploadInProgress);
		
		fileRepo.save(file);
		
		log.info("Thumbnail " + filename +" uploaded.");
		
		//file.setStatus(status);
		//file.setErrorMessage(errorMessage);
		return file;
	}
			
}
