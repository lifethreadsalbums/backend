package com.poweredbypace.pace.job.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.job.Job;
import com.poweredbypace.pace.service.UserService;

public abstract class AbstractTask implements Task {
	
	protected final Log log = LogFactory.getLog(AbstractTask.this.getClass());

	protected Job job;
	
	@Autowired
	protected UserService userService;

	public Job getJob() {
		return job;
	}
	
	public void setJob(Job job) {
		this.job = job;
	}
	
	public User getUser() {
		return userService.getByEmail(job.getUser().getEmail());
	}
	
	public static Task get(ApplicationContext applicationContext, Job job) {
		assert(applicationContext != null);
		assert(job != null);
		
		final Task task = (Task) applicationContext.getBean(job.getType());

		if(task != null)
			task.setJob(job);
		
		return task;
	}
	
}