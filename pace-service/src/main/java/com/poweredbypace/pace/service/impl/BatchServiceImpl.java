package com.poweredbypace.pace.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.batch.BatchNamingStrategy;
import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Batch.BatchState;
import com.poweredbypace.pace.repository.BatchRepository;
import com.poweredbypace.pace.service.BatchService;

@Service
public class BatchServiceImpl implements BatchService {

	@Autowired
	private BatchRepository repo;
	
	@Autowired(required=false)
	private BatchNamingStrategy batchNamingStrategy;
	
	@Override
	public Batch save(Batch entity) {
		return repo.save(entity);
	}

	@Override
	public List<Batch> save(List<Batch> entities) {
		return repo.save(entities);
	}

	@Override
	public Batch findOne(long id) {
		return repo.findOne(id);
	}

	@Override
	public List<Batch> findAll() {
		return repo.findAll();
	}

	@Override
	public void delete(Batch entity) {
		repo.delete(entity);
	}

	@Override
	public void delete(List<Batch> entities) {
		repo.delete(entities);
	}

	@Override
	public List<Batch> findAll(List<Long> ids) {
		return repo.findAll(ids);
	}

	@Override
	public void delete(long id) {
		repo.delete(id);
	}
	
	@Transactional(value=TxType.REQUIRED)
	public Batch getPendingBatch() {
		List<Batch> batches = repo.findByState(BatchState.Queued);
		Batch batch = null;
		if (batches.size()>0) {
			batch = batches.get(0);
		} else {
			batch = new Batch();
			batch.setDateCreated(new Date());
			batch.setName(batchNamingStrategy.getNextBatchName());
			batch.setState(BatchState.Queued);
			batch = repo.save(batch);
		}
		return batch;
	}

	
}
