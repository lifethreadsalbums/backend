package com.poweredbypace.pace.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Currency;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.manager.CurrencyManager;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.util.ProcessUtils;

@Controller
public class DownloadController {
	
	@Autowired
	private StorageService storageService;
	
	@Autowired
	private CurrencyManager currencyManager;
	
	@RequestMapping(value = "/download/price-list", method = RequestMethod.GET)
	public void downloadPriceList(@AuthenticationPrincipal User user, 
			HttpServletRequest request,  HttpServletResponse response) throws FileNotFoundException, IOException {
		
		Currency currency = currencyManager.getCurrency(user);
		
		String path = "content/Prices_2016_" + currency.getCurrencyCode() + ".pdf";
		File file = storageService.getFile(path);
		
		response.setContentType("application/pdf");
		response.setContentLength((int)file.length());
        response.setHeader("Content-Disposition", "inline; filename=price-list.pdf;");
        
        FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
	}

	@RequestMapping(value = "/api/html-to-pdf", method = RequestMethod.POST)
	public void convertToPdf(@RequestBody String html, 
			HttpServletRequest request,  HttpServletResponse response) throws FileNotFoundException, IOException, URISyntaxException, InterruptedException {
		
		File htmlFile = File.createTempFile("pace-html-", ".html");
		FileOutputStream fos = new FileOutputStream(htmlFile);
		fos.write(html.getBytes());
		fos.close();
		
		String scriptPath = getClass().getResource("/scripts/rasterize.js").toURI().getRawPath();
		File pdf = File.createTempFile("pace-pdf-", ".pdf");
		
		ProcessUtils.exec("phantomjs", scriptPath, htmlFile.getAbsolutePath(), pdf.getAbsolutePath(), "8.5in*11in");
		
		response.setContentType("application/pdf");
		FileCopyUtils.copy(new FileInputStream(pdf), response.getOutputStream());
		
		pdf.delete();
		htmlFile.delete();
		
	}

	

}
