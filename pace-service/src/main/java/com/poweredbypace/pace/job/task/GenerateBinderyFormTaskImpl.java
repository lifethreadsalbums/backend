package com.poweredbypace.pace.job.task;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.service.BinderyFormService;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GenerateBinderyFormTaskImpl extends AbstractTask implements GenerateBinderyFormTask {
	
	private Log log = LogFactory.getLog(getClass());
	
	public static class Params {
		public long productId;
	}
	
	@Autowired
	BinderyFormService bfGenerator;
	
	@Autowired
	ProductRepository productRepo;
	
	@Override
	public int getTimeout() {
		return 60 * 60 * 1; //1h;
	}

	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public void run() {
		
		Params params = (Params) job.getParams();
		Product p = productRepo.findOne(params.productId);
		JobProgressInfo pi = job.getJobProgressInfo();
		pi.setProductId(params.productId);
		try {
			bfGenerator.generate(p, pi);
		} catch (InterruptedException e) {
			log.info("Task interrupted");
		} 

	}

}
