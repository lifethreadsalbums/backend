package com.poweredbypace.pace.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gm4java.engine.GMException;
import org.gm4java.engine.GMServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.exception.IccProfileConversionException;
import com.poweredbypace.pace.expression.ExpressionContext;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ImageFileContext;
import com.poweredbypace.pace.expression.impl.JavaScriptExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.repository.IccProfileRepository;
import com.poweredbypace.pace.repository.ImageFileRepository;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.IccProfileService;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.util.PaceFileUtils;
import com.poweredbypace.pace.util.ProcessUtils;

@Service
public class IccProfileServiceImpl implements IccProfileService {
	
	private final String PDF_JPEG_QUALITY = "PDF_JPEG_QUALITY";
	
	private final Log logger = LogFactory.getLog(getClass());
	
	private String jpgiccPath;
	private String tificcPath;
	
	@Value("${icc.profilesPath}")
	String profilesPath;
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	IccProfileRepository iccProfileRepo;
	
	@Autowired
	ImageFileRepository imageFileRepository;
	
	@Autowired
	GenericRuleService ruleService;
	
	ExpressionEvaluator expressionEvaluator = new JavaScriptExpressionEvaluator();
	
	@Autowired
	NotificationBroadcaster notificationBroadcaster;
	
	public void setProfilesPath(String profilesPath) {
		this.profilesPath = profilesPath;
	}
	
	public String getProfilesPath() {
		return this.profilesPath;
	}

	@PostConstruct
	public void postConstruct()
	{
		jpgiccPath = ProcessUtils.findCommandPath("jpgicc");
		tificcPath = ProcessUtils.findCommandPath("tificc2");
		if (tificcPath==null)
			tificcPath = ProcessUtils.findCommandPath("tificc");
		
		logger.info("Configuring icc profile service, jpgicc=" + jpgiccPath + ", tifficc=" + tificcPath);
		if (jpgiccPath==null)
			logger.error("Cannot find jpgicc, please install it.");
		if (tificcPath==null)
			logger.error("Cannot find tificc, please install it.");
	}
	
	@Override
	public File getImage(ImageFile image, IccProfile iccProfile) {
		
		String filename = FilenameUtils.getName(image.getUrl());
		File file = null;
		
		String ext =  FilenameUtils.getExtension(filename);
		if (StringUtils.equalsIgnoreCase(ext, "png")) {
			filename = FilenameUtils.getBaseName(filename) + ".jpg";
		}
		try {			
			String url = String.format("images/%s/%s", iccProfile.getCode(), filename);
			if (logger.isDebugEnabled()) logger.debug("Downloading image from "+url);
			file = storageService.getFile(url);
			if (logger.isDebugEnabled()) logger.debug("Image downloaded, url="+url);
		} catch (Exception ex) {
			logger.info("CMYK image not found, creating a new one");
			file = convert(image, iccProfile);
		}
		return file;
	}
	
	@Override
	public File getImage(ImageFile image, Product product) {
		return getImage(image, getIccProfile(image, product));
	}
	
	@Override
	public IccProfile getIccProfile(ImageFile image, Product product) {
		if (image.getIsBlackAndWhite()==null) {
			File file = storageService.getFile(ApplicationConstants.ORIGINAL_IMAGE_PATH + image.getUrl());
			boolean isBW = imageService.isImageBlackAndWhite(file);
			image.setIsBlackAndWhite(isBW);
			file.delete();
		}
		if (image.getCustomIccProfile()!=null) 
			return image.getCustomIccProfile();
		
		IccProfile targetProfile = findProfile(new ImageFileContext(product, image));
		if (targetProfile==null) {
			targetProfile = findProfile(new ProductContext(product));
		}
		return targetProfile;
	}
	
	
	@Override
	public IccProfile getIccProfile(Product product) {
		return findProfile(new ProductContext(product));
	}
	
	private IccProfile findProfile(ExpressionContext context) {
		List<IccProfile> profiles = iccProfileRepo.findAll();
		IccProfile targetProfile = null;
		for(IccProfile iccProfile:profiles) {
			try {
				Boolean condition = expressionEvaluator.evaluate(context, 
						iccProfile.getConditionExpression(), 
						Boolean.class);
	  			
	  			if (BooleanUtils.isTrue(condition)) {
	  				targetProfile = iccProfile;
	  				break;
	  			}
			} catch(Exception e) {
				logger.error("Error while evaluating expression:'" + iccProfile.getConditionExpression() + "'. " + e.getMessage());
			}
		}
		return targetProfile;
	}

