package com.poweredbypace.pace.job.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.exception.PrintGenerationException;
import com.poweredbypace.pace.print.OutputType;
import com.poweredbypace.pace.service.PrintProductionService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GenerateDiesTask extends AbstractTask {
	
	private final Log log = LogFactory.getLog(getClass());
	
	public static class Params {
		public long productId;
		public OutputType outputType;
	}
	
	@Autowired
	private PrintProductionService printProductionService;
	
	@Override
	public int getTimeout() {
		return 60 * 60 * 2; //2h
	}

	@Override
	public void run() {
		Params params = (Params) job.getParams();
		try {
			JobProgressInfo progressInfo = job.getJobProgressInfo();
			progressInfo.setProductId(params.productId);
			printProductionService.generateDies(params.productId, params.outputType, progressInfo);
		} catch (PrintGenerationException e) {
			log.error("Error while generating die", e);
		}
	}

}
