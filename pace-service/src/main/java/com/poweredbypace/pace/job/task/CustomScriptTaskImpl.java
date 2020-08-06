package com.poweredbypace.pace.job.task;

import java.util.Map;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.CustomScript;
import com.poweredbypace.pace.repository.CustomScriptRepository;
import com.poweredbypace.pace.service.ScriptingService;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CustomScriptTaskImpl extends AbstractTask implements CustomScriptTask {
	
	private Log log = LogFactory.getLog(getClass());
	
	public static class Params {
		public String scriptId;
		public Map<String,Object> data;
		public int timeout = 0;
	}
	
	@Autowired
	CustomScriptRepository scriptRepo;
	
	@Autowired
	private ScriptingService scriptingService; 
	
	private int timeout = 60 * 60 * 4; //4h
	
	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public void run() {
		
		Params params = (Params) job.getParams();
		if (params.timeout>0) {
			this.timeout = params.timeout;
		}
		
		CustomScript script = scriptRepo.findByCode(params.scriptId);
		
		if (script==null) {
			log.info("Script " + params.scriptId + " not found.");
			return;
		}
		
		scriptingService.runScript(script.getScript(), params.data);

	}

}
