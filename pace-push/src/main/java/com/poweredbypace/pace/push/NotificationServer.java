package com.poweredbypace.pace.push;

import java.io.IOException;
import java.util.List;

import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.nettosphere.Nettosphere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

public class NotificationServer {

	private static final Logger logger = LoggerFactory.getLogger(NotificationServer.class);
	
	@Autowired
	private AmazonSQS sqs;
	
	@Autowired
	private Nettosphere nettosphere;
	
	@Autowired
	private Broadcaster broadcaster;
	
	@Value("${atmosphere.port}") private int nettospherePort;
	@Value("${sqs.url}") private String queueUrl;
	
    public void start() throws IOException {
    	
        nettosphere.start();
        
        logger.info("Pace Notification Server started on port {}", nettospherePort);
        logger.info("Pace Notification Server started on port {}", nettospherePort);
       
        while(true)
		{
			try {
				ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
				receiveMessageRequest.setMaxNumberOfMessages(1);
	            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
	            
	            long size = 0;
	            for (Message message : messages) {
	            	String jsonMessage = message.getBody();

	            	broadcaster.broadcast(jsonMessage);
					
					size += jsonMessage.length();
					
		            sqs.deleteMessage(new DeleteMessageRequest(queueUrl, message.getReceiptHandle()));
		        }
	            
	            if (messages.size()>0) {
		            logger.debug("Num messages received=" + messages.size() +
		            		", size=" + (size/1024) + 
		            		"KB, num connections=" + (broadcaster!=null ? broadcaster.getAtmosphereResources().size() : -1));
		            
		            long heapSize = Runtime.getRuntime().totalMemory() / 1024 / 1024;
		    		long heapMaxSize = Runtime.getRuntime().maxMemory() / 1024 / 1024;
		    		long heapFreeSize = Runtime.getRuntime().freeMemory() / 1024 / 1024;
		    		long usedMem = heapSize - heapFreeSize;
		    		logger.info("Memory: total:" + heapSize + " MB, max:" + heapMaxSize + " MB, free:" + heapFreeSize + " MB, used=" + usedMem + " MB");
	            }
	            
	            System.gc();
	            
			} catch (Throwable t) {
				logger.error("Error while receiving messages from SQS", t);
			}
			
        }
    }

}
