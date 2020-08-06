package com.poweredbypace.pace.job;

import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.job.task.Task;
import com.poweredbypace.pace.json.SimpleUserSerializer;

public class Job {
	
	public static enum JobPriority {
		Normal,
		High
	}
	private String id;
	private Class<? extends Task> type;
	private String description;
	private Boolean ignoreResult = false;
	private Integer attempt = 1;
	private User user;
	private JobPriority priority = JobPriority.Normal;
	private Object params;
	
	@Enumerated(EnumType.STRING)
	public JobPriority getPriority() {
		return priority;
	}
	public void setPriority(JobPriority priority) {
		this.priority = priority;
	}
	public Class<? extends Task> getType() {
		return type;
	}
	public void setType(Class<? extends Task> type) {
		this.type = type;
	}
	
	@JsonTypeInfo(use=Id.CLASS, include=As.PROPERTY, property="class")
	public Object getParams() {
		return params;
	}
	public void setParams(Object params) {
		this.params = params;
	}
	
	@JsonIgnore
	public String getId() {
		return id;
	}
	
	@JsonIgnore
	public void setId(String id) {
		this.id = id;
	}
	
	@JsonSerialize(using=SimpleUserSerializer.class)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Boolean getIgnoreResult() {
		return ignoreResult;
	}
	public void setIgnoreResult(Boolean ignoreResult) {
		this.ignoreResult = ignoreResult;
	}
	
	public Integer getAttempt() {
		return attempt;
	}
	public void setAttempt(Integer attempt) {
		this.attempt = attempt;
	}
	
	@JsonIgnore
	public JobProgressInfo getJobProgressInfo() {
		JobProgressInfo jobInfo = new JobProgressInfo();
		if (getId()!=null)
			jobInfo.setJobId(getId());
		else
			jobInfo.setJobId(UUID.randomUUID().toString());
		jobInfo.setJobName(getDescription());
		jobInfo.setUser(user);
		jobInfo.setJobType(getType().getSimpleName());
		return jobInfo;
	}
	
}
