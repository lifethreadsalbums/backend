package com.poweredbypace.pace.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.Report;
import com.poweredbypace.pace.domain.Report.ReportParameter;
import com.poweredbypace.pace.repository.ReportRepository;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

@Controller
public class ReportController {
	
	private Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private ReportRepository reportRepo;
	
	@Autowired
	private DataSource ds;
	
	@RequestMapping(value = "/api/report", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Report> getAll() {
		return reportRepo.findByParentIsNull();
	}
	
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public String generateReport(
			@RequestParam("id") String id, 
			HttpServletRequest request,
			HttpServletResponse response) throws SQLException {
	 
		Connection conn = null;
	    try {
	    	Report report = reportRepo.findByCode(id);
	    	conn = ds.getConnection();
	    	
	        Map<String,Object> params = new HashMap<String,Object>();
	        for(ReportParameter p:report.getParams()) {
	        	params.put(p.getName(), p.valueOf(request.getParameter(p.getName())));
	        }
	        
	        //compile subreports
	        for(Report subreport:report.getSubreports()) {
	        	JasperReport jasperSubreport = JasperCompileManager.compileReport(
	        		IOUtils.toInputStream(subreport.getSource()));
	        	params.put(subreport.getCode(), jasperSubreport);
	        }
	         
	        JasperReport jasperReport = JasperCompileManager.compileReport(IOUtils.toInputStream(report.getSource()));
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, conn);
	        generateReportExcel(report, params, jasperPrint, response);
	 
	    } catch (Exception ex) {
	    	log.error("Error while generating report", ex);
        } finally {
            if (conn != null) conn.close();
        }
	 
	   	return null;
	}
	 
    @SuppressWarnings("unused")
	private void generateReportHtml( JasperPrint jasperPrint, HttpServletRequest req, HttpServletResponse resp) throws IOException, JRException {
    	resp.setContentType("text/html;charset=UTF-8");
    	HtmlExporter exporter = new HtmlExporter();
        exporter.setExporterInput( new SimpleExporterInput(jasperPrint) );
        exporter.setExporterOutput( new SimpleHtmlExporterOutput(resp.getWriter()) );
        SimpleHtmlReportConfiguration configuration = new SimpleHtmlReportConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }
    
    private void generateReportExcel(Report report, Map<String,Object> params, JasperPrint jasperPrint, HttpServletResponse resp) throws JRException, IOException {
    	
    	resp.reset();
        resp.resetBuffer();
        resp.setContentType("application/vnd.ms-excel");
        
        String filename = report.getFilename();
        
        for(ReportParameter p:report.getParams()) {
        	filename += "-" + params.get(p.getName());
        }
        filename += ".xls";
        resp.setHeader("Content-Disposition", "attachment; filename="+filename);
        
    	ServletOutputStream outputStream = resp.getOutputStream();
    	JRXlsExporter xlsExporter = new JRXlsExporter();
    	
    	xlsExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
    	xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
    	
    	SimpleXlsReportConfiguration config = new SimpleXlsReportConfiguration();
    	config.setOnePagePerSheet( report.getSubreports().size()>0 );
    	config.setCollapseRowSpan(false);
    	config.setShowGridLines(true);
    	config.setRemoveEmptySpaceBetweenRows(true);
    	config.setWhitePageBackground(false);
    	config.setWrapText(false);
    	
    	//config.setShrinkToFit(true);
    	//config.setDetectCellType(true);
    	
    	if (report.getSheetNames()!=null) {
	    	String[] sheetNames = report.getSheetNames().split("\\,");
	    	config.setSheetNames(sheetNames);
    	}
    	
    	xlsExporter.setConfiguration(config);
    	xlsExporter.exportReport();
    	
    	outputStream.flush();
        outputStream.close();
    }
 
    @SuppressWarnings("unused")
	private void generateReportPDF (HttpServletResponse resp, Map<String, Object> parameters, JasperReport jasperReport, Connection conn)throws JRException, NamingException, SQLException, IOException {
        byte[] bytes = null;
        bytes = JasperRunManager.runReportToPdf(jasperReport,parameters,conn);
        resp.reset();
        resp.resetBuffer();
        resp.setContentType("application/pdf");
        resp.setContentLength(bytes.length);
        ServletOutputStream ouputStream = resp.getOutputStream();
        ouputStream.write(bytes, 0, bytes.length);
        ouputStream.flush();
        ouputStream.close();
    } 
 	

}
