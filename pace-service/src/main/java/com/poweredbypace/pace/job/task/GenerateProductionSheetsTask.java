package com.poweredbypace.pace.job.task;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.service.ProductionSheetService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GenerateProductionSheetsTask extends AbstractTask {
	
	private Log log = LogFactory.getLog(getClass());
	
	public static class Params {
		public long[] batchIds;
	}
	
	@Autowired
	private ProductionSheetService generator;
	
	@Override
	public int getTimeout() {
		return 60 * 5;
	}

	@Override
	public void run() {
		Params params = (Params) job.getParams();
		List<Long> batchIds = Arrays.asList(ArrayUtils.toObject(params.batchIds));
		
		try {
			generator.generate(batchIds, job.getJobProgressInfo());
		} catch (InterruptedException e) {
			log.error("", e);
		}

	}

}
