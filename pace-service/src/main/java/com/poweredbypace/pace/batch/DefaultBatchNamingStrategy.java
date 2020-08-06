package com.poweredbypace.pace.batch;

import org.springframework.beans.factory.annotation.Autowired;

import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.repository.BatchRepository;

public class DefaultBatchNamingStrategy implements BatchNamingStrategy {
	
	@Autowired
	private BatchRepository batchRepo;
	
	@Override
	public String getNextBatchName() {
		int maxNum = 0;
		for(Batch b:batchRepo.findAll()) {
			try {
				int nr = Integer.parseInt(b.getName());
				maxNum = Math.max(nr, maxNum);
			} catch (NumberFormatException ex) {
				
			}
		}
		return Integer.toString(maxNum + 1);
	}
}
