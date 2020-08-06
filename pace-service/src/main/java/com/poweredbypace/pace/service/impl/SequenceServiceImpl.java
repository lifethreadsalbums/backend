package com.poweredbypace.pace.service.impl;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.Sequence;
import com.poweredbypace.pace.repository.SequenceRepository;
import com.poweredbypace.pace.service.SequenceService;

@Service
public class SequenceServiceImpl implements SequenceService {

	@Autowired
	private SequenceRepository repo;
	
	@Override
	@Transactional(value=TxType.REQUIRES_NEW)
	public Sequence getNext(String code) {
		
		Sequence seq = repo.findByCode(code);
		
		if (seq==null)
			throw new IllegalStateException("Cannot find sequence "+code);
		
		seq.setValue( seq.getValue() + seq.getStep() );
		repo.save(seq);
		
		return seq;
	}

}
