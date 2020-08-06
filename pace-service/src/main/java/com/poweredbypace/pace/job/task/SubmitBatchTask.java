package com.poweredbypace.pace.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.service.BatchSubmissionService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SubmitBatchTask extends AbstractTask {
	
	public static class Params {
		public long batchId;
	}

	@Autowired
	private BatchSubmissionService batchService;
	
	@Override
	public int getTimeout() {
		return 60 * 60 * 2; //2h
	}

	@Override
	public void run() {
		batchService.submitBatch();
	}

}
