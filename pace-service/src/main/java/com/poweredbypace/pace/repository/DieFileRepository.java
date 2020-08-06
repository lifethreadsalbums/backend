package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.DieFile;
import com.poweredbypace.pace.domain.user.User;

public interface DieFileRepository extends JpaRepository<DieFile, Long> {
	
	List<DieFile> findByUser(User user);
	
}
