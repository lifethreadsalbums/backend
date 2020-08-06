package com.poweredbypace.pace.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import com.poweredbypace.pace.util.ProcessUtils;

public class FreeMarkerViewWithPdfSupport extends FreeMarkerView {

	public FreeMarkerViewWithPdfSupport() { }
	
	@Override
	protected void renderMergedTemplateModel(
			Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String uri = request.getRequestURI();
		if (uri.toLowerCase().endsWith(".pdf")) {
			//convert to pdf
			MockHttpServletResponse mockResp = new MockHttpServletResponse();
			super.renderMergedTemplateModel(model, request, mockResp);
			
			File htmlFile = File.createTempFile("pace-html-", ".html");
			FileOutputStream fos = new FileOutputStream(htmlFile);
			fos.write(mockResp.getContentAsByteArray());
			fos.close();
			
			InputStream is = getClass().getResourceAsStream("/scripts/rasterize.js");
			
			File scriptFile = File.createTempFile("pace-js-",".js");
		    IOUtils.copy(is, FileUtils.openOutputStream(scriptFile));
		    String scriptPath = scriptFile.getAbsolutePath();
			File pdf = File.createTempFile("pace-pdf-", ".pdf");
			
			ProcessUtils.exec("phantomjs", scriptPath, htmlFile.getAbsolutePath(), pdf.getAbsolutePath(), "8.5in*11in");
			
			response.setContentType("application/pdf");
			if (model.containsKey("filename")) {
				response.setHeader("Content-Disposition", "inline; filename=\"" + model.get("filename") + "\"");
			}
			FileCopyUtils.copy(new FileInputStream(pdf), response.getOutputStream());
			
			pdf.delete();
			htmlFile.delete();
			scriptFile.delete();
		} else {
			super.renderMergedTemplateModel(model, request, response);
		}
	}
	
}
