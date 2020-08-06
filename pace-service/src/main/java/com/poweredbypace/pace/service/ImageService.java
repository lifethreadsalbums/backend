package com.poweredbypace.pace.service;

import java.io.File;

import org.gm4java.engine.GMService;

public interface ImageService {
	
	File resize(File image, int width, int height, int dpi);
	void resize(File imageIn, File imageOut, int width, int height, int dpi);
	
	File resize(File image, int width, int height);
	void resize(File imageIn, File imageOut, int width, int height);
	
	void resize(File inputImage, File outputImage1, int width1, int height1, 
		File outputImage2, int width2, int height2);
	
	File rotate(File image, double rotation);
	
	File crop(File image, int width, int height, int offsetX, int offsetY);
	File cropAndResize(File image, int width, int height, int offsetX, int offsetY, int width2, int height2, boolean convertToTIFF);
	
	void split(File inputImage, 
		File outputImage1, int width1, int height1, int offsetX1, int offsetY1,
		File outputImage2, int width2, int height2, int offsetX2, int offsetY2);
	
	boolean isImageBlackAndWhite(File file);
	
	boolean isImageBlank(File f);
	
	void convertPngToJpegAndMask(File pngFile, File jpgFile, File maskFile);
	
	File blackAndWhite(File image);
	File sepia(File image);
	
	GMService getGMService();
	
}
