package com.poweredbypace.pace.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Batch.BatchState;
import com.poweredbypace.pace.repository.BatchRepository;
import com.poweredbypace.pace.service.BatchService;
import com.poweredbypace.pace.service.BatchSubmissionService;
import com.poweredbypace.pace.service.CrudService;

@Controller
@RequestMapping(value = "/api/batch")
public class BatchController extends CrudController<Batch> {

	@Override
	protected CrudService<Batch> getService() {
		return svc;
	}
	
	@Override
	protected JpaRepository<Batch, Long> getRepository() {
		return null;
	}

	@Autowired
	private BatchService svc;
	
	@Autowired
	private BatchRepository repo;
	
	
	@Autowired(required=false)
	private BatchSubmissionService batchSubmissionService;
	
	
	@RequestMapping(value = "", params={"state","pageIndex","pageSize"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Batch> getBatches(
			@RequestParam("state") BatchState state,
			@RequestParam("pageIndex") Integer pageIndex,
			@RequestParam("pageSize") Integer pageSize) {
		
		return repo.findByState(state, 
			new PageRequest(pageIndex, pageSize, new Sort(Direction.DESC, "dateCreated")));
		
	}
	
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	@ResponseBody
	public Batch getCurrentBatch() {
		return svc.getPendingBatch();	
	}
	
	@RequestMapping(value = "/current/items/count", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Map<String, Object> getCurrentBatchItemCount() {
		Batch batch = svc.getPendingBatch();
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("count", batch.getProducts().size());
		return result;
	}
	
	@Override
	public Batch save(@RequestBody Batch batch) {
		if (batch.getId()==null) {
			batch.setDateCreated(new Date());
			batch.setState(BatchState.Queued);
		}
		return super.save(batch);
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value=HttpStatus.ACCEPTED)
	public void submitBatch() {
		
		batchSubmissionService.submitBatch();
		
	}
	
	
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Map<String, Object> getCount() {
		Map<String,Object> result = new HashMap<String, Object>();
		result.put("count", repo.countByState(BatchState.Printed));
		return result;
	}

}
