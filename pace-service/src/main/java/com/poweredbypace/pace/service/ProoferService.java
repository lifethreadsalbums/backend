package com.poweredbypace.pace.service;

import java.util.List;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.layout.ProoferSettings;
import com.poweredbypace.pace.domain.layout.ProoferSettings.ProofStatus;
import com.poweredbypace.pace.domain.layout.ProoferStats;
import com.poweredbypace.pace.domain.layout.SpreadComment;
import com.poweredbypace.pace.exception.EmailAlreadyExistsException;

public interface ProoferService {
	
	SpreadComment saveComment(SpreadComment c);
	void deleteComment(long id);
	List<SpreadComment> getComments(Long layoutId);
	ProoferSettings approve(ProoferSettings settings) throws EmailAlreadyExistsException;
	ProoferSettings unapprove(ProoferSettings settings) throws EmailAlreadyExistsException;
	ProoferSettings saveProoferSettings(ProoferSettings settings) throws EmailAlreadyExistsException;
	ProoferSettings publish(ProoferSettings settings) throws EmailAlreadyExistsException;
	ProoferSettings getProoferSettings(Long layoutId);
	ProofStatus getProofStatus(Product p);
	ProoferStats getProoferStats(Product p);
	void trackComments(Product p);
	void trackComments();
	void trackReplies(Product p);
	void trackReplies();
}
