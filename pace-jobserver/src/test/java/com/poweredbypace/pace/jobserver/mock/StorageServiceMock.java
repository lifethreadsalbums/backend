package com.poweredbypace.pace.jobserver.mock;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.dto.FileInfo;
import com.poweredbypace.pace.dto.FileMetadata;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.service.StorageService;

public class StorageServiceMock implements StorageService {
	
	private static final Map<String, File> map = new HashMap<String, File>();
	
	static {
		for(String path: Data.IMAGE_PATHS) {
			URL url = StorageServiceMock.class.getResource(path);
			try {
				map.put(path, new File(url.toURI().getRawPath()));
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void putFile(File file, String path) {
		try {
			File temp = File.createTempFile("storagetemp", "");
			temp.deleteOnExit();
			Files.copy(file, temp);
			map.put(path, temp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public File getFile(String path) {
		return map.get(path);
	}
	
	@Override
	public String toString() {
		return map.toString();
	}
	
	public File getFirstLowres() {
		for(String path: map.keySet()) {
			if(path.contains("images/lowres")) {
				return map.get(path);
			}
		}
		return null;
	}
	
	public File getFirstThumbnail() {
		for(String path: map.keySet()) {
			if(path.contains("images/thumbnail")) {
				return map.get(path);
			}
		}
		return null;
	}
	
	public Map<String, File> getMap() {
		return map;
	}

	@Override
	public void putFile(File file, String path, String contentDisposition,
			JobProgressInfo progress) {
	}

	@Override
	public void putFile(File file, String path, String contentDisposition) {
	}

	@Override
	public void moveFile(String from, String top) {
		
	}

	@Override
	public void deleteFile(String path) {
		
	}

	@Override
	public void copyFile(String from, String to) {
		
	}

	@Override
	public void putFile(File file, String path, String contentDisposition,
			ProgressListener progressListener) {
		
	}

	@Override
	public List<FileInfo> listFiles(String prefix) {
		return null;
	}

	@Override
	public void putFile(File file, String path, FileMetadata meta) {
		
	}

}