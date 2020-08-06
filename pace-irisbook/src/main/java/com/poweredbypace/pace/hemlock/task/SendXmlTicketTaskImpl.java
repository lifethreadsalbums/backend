package com.poweredbypace.pace.hemlock.task;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.Attachment;
import com.poweredbypace.pace.domain.Attachment.AttachmentType;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.job.task.AbstractTask;
import com.poweredbypace.pace.print.OutputType;
import com.poweredbypace.pace.service.BatchSubmissionService;
import com.poweredbypace.pace.service.PrintProductionService;
import com.poweredbypace.pace.service.ProductService;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SendXmlTicketTaskImpl extends AbstractTask implements SendXmlTicketTask {

	private final Log log = LogFactory.getLog(getClass());
	
	public static class Params {
		public long productId;
	}
	
	@Autowired
	private PrintProductionService printProductionService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BatchSubmissionService hemlockService;
	
	@Override
	public int getTimeout() {
		return 60 * 60 * 10; //10h
	}

	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public void run() {
		Params params = (Params) job.getParams();
		try {
			Product product = productService.findOne(params.productId);
			
			Attachment attachment = productService.getAttachment(product, AttachmentType.HiResPdf);
			if (attachment!=null && attachment.getUrl()==null) {
				boolean pdfReady = false;
				long startime = System.currentTimeMillis();
				long currentTime = System.currentTimeMillis();
				long timeout = 5 * 60 * 60 * 1000; //5h
				do {
					
					log.info("PDF for " + product.getProductNumber() + " is being generated, waiting...");
					Thread.sleep(1000 * 60);
					attachment = productService.getAttachment(product, AttachmentType.HiResPdf);
					if (attachment!=null && attachment.getUrl()!=null) {
						pdfReady = true;
					}
					currentTime = System.currentTimeMillis();
					
				} while (!pdfReady && (currentTime - startime < timeout));
			}
			
			long checksum = attachment!=null ? attachment.getChecksum() : 0;
			long productChecksum = productService.getProductChecksum(product);
			log.info("Checking product " + product.getProductNumber() + ", checksum=" + checksum + ", " + productChecksum);
			if (checksum!=productChecksum || (attachment!=null && attachment.getUrl()==null) ) {
				log.info("Checksum mismatch, going to generate PDF for " + product.getProductNumber());
				JobProgressInfo progressInfo = job.getJobProgressInfo();
				progressInfo.setProductId(params.productId);
				printProductionService.generateAlbum(product, OutputType.Pdf, progressInfo);
			}
			log.info("Sending XML ticket for " + product.getProductNumber());
			hemlockService.sendTicket(product);
		} catch (InterruptedException e) {
			log.info("Task interrupted");
		} catch (PrintGenerationException e) {
			log.error("Error while generating PDF for product ID="+params.productId, e);
		}
	}


}
