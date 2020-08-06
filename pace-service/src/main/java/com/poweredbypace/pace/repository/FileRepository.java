package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.File;
import com.poweredbypace.pace.domain.user.User;

public interface FileRepository extends JpaRepository<File,Long>{
	List<File> findByUser(User user);
}
