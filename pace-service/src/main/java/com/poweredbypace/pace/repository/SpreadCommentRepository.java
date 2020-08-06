package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.layout.SpreadComment;

public interface SpreadCommentRepository extends JpaRepository<SpreadComment, Long> {
	
	@Query(value="select c from SpreadComment c where c.parent is null and c.layout.id = ?1")
	List<SpreadComment> findByLayoutId(Long layoutId);
	
	@Query(value="select c from SpreadComment c where c.parent is null and c.layout = ?1")
	List<SpreadComment> findByLayout(Layout l);

}