	/**
	 * Converts an image to a different color space using a give icc profile
	 * @param in input file
	 * @param out output file
	 * @param profile icc profile info
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws GMServiceException 
	 * @throws GMException 
	 */
	/*
	private void convert(File in, File out, ImageFile image, IccProfile profile) throws IOException, InterruptedException, GMException, GMServiceException {
		
		String profilePath = profile.getProfile();
		int quality = ruleService.getRuleValue(PDF_JPEG_QUALITY, Integer.class);
					
		String path = profilesPath + "/" + profilePath;
		
		if (logger.isDebugEnabled())
			logger.debug("Converting image " + image.getFilename() + " to CMYK, profile=" + profilePath);
		
//		File tifFile = File.createTempFile("pace-image-", ".tif");
//		
//		imageService.getGMService().execute("convert", 
//				"-quality",
//				"100",
//				"-compress",
//				"lzw",
//				in.getAbsolutePath(),
//				tifFile.getAbsolutePath());
		
		
		List<String> args = new ArrayList<String>();
		args.add(jpgiccPath);
		args.add(String.format("-q%d", quality));
		args.add("-o" + path);
		args.add("-t0");
		if (profile.getBlackPointCompensation())
			args.add("-b");
		args.add("-e");
		//args.add(tifFile.getAbsolutePath());
		args.add(in.getAbsolutePath());
		args.add(out.getAbsolutePath());
		
		String[] args2 = new String[args.size()];
		args.toArray(args2);		
		ProcessUtils.exec(args2);
		
		//tifFile.delete();
	}
	*/
	
	private File doConvert(File rgbFile, IccProfile profile) throws IOException, InterruptedException {
		
		String ext = FilenameUtils.getExtension(rgbFile.getName());
		
		String profilePath = profile.getProfile();
		String path = profilesPath + "/" + profilePath;
		
		if (logger.isDebugEnabled())
			logger.debug("Converting image " + rgbFile.getAbsolutePath() + ", profile=" + profilePath);
		
//			File tifFile = File.createTempFile("pace-image-", ".tif");
		
//			imageService.getGMService().execute("convert", 
//					"-quality",
//					"100",
//					"-compress",
//					"lzw",
//					rgbFile.getAbsolutePath(),
//					tifFile.getAbsolutePath());
		
		File outFile = File.createTempFile("pace-cmyk-image-", "."	+ ext);
		
		List<String> args = new ArrayList<String>();
		if ("tif".equals(ext)) {
		
			args.add(tificcPath);
			args.add("-o" + path);
			args.add("-t0");
			if (profile.getBlackPointCompensation())
				args.add("-b");
			args.add("-e");
			args.add(rgbFile.getAbsolutePath());
			args.add(outFile.getAbsolutePath());
		} else {
			int quality = ruleService.getRuleValue(PDF_JPEG_QUALITY, Integer.class);
			
			args.add(jpgiccPath);
			args.add(String.format("-q%d", quality));
			args.add("-o" + path);
			args.add("-t0");
			if (profile.getBlackPointCompensation())
				args.add("-b");
			args.add("-e");
			args.add(rgbFile.getAbsolutePath());
			args.add(outFile.getAbsolutePath());
		}
		
		String[] args2 = new String[args.size()];
		args.toArray(args2);		
		ProcessUtils.exec(args2);
		
		return outFile;
		
	}
	
	
	@Override
	public File convert(File rgbFile, IccProfile profile) {
		try {
			return doConvert(rgbFile, profile);
		} catch (Exception ex) {
			logger.error("Unable to convert image", ex);
			throw new IccProfileConversionException(
				String.format("Unable to convert image %s to CMYK. %s", rgbFile.getAbsolutePath(), ex.getMessage()), ex);
		}
	}
	
	
	@Override
	@Transactional
	public File convert(ImageFile image, IccProfile iccProfile) {
		try {
			
			String filename = FilenameUtils.getName(image.getUrl());
			File rgbFile = storageService.getFile(ApplicationConstants.ORIGINAL_IMAGE_PATH + image.getUrl());
			String ext = FilenameUtils.getExtension(filename);
			
			if (StringUtils.equalsIgnoreCase(ext, "png"))
			{
				File jpgFile = File.createTempFile("pace-image-", ".jpg");
				File maskFile = File.createTempFile("pace-image-", ".png");
				imageService.convertPngToJpegAndMask(rgbFile, jpgFile, maskFile);

				String maskFileName = PaceFileUtils.appendStringBeforeExtension(filename, "-mask");
				storageService.putFile(maskFile, "images/" + iccProfile.getCode() + "/" + maskFileName);
				
				rgbFile = jpgFile;
				ext = "jpg";
				filename = FilenameUtils.getBaseName(filename) + ".jpg";
			}
			
			File file = doConvert(rgbFile, iccProfile);
			
			storageService.putFile(file, "images/" + iccProfile.getCode() + "/" + filename);
			rgbFile.delete();
			
			//save information about profile
			image.setTargetIccProfile(iccProfile);
			imageFileRepository.save(image);
			notificationBroadcaster.broadcast(
				Notification.create(NotificationType.IccProfileConverted, image) );
			
			return file;
		} catch (Exception ex) {
			logger.error("Unable to convert image " + image.getFilename(), ex);
			throw new IccProfileConversionException(
				String.format("Unable to convert image %s to CMYK. %s", image.getUrl(), ex.getMessage()), ex);
		}
	}

	
}
