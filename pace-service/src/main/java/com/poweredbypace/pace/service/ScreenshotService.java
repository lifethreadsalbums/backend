package com.poweredbypace.pace.service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public interface ScreenshotService {
	
	File screenshot(String url) throws IOException, InterruptedException, URISyntaxException;
	
}
