package com.poweredbypace.pace.service.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gm4java.engine.GMService;
import org.gm4java.engine.support.GMConnectionPoolConfig;
import org.gm4java.engine.support.PooledGMService;
import org.gm4java.im4java.GMBatchCommand;
import org.im4java.core.IMOperation;
import org.springframework.stereotype.Service;

import com.google.common.io.CharStreams;
import com.poweredbypace.pace.exception.ImageProcessingException;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.util.ProcessUtils;

@Service
public class ImageServiceImpl implements ImageService {
	
	private static final String OP_CONVERT = "convert";
	private static final float NUM_WHITE_PIXELS_THRESHOLD = 0.999f;
	
	private final Log log = LogFactory.getLog(getClass());
	
	private GMService gmService;
	
	public GMService getGMService() {
		return gmService;
	}
	
	@PostConstruct
	public void postConstruct() {
		final GMConnectionPoolConfig config = new GMConnectionPoolConfig();
		
		//TODO: config the pool
		config.setGMPath(ProcessUtils.findCommandPath("gm"));
		gmService = new PooledGMService(config);
	}

	@Override
	public File resize(File image, int width, int height, int dpi) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
			File tempFile = getTempFile();
			
			IMOperation op = new IMOperation();
			op.units("PixelsPerInch");
			op.addImage(image.getAbsolutePath());
			op.density(dpi);
			op.resize(width, height);
			op.addImage(tempFile.getAbsolutePath());
			
			cmd.run(op);
			
