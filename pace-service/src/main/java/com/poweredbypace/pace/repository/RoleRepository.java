package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.user.Role;


public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
