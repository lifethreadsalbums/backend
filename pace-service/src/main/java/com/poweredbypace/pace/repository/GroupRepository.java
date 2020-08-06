package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.Group;

public interface GroupRepository extends JpaRepository<Group, Long>
{
	List<Group> findByStore(Store store);
}
