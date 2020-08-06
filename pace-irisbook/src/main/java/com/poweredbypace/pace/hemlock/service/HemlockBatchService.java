package com.poweredbypace.pace.hemlock.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Batch.BatchState;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.event.BatchSentEvent;
import com.poweredbypace.pace.hemlock.domain.BatchEmailModel;
import com.poweredbypace.pace.hemlock.task.SendXmlTicketTask;
import com.poweredbypace.pace.hemlock.task.SendXmlTicketTaskImpl;
import com.poweredbypace.pace.job.Job;
import com.poweredbypace.pace.job.JobScheduler;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.repository.BatchRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.BatchService;
import com.poweredbypace.pace.service.BatchSubmissionService;
import com.poweredbypace.pace.service.EmailService;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.util.SpringContextUtil;

@Service
public class HemlockBatchService implements BatchSubmissionService {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private HemlockTicketProducer ticketProducer;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	@Autowired
	private BatchRepository batchRepo;
	
	@Autowired
	private BatchService batchService;
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private JobScheduler jobScheduler;
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Autowired
	private LayoutService layoutService;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private EmailService emailService;
	
	@Value("${hemlock.debugMode}")
	private boolean debugMode = true;
	
	@PostConstruct
	public void postConstruct() {
		log.info("Hemlock service, debug mode="+debugMode);
	}
	
	public boolean isHemlockJob(Product product) {
		Layout layout = layoutService.getEffectiveLayout(product);
		if (product.isReprint()) {
			layout = layoutService.getEffectiveLayout(product.getOriginal());
		}
		
		if (layout==null) {
			return false;
		}
		try {
			GenericRule rule = ruleService.findRule(product, "IRIS_BATCH_GROUP");
			return rule!=null && "\"Hemlock Jobs\"".equals(rule.getJsonData());
		} catch (Exception e) {
			return false;
		}
	}
	

	@Override
	@Transactional(value=TxType.REQUIRED)
	public void submitBatch() {
		
		Batch batch = batchService.getPendingBatch();
		
		List<Product> batchProducts = productRepo.findByStateAndParentIsNull(ProductState.Printed);
		batchProducts.addAll(productRepo.findByStateAndParentIsNull(ProductState.Printing));
		batch.setProducts(batchProducts);
		
		log.info("Submitting batch " + batch.getName() + ", num jobs=" + batchProducts.size() + ", debug="+debugMode);
		
		Map<String, File> attachments = debugMode ? new HashMap<String, File>() : null;
		
		List<Product> products = new ArrayList<Product>();
		for(Product parentProduct: batch.getProducts()) {
			for(Product p:parentProduct.getProductAndChildren()) {
				p.setBatch(batch);
				products.add(p);
				if (isHemlockJob(p)) {
					log.info("Scheduling XML ticket for job "+p.getProductNumber());
					
					Job job = new Job();
					job.setType(SendXmlTicketTask.class);
					SendXmlTicketTaskImpl.Params params = new SendXmlTicketTaskImpl.Params();
					params.productId = p.getId();
					job.setParams(params);
					jobScheduler.scheduleJob(job);
					
				} else {
					log.info("Not a Hemlock job "+p.getProductNumber());
				}
			}
		}
		
		batch.setState(BatchState.Printed);
		batch.setDatePrinted(new Date());
		batch = batchRepo.save(batch);
		
		for(Product p:products) {
			p.setState(ProductState.ReadyToShip);
			productRepo.save(p);
		}
		
		sendEmails(batch, attachments);
		
		notificationBroadcaster.broadcast(Notification.create(NotificationType.BatchSentToPrint, batch));
		eventService.sendEvent(new BatchSentEvent(batch)); 
		log.info("Batch "+batch.getName()+" sent to Hemlock.");
	}
	
	@Override
	public void sendTicket(Product p) {
		if (debugMode) {
			try {
				String ticket = ticketProducer.getTicketContents(p);
				Map<String,Object> model = new HashMap<String, Object>();
				model.put("ticket", ticket);
				emailService.send(emailService.getEmailTemplate("HEMLOCK_XML_TICKET"), model);
			} catch (Exception e) {
				log.error("Error while generating XML ticket", e);
			}
			
		} else {
			ticketProducer.sendTicket(p);
		}
	}
	
	private void sendEmails(Batch batch, Map<String, File> attachments) {
		Map<String,Object> model = new HashMap<String, Object>();
		model.put("model", SpringContextUtil.getApplicationContext().getBean(BatchEmailModel.class, batch));
		
		emailService.send(emailService.getEmailTemplate("BATCH_EMAIL_HEMLOCK"), model, attachments);
		emailService.send(emailService.getEmailTemplate("BATCH_EMAIL_IRIS"), model, attachments);
		emailService.send(emailService.getEmailTemplate("BATCH_EMAIL_DR_PHOTO"), model, attachments);
	}

}
