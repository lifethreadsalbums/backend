package com.poweredbypace.pace.jobserver;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.ChangeMessageVisibilityRequest;
import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.job.Job;
import com.poweredbypace.pace.job.Job.JobPriority;
import com.poweredbypace.pace.job.JobCancelRequest;
import com.poweredbypace.pace.job.JobScheduler;
import com.poweredbypace.pace.job.task.AbstractTask;
import com.poweredbypace.pace.job.task.Task;
import com.poweredbypace.pace.json.HibernateAwareObjectMapper;
import com.poweredbypace.pace.service.UserService;

public class QueueProcessor implements ApplicationContextAware {

	protected final Log logger = LogFactory.getLog(getClass());
	private static final long CANCEL_REQUEST_EXPIRATION_TIME = 1000 * 60 * 30; //30 minutes 
	private static final int MAX_THREADS = 10;

	private ApplicationContext appContext;
	
	
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
	
	private Map<String,Long> cancelledJobs = new HashMap<String, Long>();
	
	@Autowired
	private JobScheduler jobScheduler;
	
	@Autowired
	private UserService userService;
	
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

	public String getHighPriorityJobQueueUrl() {
		return highPriorityJobQueueUrl;
	}

	public void setHighPriorityJobQueueUrl(String highPriorityJobQueueUrl) {
		this.highPriorityJobQueueUrl = highPriorityJobQueueUrl;
	}
	
	
	
	private CustomThreadPoolExecutor threadPoolExecutor;
	
	@Override
	public void setApplicationContext(ApplicationContext appContext)
			throws BeansException {
		this.appContext = appContext;
		
	}
	
