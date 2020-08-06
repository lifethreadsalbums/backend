package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.domain.user.User.UserStatus;

public interface UserRepository extends JpaRepository<User, Long> {
	
	@Query(value="SELECT u FROM User u WHERE u.isDeleted<>true AND email=:email")
	User findByEmail(@Param("email") String email);
	
	@Query(value="SELECT u FROM User u WHERE u.systemAccount<>true AND u.isDeleted<>true")
	List<User> findAll();
	
	@Query(value="SELECT u FROM User u WHERE u.systemAccount<>true AND u.isDeleted<>true")
	Page<User> findAll(Pageable pageRequest);
	
	@Query(value="SELECT DISTINCT u FROM User u inner join u.roles r WHERE u.systemAccount<>true AND u.isDeleted<>true AND "
		+ "( COALESCE(:query, 'xxx')='xxx' OR "
		+ "     ( u.firstName LIKE CONCAT(:query,'%') OR u.lastName LIKE CONCAT(:query,'%') OR u.companyName LIKE CONCAT(:query, '%')) ) "
		+ "AND ( u.status in (:status)  ) "
		+ "AND (COALESCE(:groupId, 'xxx')='xxx' OR u.group.id=:groupId) "
		+ "AND (COALESCE(:role, 'xxx')='xxx' OR r.name=:role)")
	List<User> findByQuery(
			@Param("query") String query, 
			@Param("status") UserStatus[] status, 
			@Param("groupId") Long groupId, 
			@Param("role") String role, 
			Pageable pageRequest);
	
}
