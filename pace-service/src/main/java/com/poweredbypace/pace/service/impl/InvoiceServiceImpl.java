package com.poweredbypace.pace.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.mail.EmailTemplate;
import com.poweredbypace.pace.domain.order.Invoice;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.InvoiceContext;
import com.poweredbypace.pace.repository.EmailTemplateRepository;
import com.poweredbypace.pace.repository.InvoiceRepository;
import com.poweredbypace.pace.service.EmailService;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.InvoiceService;
import com.poweredbypace.pace.util.ProcessUtils;

import freemarker.template.Configuration;

/***
 * Service for generating and emailing invoices
 *
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {
	
	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private InvoiceRepository invoiceRepo;
	
	@Autowired
	private EmailTemplateRepository emailTemplateRepo;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private Configuration freemarkerConfig;
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	/***
	 * Creates and saves an {@link Invoice} entity for the given {@link Order} 
	 * @param order the {@link Order} instance to create the invoice for
	 * @return a reference to the {@link Invoice} entity associated with the given order.
	 */
	@Override
	public Invoice create(Order order) {
		
		Invoice invoice = invoiceRepo.findByOrder(order);
		if (invoice==null) {
			invoice = new Invoice();
			invoice.setDateCreated(new Date());
		}
		invoice.setOrder(order);
		invoice.setInvoiceNumber(order.getOrderNumber());
		
		invoiceRepo.save(invoice);
		return invoice;
	}
	
	/***
	 * Generates an invoice PDF for the given {@link Order} instance
	 * @param order the {@link Order} instance to generate the PDF for
	 * @return a reference to the invoice PDF file 
	 */
	@Override
	public File generateInvoice(Order order) {
		try {
			Invoice invoice = invoiceRepo.findByOrder(order);
			EmailTemplate template = emailTemplateRepo.findByName("INVOICE");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("invoice", invoice);
			
			String invoiceHtml = FreeMarkerTemplateUtils.processTemplateIntoString(
					freemarkerConfig.getTemplate(template.getName()), model);
			
			File htmlFile = File.createTempFile("pace-html-", ".html");
			FileUtils.writeStringToFile(htmlFile, invoiceHtml);
			
			InputStream is = getClass().getResourceAsStream("/scripts/rasterize.js");
			
			File scriptFile = File.createTempFile("pace-js-",".js");
		    IOUtils.copy(is, FileUtils.openOutputStream(scriptFile));
		    String scriptPath = scriptFile.getAbsolutePath();
			
			File pdf = File.createTempFile("pace-pdf-", ".pdf");
			ProcessUtils.exec("phantomjs", scriptPath, htmlFile.getAbsolutePath(), pdf.getAbsolutePath(), "8.5in*11in");
			htmlFile.delete();
			scriptFile.delete();
			
			return pdf;
		} catch(Exception ex) {
			log.error("Error while generating invoice", ex);
			return null;
		}
	}
	
	/***
	 * Emails the invoice to End User and Admin.
	 * End User receives an HTML invoice. Admin receives an HTML invoice with PDF attached. 
	 * @param invoice the {@link Invoice} to be sent
	 * @param emailToAdmin whether to send Admin an email with the invoice 
	 * @param emailToUser  whether to send End User an email with the invoice 
	 */
	private void emailInvoice(Invoice invoice, boolean emailToAdmin, boolean emailToUser) {
		try {
			EmailTemplate template = emailTemplateRepo.findByName("INVOICE");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("invoice", invoice);
			
			String invoiceHtml = FreeMarkerTemplateUtils.processTemplateIntoString(
					freemarkerConfig.getTemplate(template.getName()), model);
			
			File htmlFile = File.createTempFile("pace-html-", ".html");
			FileUtils.writeStringToFile(htmlFile, invoiceHtml);
			
			InputStream is = getClass().getResourceAsStream("/scripts/rasterize.js");
			
			File scriptFile = File.createTempFile("pace-js-",".js");
		    IOUtils.copy(is, FileUtils.openOutputStream(scriptFile));
		    String scriptPath = scriptFile.getAbsolutePath();
			
			File pdf = File.createTempFile("pace-invoice-", ".pdf");
			ProcessUtils.exec("phantomjs", scriptPath, htmlFile.getAbsolutePath(), pdf.getAbsolutePath(), "8.5in*11in");
			htmlFile.delete();
			scriptFile.delete();
			
			//email to admin
			if (emailToAdmin) {
				String filename = "invoice-" + invoice.getInvoiceNumber() + ".pdf";
				try {
					GenericRule rule = ruleService.findRule("INVOICE_PDF_NAME");
					if (rule!=null) {
						filename = expressionEvaluator.evaluate(
							new InvoiceContext(invoice), rule.getJsonData(), String.class);
					}
					
				} catch(Exception ex) {
					log.warn("Cannot eval INVOICE_PDF_NAME. " + ex.getMessage());
				}
				
				Map<String, File> attachments = new HashMap<String, File>();
				attachments.put(filename, pdf);
				emailService.send(template, model, attachments);
			}
			
			if (emailToUser) {
				emailService.send(invoice.getOrder().getUser().getEmail(), template, model);
			}
			
		} catch(Exception ex) {
			log.error("Error while sending invoice", ex);
		}
	}

	/***
	 * Emails an HTML to End User.
	 * @param invoice the {@link Invoice} to be sent
	 */
	@Override
	public void emailInvoice(Invoice invoice) { 
		emailInvoice(invoice, true, true);
	}
	
	/***
	 * Emails an HTML to Admin.
	 * @param invoice the {@link Invoice} to be sent
	 */
	@Override
	public void emailInvoiceToAdmin(Invoice invoice) {
		emailInvoice(invoice, true, false);
	}
}
