package com.poweredbypace.pace.print.productionsheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Batch.BatchState;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.mail.EmailTemplate;
import com.poweredbypace.pace.domain.order.Invoice;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.exception.PdfGenerationException;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.JavaScriptExpressionEvaluator;
import com.poweredbypace.pace.hemlock.domain.BatchItem;
import com.poweredbypace.pace.irisbook.IrisProduct;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.print.productionsheet.AbstractProductionSheetRenderer.Margins;
import com.poweredbypace.pace.repository.BatchRepository;
import com.poweredbypace.pace.repository.EmailTemplateRepository;
import com.poweredbypace.pace.repository.InvoiceRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.EmailService;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.ProductionSheetService;
import com.poweredbypace.pace.service.StorageService;
import com.poweredbypace.pace.util.StringUtils;

@Service
public class ProductionSheetsServiceImpl implements ProductionSheetService {
	
	private final Log logger = LogFactory.getLog(getClass());
	

	@Autowired
	private StorageService storageService;

	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	private ExpressionEvaluator evaluator = new JavaScriptExpressionEvaluator();
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Autowired
	private EmailTemplateRepository emailTemplateRepo;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private BatchRepository batchRepo;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private InvoiceRepository invoiceRepo;
	
	
	public ProductionSheetsServiceImpl() { }
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void generate(List<Long> batchIds, JobProgressInfo jobInfo)
			throws PdfGenerationException, InterruptedException {
		
		
		List<Batch> batches = batchRepo.findAll(batchIds);
		
		String batchNumbers = "";
		for(Batch batch:batches) {
			batchNumbers += batchNumbers.equals("") ? batch.getName() : ","+batch.getName();
		}
		
		jobInfo.setJobName(String.format("Generating Production Sheets for %s", batchNumbers));
		jobInfo.setProgressPercent(0);
		notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
		
		try {
			
			List<BatchItem> shippingRows = new ArrayList<BatchItem>();
			List<BatchItem> sewingRows = new ArrayList<BatchItem>();
			
			for(Batch batch:batches) {
				List<Product> products = batch.getProducts();
				if (batch.getState()==BatchState.Queued) {
					ProductState[] states = {ProductState.Printing, ProductState.Printed};
					products = productRepo.findByProductStatesAndParentIsNull(states);
				}
				
				for(Product p:products) {
					//for(Product p:product.getProductAndChildren()) {
						IrisProduct irisProduct = new IrisProduct(p);
						BatchItem row = new BatchItem(p, evaluator, ruleService);
						
						OrderItem oi = p.getOrderItem();
						if (p.getParent()!=null)
							oi = p.getParent().getOrderItem();
						
						if (oi!=null) {
							Invoice invoice = invoiceRepo.findByOrder(oi.getOrder());
							row.setInvoice(invoice);
						}
						
						row.setBatch(batch);
						
						if (!p.isReprint() && !(irisProduct.isTS() && p.isDuplicate())) {
							for(int i=0;i<row.getNumSets();i++)
								shippingRows.add(row);
						}
						
						if (!irisProduct.isNoPrinting()) {
							for(int i=0;i<row.getNumSets();i++)
								sewingRows.add(row);
						}
					//}
				}
			}
			
			Collections.sort(shippingRows, new Comparator<BatchItem>() {
				@Override
				public int compare(BatchItem o1, BatchItem o2) {
					String s1 = o1.getProduct().getProductNumber();
					String s2 = o2.getProduct().getProductNumber();
					return StringUtils.alphanumCompare(s1,s2);
				}
			});
			
			Collections.sort(sewingRows, new Comparator<BatchItem>() {
				@Override
				public int compare(BatchItem o1, BatchItem o2) {
					String s1 = o1.getGroup() + "-" + o1.getProduct().getProductNumber();
					String s2 = o2.getGroup() + "-" + o2.getProduct().getProductNumber();
					return StringUtils.alphanumCompare(s1,s2);
				}
			});
			
			File sewingFile = File.createTempFile("production-sheet-", ".pdf");
			File shippingFile = File.createTempFile("production-sheet-", ".pdf");
			
			if (shippingRows.size()>0) {
				generate(shippingFile, 
						new AbstractProductionSheetRenderer[] { 
							new ShippingSheetRenderer(shippingRows) 
						}, 
						batchNumbers, 
						String.format("%s-Shipping.pdf", batchNumbers));
			}
			
			jobInfo.setProgressPercent(33);
			notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
			
			if (sewingRows.size()>0) {
				generate(sewingFile, 
						new AbstractProductionSheetRenderer[] { new SewingSheetRenderer(sewingRows)}, 
						batchNumbers, 
						String.format("%s-Sewing.pdf", batchNumbers));
			}
			
			jobInfo.setProgressPercent(66);
			notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
			
			sendProductionSheetsCompletedNotification(batchNumbers, shippingFile, sewingFile);
				
			jobInfo.setProgressPercent(100);
			jobInfo.setIsCompleted(true);
			notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, jobInfo));
		} catch (Throwable e) {
			jobInfo.setErrorMessage("An error occured while generating production sheet PDF. "+e.getMessage());
			logger.error(jobInfo.getErrorMessage(), e);
			//notificationBroadcaster.broadcast(NotificationType.EntityChange, jobInfo);
			throw new PdfGenerationException("An error occured while generating production sheet PDF.", e);
		}
		
	}

	
	private void generate(File file, 
			AbstractProductionSheetRenderer[] renderers, 
			String batchNumbers, 
			String s3Filename) throws IOException, DocumentException
	{
		Document document = new Document(PageSize.LEGAL.rotate());
		
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

		writer.setPdfVersion(PdfWriter.VERSION_1_6);
		writer.setCompressionLevel(9);
		document.open();
	
		for(AbstractProductionSheetRenderer r:renderers)
		{
			if (!r.isEmpty())
			{
				document.setPageSize(r.getPageSize());
				Margins margins = r.getMargins();
				document.setMargins(margins.getLeft(), margins.getRight(), 
						margins.getTop(), margins.getBottom());
				document.newPage();
				r.render(document, writer, batchNumbers);
			}
		}
		
		document.close();
		
		String contentDisposition = String.format("attachment; filename=\"%s\"", s3Filename);
		storageService.putFile(file, "sheets/" + s3Filename, contentDisposition);		
	}
	
	private void sendProductionSheetsCompletedNotification(String batchNumbers, File shippingFile, File sewingFile) {
		Map<String,Object> model = new HashMap<String,Object>();
		model.put("batchNumbers", batchNumbers);

		Map<String,File> attachments = new HashMap<String, File>();
		if (shippingFile.length()>0) {
			attachments.put(String.format("%s-Shipping.pdf", batchNumbers), shippingFile);
		}
		if (sewingFile.length()>0) {
			attachments.put(String.format("%s-Sewing.pdf", batchNumbers), sewingFile);
		}
		
		EmailTemplate template = emailTemplateRepo.findByName("PRODUCTION_SHEET_NOTIFICATION");
		emailService.send(template, model, attachments);
	}

	
}
