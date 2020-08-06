package com.poweredbypace.pace.service;

import com.poweredbypace.pace.domain.Sequence;

public interface SequenceService {
	
	Sequence getNext(String code);
	
}
