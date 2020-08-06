package com.poweredbypace.pace.jobserver;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poweredbypace.pace.job.Job;
import com.poweredbypace.pace.job.JobCancelRequest;
import com.poweredbypace.pace.job.JobResult;
import com.poweredbypace.pace.job.JobScheduler;
import com.poweredbypace.pace.job.Job.JobPriority;
import com.poweredbypace.pace.job.task.AbstractTask;
import com.poweredbypace.pace.job.task.Task;

public class SqsJobScheduler implements ApplicationContextAware, JobScheduler {
	private final Log logger = LogFactory.getLog(getClass());
	private final ObjectMapper mapper = new ObjectMapper();
	
	
	@Value("${aws.accessKey}")
	private String accessKey;
	
	@Value("${aws.secretKey}")			
	private String secretKey;
	
	@Value("${queue.jobQueueUrl}")			
	private String jobQueueUrl;
	
	@Value("${queue.highPriorityJobQueueUrl}")	
	private String highPriorityJobQueueUrl;
	
	@Value("${queue.jobCancelRequestQueueUrl}")	
	private String jobCancelRequestQueueUrl;
	
	@Value("${queue.jobResultQueueUrl}")
	private String jobResultQueueUrl;
	
	private Boolean debugMode = false;
	private ApplicationContext appContext;
	
	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getJobCancelRequestQueueUrl() {
		return jobCancelRequestQueueUrl;
	}

	public void setJobCancelRequestQueueUrl(String jobCancelRequestQueueUrl) {
		this.jobCancelRequestQueueUrl = jobCancelRequestQueueUrl;
	}

	public String getJobQueueUrl() {
		return jobQueueUrl;
	}

	public void setJobQueueUrl(String jobQueueUrl) {
		this.jobQueueUrl = jobQueueUrl;
	}
	
	public String getJobResultQueueUrl() {
		return jobResultQueueUrl;
	}

	public void setJobResultQueueUrl(String jobResultQueueUrl) {
		this.jobResultQueueUrl = jobResultQueueUrl;
	}
	
	public String getHighPriorityJobQueueUrl() {
		return highPriorityJobQueueUrl;
	}

	public void setHighPriorityJobQueueUrl(String highPriorityJobQueueUrl) {
		this.highPriorityJobQueueUrl = highPriorityJobQueueUrl;
	}
	
	public void setDebugMode(Boolean debugMode) {
		this.debugMode = debugMode;
	}

	/* (non-Javadoc)
	 * @see com.poweredbypace.pace.jobserver.JobScheduler#scheduleJob(com.poweredbypace.pace.job.Job)
	 */
	@Override
	@Async
	public String scheduleJob(Job job)
	{
		return scheduleJob(job, 0);
	}

	/* (non-Javadoc)
	 * @see com.poweredbypace.pace.jobserver.JobScheduler#scheduleJob(com.poweredbypace.pace.job.Job, int)
	 */
	@Override
	@Async
	public String scheduleJob(Job job, int delaySeconds) {
		if (debugMode) {
			try {
				String json = mapper.writeValueAsString(job);
				Job job2 = mapper.readValue(json, Job.class);
				job2.setUser(job.getUser());
				Task task = AbstractTask.get(appContext, job2);
				task.run();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return "";
		}
		
		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey));
		try {
			String json = mapper.writeValueAsString(job);
			SendMessageRequest req = new SendMessageRequest( 
					job.getPriority()==JobPriority.Normal ?
					jobQueueUrl : highPriorityJobQueueUrl, json);
			if (delaySeconds>0)
				req.setDelaySeconds(delaySeconds);
			SendMessageResult res = sqs.sendMessage(req);
			
			logger.debug(String.format("Job scheduled: %s", json));
			
			return res.getMessageId();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	@Async
	public String scheduleJob(String json) {
		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey));
		try {
			SendMessageRequest req = new SendMessageRequest(jobQueueUrl, json);
			SendMessageResult res = sqs.sendMessage(req);
			
			logger.debug(String.format("Job scheduled: %s", json));
			
			return res.getMessageId();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.poweredbypace.pace.jobserver.JobScheduler#sendJobResult(com.poweredbypace.pace.job.JobResult)
	 */
	@Override
	public void sendJobResult(JobResult result) {
		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey));
		try {
			sqs.sendMessage(new SendMessageRequest(
					jobResultQueueUrl, mapper.writeValueAsString(result)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/* (non-Javadoc)
	 * @see com.poweredbypace.pace.jobserver.JobScheduler#cancelJob(com.poweredbypace.pace.job.JobCancelRequest)
	 */
	@Override
	@Async
	public void cancelJob(JobCancelRequest req) {
		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey));
		try {
			sqs.sendMessage(new SendMessageRequest(
					jobCancelRequestQueueUrl, mapper.writeValueAsString(req)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	
	/* (non-Javadoc)
	 * @see com.poweredbypace.pace.jobserver.JobScheduler#waitForCompletion(java.lang.String)
	 */
	@Override
	public JobResult waitForCompletion(String id) {
		boolean complete = false;
		JobResult result = null;
		
		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey)); 
		while(!complete) 
		{
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(jobResultQueueUrl);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : messages) {
            	try {
            		JobResult res = mapper.readValue(message.getBody(), JobResult.class);
            		if (res.getId().equals(id))
            		{
            			complete = true;
            			result = res;
            			logger.trace("Result found in the queue, deleting message from the queue "+jobResultQueueUrl);
            			//result found in the queue, delete message from the queue
            		    sqs.deleteMessage(new DeleteMessageRequest(jobResultQueueUrl, message.getReceiptHandle()));
            		} else {
            			//result not found, return the message to the queue by setting its visiblityTimeout to 0
            			sqs.changeMessageVisibility(
        					new ChangeMessageVisibilityRequest(
        						jobResultQueueUrl, 
        						message.getReceiptHandle(), 
        						0));
            		}
            		
            	} catch (Throwable e) {
					logger.trace("Error while processing job result queue", e);
				} 
            }
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.poweredbypace.pace.jobserver.JobScheduler#waitForAnyJobResult()
	 */
	@Override
	public JobResult waitForAnyJobResult() {
		boolean complete = false;
		JobResult result = null;
		
		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey)); 
		while(!complete) 
		{
			ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(jobResultQueueUrl);
            List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
            for (Message message : messages) {
            	try {
            		JobResult res = mapper.readValue(message.getBody(), JobResult.class);
            		result = res;
            		complete = true;
            		//logger.debug("Result found in the queue, deleting message from the queue "+jobResultQueueUrl);
        			
            		//result found in the queue, delete message from the queue
            		sqs.deleteMessage(new DeleteMessageRequest(jobResultQueueUrl, message.getReceiptHandle()));
            		
            	} catch (Throwable e) {
					logger.trace("Error while processing job result queue", e);
				} 
            }
		}
		return result;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.appContext = applicationContext;
		
	}

}
