package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.Sequence;

public interface SequenceRepository extends JpaRepository<Sequence, Long> {
	
	Sequence findByCode(String code);

}
