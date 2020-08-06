package com.poweredbypace.pace.domain;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.SimpleUserSerializer;

public class JobProgressInfo {

	private String jobId = UUID.randomUUID().toString();
	private Integer progressPercent = 0;
	private Long productId;
	private Long orderId;
	private String jobName;
	private String jobType;
	private User user;
	private String errorMessage;
	private Boolean isCompleted = false;
	private Boolean isWaiting = false;
	private Boolean isCancellable = true;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getJobId() {
		return jobId;
	}

	public void setJobId(String jobId) {
		this.jobId = jobId;
	}

	public Integer getProgressPercent() {
		return progressPercent;
	}

	public void setProgressPercent(Integer progressPercent) {
		this.progressPercent = progressPercent;
	}
	
	@JsonSerialize(using=SimpleUserSerializer.class)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getIsWaiting() {
		return isWaiting;
	}

	public void setIsWaiting(Boolean isWaiting) {
		this.isWaiting = isWaiting;
	}
	
	public Boolean getIsCancellable() {
		return isCancellable;
	}

	public void setIsCancellable(Boolean isCancellable) {
		this.isCancellable = isCancellable;
	}
	
	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}
	
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Boolean getIsCompleted() {
		return isCompleted;
	}

	public void setIsCompleted(Boolean isCompleted) {
		this.isCompleted = isCompleted;
	}

	public JobProgressInfo() {
		super();
	}
	
}
