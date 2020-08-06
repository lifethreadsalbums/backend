package com.poweredbypace.pace.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.event.ProgressEvent;
import com.amazonaws.event.ProgressListener;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.Download;
import com.amazonaws.services.s3.transfer.Transfer;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.dto.FileInfo;
import com.poweredbypace.pace.dto.FileMetadata;
import com.poweredbypace.pace.exception.FileNotFoundException;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.service.StorageService;

@Service
public class S3StorageService implements StorageService {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Value("${aws.accessKey}")
	private String accessKey;
	
	@Value("${aws.secretKey}")
	private String secretKey;
	
	@Value("${s3.bucket}")
	private String bucketName = "irisstudio";
	
	private CannedAccessControlList publicReadAcl = CannedAccessControlList.PublicRead;
	private AmazonS3Client amazonS3Client;
	private TransferManager uploadTx;
	private TransferManager downloadTx;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	@PostConstruct
	private void initializeS3()
	{
		amazonS3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
		uploadTx = new TransferManager(amazonS3Client);
		downloadTx = new TransferManager(amazonS3Client);
	}
	
	public List<FileInfo> listFiles(String prefix) {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
    		.withBucketName(bucketName)
    		.withPrefix(prefix);
		ObjectListing objectListing;
		
		List<FileInfo> result = new ArrayList<FileInfo>();
		do {
	        objectListing = amazonS3Client.listObjects(listObjectsRequest);
	        for (S3ObjectSummary objectSummary : 
	            objectListing.getObjectSummaries()) {
	        	
	        	String name = objectSummary.getKey();
	        	FileInfo fi = new FileInfo();
	        	fi.setName(name);
	        	fi.setLastModified(objectSummary.getLastModified());
	        	fi.setSize(objectSummary.getSize());
	        	log.debug("key = "+name);
	        	result.add(fi);
	        }
	        listObjectsRequest.setMarker(objectListing.getNextMarker());
		} while (objectListing.isTruncated());
		return result;
	}
	
	private void waitForCompletion(Transfer transfer) {
		try {
			transfer.waitForCompletion();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void putFile(File file, String path) {
		Upload myUpload = uploadTx.upload(new PutObjectRequest(bucketName, path, file)
			.withCannedAcl(publicReadAcl));
		waitForCompletion(myUpload);
	}
	
	@Override
	public void putFile(File file, String path, String contentDisposition) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentDisposition(contentDisposition);
		Upload myUpload = uploadTx.upload(new PutObjectRequest(bucketName, path, file)
			.withCannedAcl(publicReadAcl)
			.withMetadata(metadata));
		waitForCompletion(myUpload);
	}
	
	@Override
	public void putFile(File file, String path, FileMetadata meta) {
		ObjectMetadata metadata = new ObjectMetadata();
		if (meta.getContentType()!=null) {
			metadata.setContentType(meta.getContentType());
		}
		if (meta.getContentDisposition()!=null) {
			metadata.setContentDisposition(meta.getContentDisposition());
		}
		Upload myUpload = uploadTx.upload(new PutObjectRequest(bucketName, path, file)
			.withCannedAcl(publicReadAcl)
			.withMetadata(metadata));
		waitForCompletion(myUpload);
	}
	
	@Override
	public void putFile(File file, String path, String contentDisposition, final JobProgressInfo progressInfo) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentDisposition(contentDisposition);
		
		final Upload myUpload = uploadTx.upload(new PutObjectRequest(bucketName, path, file)
			.withCannedAcl(publicReadAcl)
			.withMetadata(metadata));
		myUpload.addProgressListener(new ProgressListener() {
			private int lastProgress = 0;
			
			@Override
			public void progressChanged(ProgressEvent progressEvent) {
				if (progressInfo!=null) {
					int progress = (int)myUpload.getProgress().getPercentTransferred();
					
					if (progress>lastProgress) {
						lastProgress = progress;
						progressInfo.setProgressPercent(progress);
						notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, progressInfo));
					}
				}
			}
		});
		waitForCompletion(myUpload);
	}
	
	@Override
	public void putFile(File file, String path, String contentDisposition, 
			final com.poweredbypace.pace.event.ProgressListener progressListener) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentDisposition(contentDisposition);
		
		final Upload myUpload = uploadTx.upload(new PutObjectRequest(bucketName, path, file)
			.withCannedAcl(publicReadAcl)
			.withMetadata(metadata));
		if (progressListener!=null) {
			myUpload.addProgressListener(new ProgressListener() {
				private int lastProgress = 0;
				
				@Override
				public void progressChanged(ProgressEvent progressEvent) {
					int progress = (int)myUpload.getProgress().getPercentTransferred();
						
					if (progress>lastProgress) {
						lastProgress = progress;
						progressListener.progressChanged(progress);
							
					}
				}
			});
		}
		waitForCompletion(myUpload);
	}
	
	@Override
	public File getFile(String path)
	{
		File file = null;
		
		try {
			file = File.createTempFile("pace-s3file-", FilenameUtils.getName(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		file.deleteOnExit();
		
		try {
			Download download = downloadTx.download(bucketName, path, file);
			waitForCompletion(download);
		} catch (AmazonS3Exception ex) {
			if (ex.getStatusCode()==404) {
				throw new FileNotFoundException("Cannot download file "+path + " from " + bucketName, ex);
			}
			throw ex;
		}
		
		return file;
	}
	
	@Override
	public void copyFile(String from, String to) {
		amazonS3Client
			.copyObject(new CopyObjectRequest(bucketName, from, bucketName, to)
			.withCannedAccessControlList(publicReadAcl));
	}

	@Override
	public void moveFile(String from, String to) {
		copyFile(from, to);
		deleteFile(from);
	}

	@Override
	public void deleteFile(String path) {
		amazonS3Client.deleteObject(bucketName, path);
	}
}
