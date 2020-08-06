package com.poweredbypace.pace.repository;

import java.util.Set;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poweredbypace.pace.domain.layouttemplate.LayoutTemplate;

public interface LayoutTemplateRepository extends JpaRepository<LayoutTemplate, Long> {
	@Query("select lt from LayoutTemplate lt "
			+ "where lt.publicTemplate=true "
			+ "order by lt.ord desc")
	@Cacheable(value="LayoutTemplate")
	public Set<LayoutTemplate> getAllPublic();
	
	@Query("select lt from LayoutTemplate lt where "
			+ "lt.publicTemplate=true and "
			+ "lt.numEffectiveCells=?1 and "
			+ "lt.target='page' "
			+ "order by lt.ord desc")
	public Set<LayoutTemplate> getAllPagePublicTemplates(int size);
	
	@Query("select lt from LayoutTemplate lt where "
			+ "lt.publicTemplate=true and "
			+ "lt.left!=null and "
			+ "lt.right!=null and "
			+ "lt.left.numEffectiveCells=?1 and "
			+ "lt.right.numEffectiveCells=?2 "
			+ "order by lt.ord desc")
	public Set<LayoutTemplate> getAllTwoPagePublicTemplates(int lSize, int rSize);
	
	@Query("select lt from LayoutTemplate lt where "
			+ "lt.publicTemplate=true and "
			+ "lt.target='spread' and "
			+ "lt.numEffectiveCells=?1 "
			+ "order by lt.ord desc")
	public Set<LayoutTemplate> getAllSpreadSinglePublicTemplates(int size);
}
