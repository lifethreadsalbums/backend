package com.poweredbypace.pace.service;

import java.io.File;
import java.util.List;

import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.dto.FileInfo;
import com.poweredbypace.pace.dto.FileMetadata;
import com.poweredbypace.pace.event.ProgressListener;

public interface StorageService {

	void putFile(File file, String path);
	
	void putFile(File file, String path, String contentDisposition);
	
	void putFile(File file, String path, String contentDisposition, JobProgressInfo progress);
	
	void putFile(File file, String path, String contentDisposition, final ProgressListener progressListener);
	
	void putFile(File file, String path, FileMetadata meta);
	
	File getFile(String path);
	
	void copyFile(String from, String to);
	
	void moveFile(String from, String top);
	
	void deleteFile(String path);
	
	List<FileInfo> listFiles(String prefix);
	
}