package com.poweredbypace.pace.event;

import com.poweredbypace.pace.domain.Batch;

public class BatchSentEvent extends ApplicationEvent {
	
	private Long batchId;

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}
	
	public BatchSentEvent() {
		super();
	}

	public BatchSentEvent(Batch batch) {
		super();
		this.batchId = batch.getId();
	}

}
