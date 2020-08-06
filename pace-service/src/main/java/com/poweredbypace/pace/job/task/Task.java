package com.poweredbypace.pace.job.task;

import com.poweredbypace.pace.job.Job;


public interface Task extends Runnable {

	void setJob(Job job);
	Job getJob();
	int getTimeout();
	
}
