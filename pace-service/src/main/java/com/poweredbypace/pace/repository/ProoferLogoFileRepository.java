package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.ProoferLogoFile;
import com.poweredbypace.pace.domain.user.User;

public interface ProoferLogoFileRepository extends JpaRepository<ProoferLogoFile, Long> {
	
	List<ProoferLogoFile> findByUser(User user);
	
}
