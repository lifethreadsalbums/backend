package com.poweredbypace.pace.print.jpeg;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gm4java.engine.GMException;
import org.gm4java.engine.GMServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.domain.IccProfile.ColorSpace;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.TextStampElement;
import com.poweredbypace.pace.event.ProgressListener;
import com.poweredbypace.pace.exception.JpegGenerationException;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.print.JobProgressMonitor;
import com.poweredbypace.pace.print.LayoutPrintGenerator;
import com.poweredbypace.pace.print.OutputType;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.IccProfileService;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.util.PACEUtils;
import com.poweredbypace.pace.util.PaceFileUtils;
import com.poweredbypace.pace.util.ProcessUtils;

public class GsJpegSpreadGenerator implements LayoutPrintGenerator {
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Autowired(required=false)
	@Qualifier("pdfGenerator")
	public LayoutPrintGenerator pdfGenerator;
	
	@Autowired(required=false)
	public IccProfileService iccService;
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	@Autowired
	private ImageService imageService;
	
	private OutputType outputType = OutputType.Jpeg;
	private boolean zipSpreads = true;
	
	public void setOutputType(OutputType output) {
		this.outputType = output;
	}
	
	public void setZipSpreads(boolean zipSpreads) {
		this.zipSpreads = zipSpreads;
	}

	@Override
	public File generateAlbum(Product product, ProgressListener progressListener)
		throws InterruptedException, PrintGenerationException {
		File pdf = pdfGenerator.generateAlbum(product, progressListener);
		return convert(pdf, product, progressListener, false);
	}
	
	@Override
	public File generateCover(Product product, ProgressListener progressListener)
		throws InterruptedException, PrintGenerationException {
		File pdf = pdfGenerator.generateCover(product, progressListener);
		return convert(pdf, product, progressListener, true);
	}

	@Override
	public File generateCameos(Product product, ProgressListener progressListener)
		throws InterruptedException, PrintGenerationException {
		File pdf = pdfGenerator.generateCameos(product, progressListener);
		return convert(pdf, product, progressListener, false);
	}
	
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
	        } else {
	        	logger.debug(currentLine);
	        }
		}
	}
	
	@Override
	public File generateAlbumPreview(Product product, ProgressListener progressListener)
			throws InterruptedException, PrintGenerationException {
		return null;
	}
	
	private File convert(File pdf, Product product, ProgressListener progressListener, boolean cover)
			throws InterruptedException, PrintGenerationException {
		
		try {
			IccProfile iccProfile = iccService.getIccProfile(product);
			String iccProfilePath = iccService.getProfilesPath() + "/" + iccProfile.getProfile();
			
			File outputDir = PaceFileUtils.createTempDir();
			String spreadFilename = "spread";
			
			ProductContext ctx = new ProductContext(product);
			GenericRule rule = ruleService.findRule(ctx, "SPREAD_FILENAME");
			if (rule!=null) {
				spreadFilename = expressionEvaluator.evaluate(ctx, rule.getJsonData(), String.class);				
			} else {
				logger.warn("Cannot find SPREAD_FILENAME rule, using default spread name.");
			}
	    	
	    	String ext = this.outputType==OutputType.Jpeg ? "jpg" : "tif";
	    	String dev = this.outputType==OutputType.Jpeg ? "jpeg" : "tiff24nc";
	    	
	    	if (iccProfile.getColorSpace()==ColorSpace.Cmyk && this.outputType==OutputType.Tiff) {
	    		dev = "tiff32nc";
	    	}
			
			String outputFileName = outputDir.getAbsolutePath() + "/" + spreadFilename + "_%03d." + ext;
			if (product.isReprint() || cover) {
				outputFileName = outputFileName + "_tmp";
			}
			
			ProcessBuilder pb = null;
			
			if (this.outputType==OutputType.Tiff) {
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
			
			//rename reprint pages
			if (product.isReprint()) {
				List<Integer> pageNumbers = PACEUtils.getReprintPages(product);
				String format = outputDir.getAbsolutePath() + "/" + spreadFilename + "_%03d." + ext;
				for(int i=0;i<pageNumbers.size();i++) {
					String spreadName = String.format(format + "_tmp", i + 1);
					String spreadName2 = String.format(format, pageNumbers.get(i));
					FileUtils.moveFile(
						new File(spreadName),
						new File(spreadName2));
				}
			} 
			if (cover) {
				for(int i=0;i<2;i++) {
					String format = outputDir.getAbsolutePath() + "/" + spreadFilename + "_%03d." + ext;
					String format2 = outputDir.getAbsolutePath() + "/" + spreadFilename + "_COVER_%03d." + ext;
					String spreadName = String.format(format + "_tmp", i + 1);
					String spreadName2 = String.format(format2, i);
					try {
						FileUtils.moveFile(
							new File(spreadName),
							new File(spreadName2));
					} catch (Exception ex) {}
				}
			}
			
			pdf.delete();
			
			if (this.zipSpreads) {
				//zip files
				File zip = File.createTempFile("spread-zip-", ".zip");
				zip.delete();
				ProcessUtils.exec("/usr/bin/zip", "-0", "-r", "-j", zip.getAbsolutePath(), outputDir.getAbsolutePath());
				
				outputDir.delete();
				return zip;
			} else {
				return outputDir;
			}
			
		} catch (IOException e) {
			throw new PrintGenerationException(e);
		}
	}

	@Override
	public File generateDie(Product product, TextStampElement element,
			ProgressListener progressListener) throws InterruptedException,
			PrintGenerationException {
		
		File pdf = pdfGenerator.generateDie(product, element, progressListener);
		try {
			File tempPng = File.createTempFile("pace-die-", ".png");
			ProcessUtils.exec("gs", "-dNOPAUSE", "-dBATCH", "-sDEVICE=pngalpha", "-r600",
				"-sOutputFile=" + tempPng.getAbsolutePath(), pdf.getAbsolutePath());
			
			File resultPng = File.createTempFile("pace-die-", ".png");
			imageService.getGMService().execute("convert", tempPng.getAbsolutePath(), "-trim", "+repage", resultPng.getAbsolutePath());
					
			pdf.delete();
			return resultPng;
		} catch(IOException e) {
			throw new PrintGenerationException(e);
		} catch (GMException e) {
			throw new PrintGenerationException(e);
		} catch (GMServiceException e) {
			throw new PrintGenerationException(e);
		}
	}

}
