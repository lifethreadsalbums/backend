package com.poweredbypace.pace.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.poweredbypace.pace.config.test.TestConfig;
import com.poweredbypace.pace.service.ImageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
public class ImageServiceTest {
	
	private static final int RAND_MAX = 1000;
	
	private static final String[] imagePaths = new String[] {
		"/test-cases/01_BW.jpg",
		"/test-cases/02_BW.jpg",
		"/test-cases/03_BW.jpg"
	};
	
	@Autowired
	ImageService imageService;
	
	@Test
	public void resizeFileTest() throws IOException {
		File inFile = getAnyImageFile();
		assertTrue("input file should exist", inFile.exists());
		
		final int targetSize = new Random().nextInt(RAND_MAX) + 1;
		
		File outFile = imageService.resize(inFile, targetSize, targetSize);
		assertTrue("output file should exist", outFile.exists());
		
		BufferedImage img = ImageIO.read(outFile);
		assertEquals("holds dimension", targetSize, Math.max(img.getWidth(), img.getHeight()));
	}
	
	@Test
	public void resizeFileWithOutputGivenTest() throws IOException {
		File inFile = getAnyImageFile();
		assertTrue("input file should exist", inFile.exists());
		
		File outFile = File.createTempFile("pace-test-", "");
		outFile.deleteOnExit();
		
		final int targetSize = new Random().nextInt(RAND_MAX) + 1;
		
		imageService.resize(inFile, outFile, targetSize, targetSize);
		assertTrue("output file should exist", outFile.exists());
		
		BufferedImage img = ImageIO.read(outFile);
		assertEquals("holds dimension", targetSize, Math.max(img.getWidth(), img.getHeight()));
	}
	
	@Test
	public void resizeFileIntoTwoOutputsTest() throws IOException {
		File inFile = getAnyImageFile();
		assertTrue("input file should exist", inFile.exists());
		
		File outFile1 = File.createTempFile("pace-test-", "");
		outFile1.deleteOnExit();
		
		File outFile2 = File.createTempFile("pace-test-", "");
		outFile1.deleteOnExit();
		
		final Random r = new Random();
		final int targetSize1 = r.nextInt(RAND_MAX) + 1;
		final int targetSize2 = r.nextInt(RAND_MAX) + 1;
		
		imageService.resize(inFile, outFile1, targetSize1, targetSize1,
				outFile2, targetSize2, targetSize2);
		assertTrue("output1 file should exist", outFile1.exists());
		assertTrue("output2 file should exist", outFile2.exists());
		
		BufferedImage img = ImageIO.read(outFile1);
		assertEquals("holds dimension", targetSize1, Math.max(img.getWidth(), img.getHeight()));
		
		img = ImageIO.read(outFile2);
		assertEquals("holds dimension", targetSize2, Math.max(img.getWidth(), img.getHeight()));
	}
	
	@Test
	public void rotateTest() throws IOException {
		File inFile = getAnyImageFile();
		assertTrue("input file should exist", inFile.exists());
		
		File outFile = imageService.rotate(inFile, 90);
		assertTrue("output file should exist", outFile.exists());
		
		BufferedImage img = ImageIO.read(inFile);
		final int inWidth = img.getWidth();
		final int inHeight = img.getHeight();
		img = ImageIO.read(outFile);
		final int outWidth = img.getWidth();
		final int outHeight = img.getHeight();
		assertEquals("width of input should be height of output", inWidth, outHeight);
		assertEquals("height of input should be width of output", inHeight, outWidth);
	}
	
	@Test
	public void cropTest() throws IOException {
		File inFile = getAnyImageFile();
		assertTrue("input file should exist", inFile.exists());
		
		BufferedImage img = ImageIO.read(inFile);
		final int inWidth = img.getWidth();
		final int inHeight = img.getHeight();
		
		final Random r = new Random();
		final int cropWidth = r.nextInt(Math.min(inWidth, inHeight)) + 1;
		final int cropHeight = r.nextInt(Math.min(inWidth, inHeight)) + 1;
		final int offsetX = r.nextInt(inWidth - cropWidth) + 1;
		final int offsetY = r.nextInt(inHeight - cropHeight) + 1;
		
		File outFile = imageService.crop(inFile, cropWidth, cropHeight, offsetX, offsetY);
		assertTrue("output file should exist", outFile.exists());
		
		img = ImageIO.read(outFile);
		assertEquals("holds width", cropWidth, img.getWidth());
		assertEquals("holds height", cropHeight, img.getHeight());
	}
	
	@Test
	public void splitTest() throws IOException {
		File inFile = getAnyImageFile();
		assertTrue("input file should exist", inFile.exists());
		
		BufferedImage img = ImageIO.read(inFile);
		final int inWidth = img.getWidth();
		final int inHeight = img.getHeight();
		
		File outFile1 = File.createTempFile("pace-test-", "");
		outFile1.deleteOnExit();
		
		File outFile2 = File.createTempFile("pace-test-", "");
		outFile2.deleteOnExit();
		
		final Random r = new Random();
		final int width1 = r.nextInt(inWidth) + 1,
				width2 = r.nextInt(width1) + 1,
				height1 = r.nextInt(inHeight) + 1,
				height2 = r.nextInt(height1) + 1,
				offsetX1 = r.nextInt(inWidth - width1),
				offsetX2 = r.nextInt(width1 - width2),
				offsetY1 = r.nextInt(inHeight - height1),
				offsetY2 = r.nextInt(height1 - height2);
		
		imageService.split(inFile,
				outFile1, width1, height1, offsetX1, offsetY1,
				outFile2, width2, height2, offsetX2, offsetY2);
		
		img = ImageIO.read(outFile1);
		assertEquals("first holds width", width1, img.getWidth());
		assertEquals("first holds height", height1, img.getHeight());
		
		img = ImageIO.read(outFile2);
		assertEquals("second holds width", width2, img.getWidth());
		assertEquals("second holds height", height2, img.getHeight());
	}
	
	private File getAnyImageFile() {
		final Random r = new Random();
		final URL url = getClass().getResource(imagePaths[r.nextInt(imagePaths.length)]);
		try {
			return new File(url.toURI().getRawPath());
		} catch (URISyntaxException e) {
			// this should never happen
			return null;
		}
	}

}