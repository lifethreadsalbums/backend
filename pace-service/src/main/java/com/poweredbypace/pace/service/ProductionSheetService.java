package com.poweredbypace.pace.service;

import java.util.List;

import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.exception.PdfGenerationException;

public interface ProductionSheetService {
	
	void generate(List<Long> batcheIds, JobProgressInfo jobInfo) throws PdfGenerationException, InterruptedException;
	
}
