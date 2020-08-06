package com.poweredbypace.pace.job;


public interface JobScheduler {

	String scheduleJob(String json);
	String scheduleJob(Job job);
	String scheduleJob(Job job, int delaySeconds);
	void sendJobResult(JobResult result);
	void cancelJob(JobCancelRequest req);
	JobResult waitForCompletion(String id);
	JobResult waitForAnyJobResult();

}