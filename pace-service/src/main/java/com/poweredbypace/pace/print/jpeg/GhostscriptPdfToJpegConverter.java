package com.poweredbypace.pace.print.jpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.exception.JpegGenerationException;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.print.JobProgressMonitor;
import com.poweredbypace.pace.print.OutputType;
import com.poweredbypace.pace.service.IccProfileService;
import com.poweredbypace.pace.util.PaceFileUtils;
import com.poweredbypace.pace.util.ProcessUtils;

public class GhostscriptPdfToJpegConverter {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired(required=false)
	public IccProfileService iccService;
	
	private void monitorProcessOutput(Process pr, JobProgressMonitor progressMonitor) throws NumberFormatException, IOException {
		BufferedReader in = new BufferedReader(
			new InputStreamReader(pr.getInputStream()));
		String currentLine = null;
		int totalPages = 0;
		int currentPage = 0;
		Pattern totalPagesPattern = Pattern.compile("Processing pages \\d+ through (\\d+)");
		Pattern currentPagePattern = Pattern.compile("Page (\\d+)");
        
		while ((currentLine = in.readLine()) != null) {
			Matcher totalPagesMatcher = totalPagesPattern.matcher(currentLine);
			Matcher currentPageMatcher = currentPagePattern.matcher(currentLine);
	        if (totalPagesMatcher.find()) {
	        	String grp = totalPagesMatcher.group(1);
	        	totalPages = Integer.parseInt(grp);
	        	logger.debug("total pages="+totalPages);
	        } else if (currentPageMatcher.find()) {
	        	String grp = currentPageMatcher.group(1);
	        	currentPage = Integer.parseInt(grp);
	        	logger.debug("current page="+currentPage);
	        	if (progressMonitor!=null && totalPages>0) {
	        		progressMonitor.progress( Math.round((float) currentPage / (float) totalPages * 100.0f) );
	        	}
	        } else {
	        	logger.debug(currentLine);
	        }
		}
	}
	
	public File convert(File pdf, IccProfile iccProfile, OutputType outputType, 
			String spreadFilename, boolean zip, JobProgressMonitor progressMonitor)
			throws InterruptedException, PrintGenerationException {
		
		try {
			String iccProfilePath = iccService.getProfilesPath() + "/" + iccProfile.getProfile();
			
			File outputDir = PaceFileUtils.createTempDir();
			String ext = outputType==OutputType.Jpeg ? "jpg" : "tif";
	    	String dev = outputType==OutputType.Jpeg ? "jpeg" : "tiff24nc";
	    	
	    	if (iccProfile.getColorSpace()==ColorSpace.Cmyk && outputType==OutputType.Tiff) {
	    		dev = "tiff32nc";
	    	}
			
			String outputFileName = outputDir.getAbsolutePath() + "/" + spreadFilename + "_%03d." + ext;
			ProcessBuilder pb = null;
			
			if (outputType==OutputType.Tiff) {
				pb = new ProcessBuilder("gs",
					"-dBATCH", 
					"-dNOPAUSE", 
					"-sOutputFile=" + outputFileName,
					"-sDEVICE=" + dev,
					"-dDOINTERPOLATE",
					"-dTextAlphaBits=4",
					"-dGraphicsAlphaBits=4",
					"-sOutputICCProfile=" + iccProfilePath,
					"-r300x300",
					"-f",
					pdf.getAbsolutePath());
			} else {
				pb = new ProcessBuilder("gs",
					"-dBATCH", 
					"-dNOPAUSE", 
					"-sOutputFile=" + outputFileName,
					"-sDEVICE=" + dev,
					"-dJPEGQ=100",
					"-dDOINTERPOLATE",
					"-dTextAlphaBits=4",
					"-dGraphicsAlphaBits=4",
					"-sOutputICCProfile=" + iccProfilePath,
					"-r300x300",
					"-f",
					pdf.getAbsolutePath());
			}
			
			String execPath = "/usr/bin:/usr/local/bin:/opt/local/bin";
			String oldPath = pb.environment().get("PATH");
			String newPath = execPath + ":" + oldPath;
			pb.environment().put("PATH", newPath);
			
			pb.redirectErrorStream(true);
			final Process pr = pb.start();
			
			monitorProcessOutput(pr, null);
			
			int result = pr.waitFor();
			if (result!=0) 
				throw new JpegGenerationException("Error while converting PDF to JPEG, exit code=" + Integer.toString(result));
			
			if (zip) {
				//zip files
				File zipFile = File.createTempFile("spread-zip-", ".zip");
				zipFile.delete();
				ProcessUtils.exec("/usr/bin/zip", "-0", "-r", "-j", zipFile.getAbsolutePath(), outputDir.getAbsolutePath());
				
				outputDir.delete();
				return zipFile;
			} else {
				return outputDir;
			}
			
		} catch (IOException e) {
			throw new JpegGenerationException(e);
		}
	}

}
