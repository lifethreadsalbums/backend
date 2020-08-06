package com.poweredbypace.pace.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import com.poweredbypace.pace.service.ScreenshotService;
import com.poweredbypace.pace.util.ProcessUtils;

public class ScreenshotServiceImpl implements ScreenshotService {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Value("${screenshot.apiUser}")
	private String apiUser;
	@Value("${screenshot.apiPass}")
	private String apiPass;
	@Value("${screenshot.loginUrl}")
	private String loginUrl;

	@Override
	public File screenshot(String url) throws IOException, InterruptedException, URISyntaxException {
		
		log.info("Screenshot from " + url);
		InputStream is = getClass().getResourceAsStream("/scripts/covershot.js");
		
		File scriptFile = File.createTempFile("pace-js-",".js");
	    IOUtils.copy(is, FileUtils.openOutputStream(scriptFile));
		
		File outputFile = File.createTempFile("pace-screenshot-", ".jpg");
		ProcessUtils.exec("phantomjs2",
			"--ignore-ssl-errors=yes",
			scriptFile.getAbsolutePath(), 
			loginUrl, 
			url, 
			apiUser, 
			apiPass, 
			outputFile.getAbsolutePath());
		
		scriptFile.delete();
		
		return outputFile;
		
	}

}
