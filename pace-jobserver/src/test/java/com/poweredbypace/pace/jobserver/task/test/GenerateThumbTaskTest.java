package com.poweredbypace.pace.jobserver.task.test;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.poweredbypace.pace.job.Job;
import com.poweredbypace.pace.job.task.AbstractTask;
import com.poweredbypace.pace.job.task.GenerateThumbTask;
import com.poweredbypace.pace.job.task.Task;
import com.poweredbypace.pace.jobserver.config.TaskConfig;
import com.poweredbypace.pace.jobserver.config.TaskTestConfig;
import com.poweredbypace.pace.jobserver.mock.Data;
import com.poweredbypace.pace.jobserver.mock.StorageServiceMock;
import com.poweredbypace.pace.service.StorageService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TaskTestConfig.class, TaskConfig.class }, loader = AnnotationConfigContextLoader.class)
public class GenerateThumbTaskTest {
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	StorageService storageService;
	
	@Test
	public void resizeTest() throws IOException {
		Task task = AbstractTask.get(applicationContext, getJob());
		task.run();
		
		File lowres = ((StorageServiceMock)storageService).getFirstLowres();
		File thumb = ((StorageServiceMock)storageService).getFirstThumbnail();
		assertNotNull("lowres not null", lowres);
		assertNotNull("thumb not null", thumb);
		assertTrue("lowres exists", lowres.exists());
		assertTrue("thumb exists", thumb.exists());
		
		BufferedImage img = ImageIO.read(lowres);
		assertEquals("lower holds dimension", 1000, Math.max(img.getWidth(), img.getHeight()));
		
		img = ImageIO.read(thumb);
		assertEquals("thumbnail holds dimension", 240, Math.max(img.getWidth(), img.getHeight()));
	}
	
	private Job getJob() {
		Job job = new Job();
		
		final Random r = new Random();
		job.setType(GenerateThumbTask.class);
		GenerateThumbTask.Params params = new GenerateThumbTask.Params();
		params.imageId = (long)r.nextInt(Data.IMAGE_PATHS.length);
		job.setParams(params);
		
		return job;
	}
	
}
