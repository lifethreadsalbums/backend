package com.poweredbypace.pace.service.impl;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.ProductOption;
import com.poweredbypace.pace.domain.layout.ImageStampElement;
import com.poweredbypace.pace.domain.layout.TextStampElement;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.print.LayoutPrintGenerator;
import com.poweredbypace.pace.service.DieService;
import com.poweredbypace.pace.service.ImageService;
import com.poweredbypace.pace.service.ScreenshotService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.util.HibernateUtil;

@Service
public class DieServiceImpl implements DieService {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired(required=false)
	private ScreenshotService screenshotService;
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private ImageService imageService;
	
	@Autowired(required=false)
	@Qualifier("jpegGenerator")
	public LayoutPrintGenerator jpegGenerator;
	
	@Autowired
	private Env env;

	@Override
	public File generateDie(Product product, String optionCode) throws PrintGenerationException {
		
		ProductOption<?> po = product.getProductOptionByCode(optionCode);
		if (po==null) return null;
		
		Object value = HibernateUtil.unproxy(po.getValue());
		if (value instanceof ImageStampElement ||
			value instanceof TextStampElement) {
			
			if (log.isDebugEnabled())
				log.debug("Generating DIE for " + product.getName());
			
			try {
				File dieFile = null;
				if (value instanceof ImageStampElement) {
					ImageStampElement el = (ImageStampElement) value;
					
					File originalFile = storageService.getFile(el.getImageFile().getOriginalImageUrl());
					
					//gm convert customdie.png -density 300x300 -units PixelsPerInch -resample 600x600 -resize 2400 out.png
					//TODO: DPI company specific
					int dpi = 600;
					int width = Math.round(el.getWidth() / ApplicationConstants.PPI * dpi);
					int height = Math.round(el.getHeight() / ApplicationConstants.PPI * dpi);
					
					String ext = ".png";
					
					dieFile = File.createTempFile("pace-die-", ext);
					imageService.resize(originalFile, dieFile, width, height, dpi);
					originalFile.delete();
				} else {
					TextStampElement el = (TextStampElement) value;
					dieFile = jpegGenerator.generateDie(product, el, null);
				}
				
				return dieFile;
				
				
			} catch(Exception ex ) {
				throw new PrintGenerationException("Cannot generate die file", ex);
			}
		}
		return null;
	}

	@Override
	public File generateDieScreenshot(Product product, String optionCode) throws PrintGenerationException {
		ProductOption<?> po = product.getProductOptionByCode(optionCode);
		if (po==null) return null;
		
		//make screenshot
		String section = po.getPrototypeProductOption().getEffectiveGroup().getUrl() +
			"/" + po.getPrototypeProductOption().getUrl();
		String url = "https://"+env.getStore().getDomainName() + "/#/build/" + 
			product.getId() + "/" + section + "?screenshot=true";
		File screenshot;
		try {
			screenshot = screenshotService.screenshot(url);
		} catch (Exception e) {
			throw new PrintGenerationException("Cannot generate die screenshot", e);
		} 
		return screenshot;
	}

}