			return tempFile;
		} catch (Exception e) {
			throw new ImageProcessingException("Unable to resize image", e);
		}
	}
	
	@Override
	public void resize(File imageIn, File imageOut, int width, int height, int dpi) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
			
			IMOperation op = new IMOperation();
			op.units("PixelsPerInch");
			op.addImage(imageIn.getAbsolutePath());
			op.density(300);
			op.resample(dpi);
			op.resize(width, height);
			op.addImage(imageOut.getAbsolutePath());
			
			cmd.run(op);
		} catch (Exception e) {
			throw new ImageProcessingException("Unable to resize image", e);
		}
	}
	
	@Override
	public File resize(File image, int width, int height) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
			File tempFile  = getTempFile();
			
			IMOperation op = new IMOperation();
			op.size(width, height);
			op.addImage(image.getAbsolutePath());
			op.resize(width, height);
			op.addImage(tempFile.getAbsolutePath());
			
			cmd.run(op);
			
			return tempFile;
		} catch (Exception e) {
			throw new ImageProcessingException("Unable to resize image", e);
		}
	}

	@Override
	public void resize(File imageIn, File imageOut, int width, int height) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
			
			IMOperation op = new IMOperation();
			op.size(width, height);
			op.addImage(imageIn.getAbsolutePath());
			op.resize(width, height);
			op.addImage(imageOut.getAbsolutePath());
			
			cmd.run(op);
		} catch (Exception e) {
			throw new ImageProcessingException("Unable to resize image", e);
		}
	}

	@Override
	public void resize(File inputImage, File outputImage1, int width1,
			int height1, File outputImage2, int width2, int height2) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
			
			final int maxWidth = Math.max(width1, width2);
			final int maxHeight = Math.max(height1, height2);
			
			IMOperation op = new IMOperation();
			op.size(maxWidth, maxHeight);
			op.addImage(inputImage.getAbsolutePath());
			
			op.resize(width1, height1);
			op.write(outputImage1.getAbsolutePath());
			
			op.resize(width2, height2);
			op.addImage(outputImage2.getAbsolutePath());
			
			cmd.run(op);
		} catch (Exception e) {
			throw new ImageProcessingException("Unable to resize image", e);
		}
	}

	@Override
	public File rotate(File image, double rotation) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
			File tempFile = getTempFile();
			
			IMOperation op = new IMOperation();
			op.addImage(image.getAbsolutePath());
			op.rotate(rotation);
			op.addImage(tempFile.getAbsolutePath());
			
			cmd.run(op);
			
			return tempFile;
		} catch (Exception e) {
			throw new ImageProcessingException("Unable to rotate image", e);
		}
	}

	@Override
	public File crop(File image, int width, int height, int offsetX, int offsetY) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
			File tempFile = getTempFile();
			
			IMOperation op = new IMOperation();
			op.addImage(image.getAbsolutePath());
			op.crop(width, height, offsetX, offsetY);
			op.addImage(tempFile.getAbsolutePath());
			
			cmd.run(op);
			
			return tempFile;
		} catch (Exception e) {
			throw new ImageProcessingException("Unable to crop image", e);
		}
	}
	
	@Override
	public File cropAndResize(File image, int width, int height, 
		int offsetX, int offsetY, int width2, int height2, boolean convertToTIFF) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
	
			String ext = convertToTIFF ? ".tif" : ".jpg";
			File tempFile = File.createTempFile("pace-image-", ext);
			tempFile.deleteOnExit();
			
			// create the operation, add images and operators/options
			IMOperation op = new IMOperation();
			op.addImage(image.getAbsolutePath());
			op.crop(width, height, offsetX, offsetY);
			op.resize(width2, height2);
			if (convertToTIFF) {
				op.compress("LZW");
			}
			op.addImage(tempFile.getAbsolutePath());
	
			// execute the operation
			cmd.run(op);
			
			return tempFile;
		} catch (Exception ex) {
			throw new ImageProcessingException("Unable to crop image", ex);
		} 
	}

	@Override
	public void split(File inputImage, File outputImage1, int width1,
			int height1, int offsetX1, int offsetY1, File outputImage2,
			int width2, int height2, int offsetX2, int offsetY2) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
			
			IMOperation op = new IMOperation();
			op.addImage(inputImage.getAbsolutePath());
			
			op.crop(width1, height1, offsetX1, offsetY1);
			op.write(outputImage1.getCanonicalPath());
			
			op.crop(width2, height2, offsetX2, offsetY2);
			op.addImage(outputImage2.getAbsolutePath());
			
			cmd.run(op);
		} catch (Exception e) {
			throw new ImageProcessingException("Unable to split image", e);
		}
	}


	@Override
	public boolean isImageBlackAndWhite(File file) 
	{
		ProcessBuilder pb = new ProcessBuilder("python","main.py", file.getAbsolutePath())
			.directory(new File("/pace/gmtools"));
		
		boolean result = false;
		Process pr;
		try {
			pr = pb.start();
		
			String output = CharStreams.toString(new InputStreamReader(pr.getInputStream(), "UTF-8"));
			result = StringUtils.equals(output, "true\n");
				
			int res = pr.waitFor();
			if (res!=0) {
				String errorString = CharStreams.toString(new InputStreamReader(pr.getErrorStream(), "UTF-8"));
				throw new ImageProcessingException(errorString);
			}
			
			pr.getInputStream().close();
			pr.getOutputStream().close();
			pr.getErrorStream().close();
			
		} catch (Exception e) {
			log.error("Unable to determine whether image is B/W "+file.getName() + ". " + e.getMessage());
			//throw new ImageProcessingException();
			return false;
		}
		return result;
	}
	
	@Override
	public void convertPngToJpegAndMask(File pngFile, File jpgFile, File maskFile) {
		try {
			//create flatten jpg file
			gmService.execute("convert", 
				pngFile.getAbsolutePath(), 
				"-flatten",
				jpgFile.getAbsolutePath());
			
			//create mask
			gmService.execute("convert", 
				pngFile.getAbsolutePath(),
				"+profile", 
				"\"*\"", 
				"-channel",
				"Opacity",
				maskFile.getAbsolutePath());
		} catch (Exception ex) {
			throw new ImageProcessingException(
				String.format("Unable to convert PNG image %s to JPEG.", pngFile.getAbsolutePath()), ex);
		}
	}
	
	@Override
	public boolean isImageBlank(File f) {
		try {
			BufferedImage img = ImageIO.read(f);
			int numWhilePixels = 0;
			for(int y=0;y<img.getHeight();y++)
				for(int x=0;x<img.getWidth();x++)
				{
					int color = img.getRGB(x, y);
					if (color==0xffffffff)
						numWhilePixels++;
				}
			float numWhilePixelsNorm = (float)numWhilePixels / (float)(img.getWidth()*img.getHeight());
			log.debug(String.format("Checking if image is blank, num white pixels=%f", numWhilePixelsNorm));
			return numWhilePixelsNorm > NUM_WHITE_PIXELS_THRESHOLD;
		} catch (IOException e) {
			log.error("Unable to load image from "+f.getName(), e);
			throw new ImageProcessingException("Unable to load image from "+f.getName(), e);
		}
		
	}
	
	@Override
	public File blackAndWhite(File image) {
		IMOperation op = new IMOperation();
		op.modulate(100d, 0d);
		return convert(image, op);
	}
	
	@Override
	public File sepia(File image) {
		IMOperation op = new IMOperation();
		op.modulate(115d,  0d,  100d);
		op.colorize(7,  21,  50);
		return convert(image, op);
	}
	
	private File convert(File image, IMOperation operation) {
		try {
			GMBatchCommand cmd = new GMBatchCommand(gmService, OP_CONVERT);
			File tempFile = getTempFile();
			
			IMOperation op = new IMOperation();
			op.addImage(image.getAbsolutePath());
			op.addOperation(operation);
			op.addImage(tempFile.getAbsolutePath());
			
			cmd.run(op);
			return tempFile;
		} catch (Exception e) {
			throw new ImageProcessingException("Unable to process image", e);
		}
	}
	
	private File getTempFile() throws IOException {
		File file = File.createTempFile("pace-image-", "");
		file.deleteOnExit();
		return file;
	}
	
}