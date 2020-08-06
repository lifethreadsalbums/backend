package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.LogoFile;
import com.poweredbypace.pace.domain.user.User;

public interface LogoFileRepository extends JpaRepository<LogoFile, Long> {
	
	List<LogoFile> findByUser(User user);
	
}
