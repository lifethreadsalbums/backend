package com.poweredbypace.pace.binderyform.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.domain.Attachment;
import com.poweredbypace.pace.domain.Attachment.AttachmentType;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.event.BinderyFormGeneratedEvent;
import com.poweredbypace.pace.event.OrderBinderyFormGeneratedEvent;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.print.BinderyFormGenerator;
import com.poweredbypace.pace.repository.AttachmentRepository;
import com.poweredbypace.pace.service.BinderyFormService;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.StorageService;

@Service
public class BinderyFormServiceImpl implements BinderyFormService {

	
	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private BinderyFormGenerator bfGen;
	
	@Autowired
	private AttachmentRepository attachmentRepo;

	@Autowired
	private StorageService storageService;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;

	@Autowired
	private EventService eventService;
	
	
	@Override
	@Transactional
	public void generate(Product product, JobProgressInfo job)
			throws InterruptedException {

		try {
			if (product.isReprint()) return;
			if (product.getPrototypeProduct().getProductType()==ProductType.NondesignableProduct) return;
			
			job.setJobName("Generating Bindery Form for " + product.getProductNumber());
			
			String s3filename = String.format("bindery-sheet-%s.pdf", product.getProductNumber());
			File file = bfGen.generate(product, job);
			
			String contentDisposition = String.format("attachment; filename=\"%s\"", s3filename);
			String pdfURL = ApplicationConstants.BINDERY_FORM_PATH + s3filename;
			storageService.putFile(file, pdfURL, contentDisposition);
			
			List<Attachment> attachments = attachmentRepo.findByProductAndType(product, AttachmentType.BinderyForm);
			
			Attachment a = new Attachment();
			if (attachments!=null && attachments.size()>0)
				a = attachments.get(0);
			a.setProduct(product);
			a.setType(AttachmentType.BinderyForm);
			int version = a.getDocumentVersion()!=null ? a.getDocumentVersion() : 0;
			a.setDocumentVersion(++version);
			a.setUrl(pdfURL);
			a.setDate(new Date());
			attachmentRepo.save(a);
			
			job.setProgressPercent(100);
			job.setIsCompleted(true);
			notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, job));
			
			eventService.sendEvent(new BinderyFormGeneratedEvent(product));
			
		} catch (Throwable t) {
			log.error("An error occured while generating bindery form PDF. ", t);
			job.setErrorMessage("An error occured while generating bindery form PDF. "+t.getMessage());
			notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, job));
		}
		
	}
	
	
	@Override
	@Transactional
	public void generate(Order order, JobProgressInfo job)
			throws InterruptedException {

		try {
			
			job.setJobName("Generating Bindery Form for " + order.getOrderNumber());
			List<Product> allProducts = new ArrayList<Product>();
			for(OrderItem oi:order.getOrderItems()) {
				for (Product p:oi.getProduct().getProductAndChildren()) {
					if (p.isReprint() || p.getPrototypeProduct().getProductType()==ProductType.NondesignableProduct) 
						continue;
					allProducts.add(p);
				}
			}

			String s3filename = String.format("bindery-sheet-%s.pdf", order.getOrderNumber());
			File file = bfGen.generate(allProducts, job);
			
			String contentDisposition = String.format("attachment; filename=\"%s\"", s3filename);
			String pdfURL = ApplicationConstants.BINDERY_FORM_PATH + s3filename;
			storageService.putFile(file, pdfURL, contentDisposition);
			
			job.setProgressPercent(100);
			job.setIsCompleted(true);
			notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, job));
			
			eventService.sendEvent(new OrderBinderyFormGeneratedEvent(order));
			
		} catch (Throwable t) {
			log.error("An error occured while generating bindery form PDF. ", t);
			job.setErrorMessage("An error occured while generating bindery form PDF. "+t.getMessage());
			notificationBroadcaster.broadcast(Notification.create(NotificationType.JobProgress, job));
		}
		
	}
	
	
	
}