	@PostConstruct
	public void initialize()
	{
		BlockingQueue<Runnable> worksQueue = new FakeQueue<Runnable>();
		RejectedExecutionHandler executionHandler = new MyRejectedExecutionHandeler();
		 
		// Create the ThreadPoolExecutor
		threadPoolExecutor = new CustomThreadPoolExecutor(MAX_THREADS, MAX_THREADS, 2,
		        TimeUnit.SECONDS, worksQueue, executionHandler, 
		        new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey)),
		        jobQueueUrl, highPriorityJobQueueUrl);
		threadPoolExecutor.allowCoreThreadTimeOut(true);
		
		if (StringUtils.isEmpty(highPriorityJobQueueUrl)) 
			logger.warn("High priority queue won't be processed");
		
		if (StringUtils.isEmpty(jobQueueUrl)) 
			logger.warn("Low priority queue won't be processed");
	}
	
	private void receiveAndProcessMessages(AmazonSQS sqs, String queueUrl, ObjectMapper mapper) 
			throws JsonParseException, JsonMappingException, IOException
	{
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
        List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {
        	
        	Job job = mapper.readValue(message.getBody(), Job.class);
			job.setId(message.getMessageId());
			if (job.getUser()!=null) {
				User user = userService.getByEmail(job.getUser().getEmail());
				job.setUser(user);
			}
        	
        	//check if job has been cancelled
			if (cancelledJobs.containsKey(job.getId()))
        	{
        		logger.debug("This job has been cancelled, deleting the message, id="+message.getMessageId());
        		//delete message from the queue and continue
        		sqs.deleteMessage(new DeleteMessageRequest(jobQueueUrl, message.getReceiptHandle()));
        		continue;
        	}
    		
			Task task = AbstractTask.get(appContext, job);
			threadPoolExecutor.execute(task, message);
	    }
	}
	
	private void processCancelRequests(AmazonSQS sqs, ObjectMapper mapper) 
			throws JsonParseException, JsonMappingException, IOException
	{
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(jobCancelRequestQueueUrl);
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
        for (Message message : messages) {
        	
    		JobCancelRequest req = mapper.readValue(message.getBody(), JobCancelRequest.class);
    		
    		if (threadPoolExecutor.cancelTask(req.getId()))
    		{
    	        sqs.deleteMessage(new DeleteMessageRequest(
    	        		jobCancelRequestQueueUrl, message.getReceiptHandle()));
    		} else {
    			cancelledJobs.put(req.getId(), new Date().getTime());
    			cleanUpCancelledJobs();
    			//return cancel request to the queue
    			sqs.changeMessageVisibility(
    					new ChangeMessageVisibilityRequest(
    							jobCancelRequestQueueUrl, 
    							message.getReceiptHandle(), 
    							0));
    		}
        
        }
	}
	
	public void processQueue()
	{
		ObjectMapper mapper = new HibernateAwareObjectMapper();// ObjectMapper(); 
    	
		AmazonSQS sqs = new AmazonSQSClient(new BasicAWSCredentials(accessKey, secretKey));
		
		logger.info("PACE Job Server started");
		logger.info("MAX_THREAD="+MAX_THREADS);
		while(true)
		{
			try {
				
				//receive hi priority jobs first
				if (!StringUtils.isEmpty(highPriorityJobQueueUrl)) {
					receiveAndProcessMessages(sqs, highPriorityJobQueueUrl, mapper);
				}
				
				//receive normal priority jobs
				if (!StringUtils.isEmpty(jobQueueUrl)) {
					receiveAndProcessMessages(sqs, jobQueueUrl, mapper);
				}
				
	            //process cancel requests
				processCancelRequests(sqs, mapper);
            
			} catch (Throwable e) {
			
				logger.error("Error while processing jobs", e);
				
			} 
            
            try {
				Thread.sleep(250);
			} catch (InterruptedException e) { }
		}
	}
	
	private void cleanUpCancelledJobs()
	{
		long now = new Date().getTime();
		for(String id:cancelledJobs.keySet())
		{
			long timestamp = cancelledJobs.get(id);
			if (now - timestamp > CANCEL_REQUEST_EXPIRATION_TIME)
			{
				cancelledJobs.remove(id);
			}
		}
	}

	private static class FakeQueue<E> extends LinkedBlockingQueue<E> {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 4612091147387323176L;

		@Override
		public boolean offer(E element)
		{
			return false;
		}
	}
	
	private static class MyRejectedExecutionHandeler implements RejectedExecutionHandler {
		//protected final Log logger = LogFactory.getLog(getClass());
		@Override
		public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
			CustomThreadPoolExecutor customExecutor = (CustomThreadPoolExecutor)executor;
			
			if (!(r instanceof Task))
				return;
			Task task = (Task) r;
			customExecutor.returnMessageToQueue(task);
		}
		
	}
	
	private class CustomThreadPoolExecutor extends ThreadPoolExecutor {
		protected final Log logger = LogFactory.getLog(getClass());
		private Map<String,Thread> threadsById;
		private Map<String,Message> messagesById;
		private AmazonSQS sqs;
		private String queueUrl;
		private String highPriorityQueueUrl;

		public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
				long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, 
				RejectedExecutionHandler h, AmazonSQS sqs, String queueUrl, String highPriorityQueueUrl) {
			super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, h);
			
			this.threadsById = new ConcurrentHashMap<String, Thread>();
			this.messagesById = new ConcurrentHashMap<String, Message>();
			this.sqs = sqs;
			this.queueUrl = queueUrl;
			this.highPriorityQueueUrl = highPriorityQueueUrl;
		}

		@Override
		protected void beforeExecute(Thread t, Runnable r) {
			
			if (r instanceof Task) 
			{
				Task task = (Task) r;
				threadsById.put(task.getJob().getId(), t);
				
				Message m = messagesById.get(task.getJob().getId());
				if (m!=null)
				{
					logger.debug("Before execute, setting message time out, id="+task.getJob().getId());
					sqs.changeMessageVisibility(
							new ChangeMessageVisibilityRequest(
									task.getJob().getPriority()==JobPriority.Normal ? queueUrl : highPriorityQueueUrl, 
									m.getReceiptHandle(), 
									task.getTimeout()));
				} else 
					logger.error("Before execute, cannot find a message for job ID="+task.getJob().getId());
			}
			
			super.beforeExecute(t, r);
		}

		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			
			if (!(r instanceof Task))
				return;
			Task task = (Task) r;
			
			if (t != null) {
				
				logger.info("Exception detected, ex="+t.getMessage()+", jobId=" +task.getJob().getId()+", t="+t);
				if (task.getJob().getAttempt()<3) {
					task.getJob().setAttempt( task.getJob().getAttempt() + 1 );
					logger.info("Rescheduling job, attempt = "+task.getJob().getAttempt());
					jobScheduler.scheduleJob(task.getJob());
				}
				
				//returnMessageToQueue(task);
				
			}
			
			logger.debug("Deleting message from queue, id="+task.getJob().getId());
			Message m = messagesById.get(task.getJob().getId());
			if (m!=null) {
				
				sqs.deleteMessage(new DeleteMessageRequest(
						task.getJob().getPriority()==JobPriority.Normal ? queueUrl : highPriorityQueueUrl,
						m.getReceiptHandle()));
				messagesById.remove(task.getJob().getId());
				threadsById.remove(task.getJob().getId());
				
			} else {
				logger.error("Cannot find message for jobId=" + task.getJob().getId());
			}
				
		}

		public void execute(Task task, Message m)
		{
			//do not execute task if is already being executed
			if (messagesById.get(task.getJob().getId())!=null) {
				logger.debug("" +
						"=" + task.getJob().getId() + " is already being executed, ignoring it.");
				return;
			}
			logger.debug("Executing task, type="+task.getClass().getName() + ", ID=" + task.getJob().getId() );
			messagesById.put(task.getJob().getId(), m);
			
			super.execute(task);
		}
		
		public void returnMessageToQueue(Task task)
		{
			Message m = messagesById.get(task.getJob().getId());
			if (m!=null)
			{
				sqs.changeMessageVisibility(
						new ChangeMessageVisibilityRequest(
								task.getJob().getPriority()==JobPriority.Normal ? queueUrl : highPriorityQueueUrl, 
								m.getReceiptHandle(), 
								0));
				messagesById.remove(task.getJob().getId());
				logger.debug("Message returned to queue, id="+task.getJob().getId());
			}
		}
		
		public boolean cancelTask(String id)
		{
			if (threadsById.containsKey(id))
			{
				logger.debug("Cancelling task, ID="+id);
				Thread t = threadsById.get(id);
				//interrupt the thread
				//if the interruption is handled properly the message will be deleted
				//when the afterExecution() method is called
				//otherwise it will be returned to the queue
				t.interrupt();
				return true;
			}
			return false;
		}
		
		
	}
	
}
