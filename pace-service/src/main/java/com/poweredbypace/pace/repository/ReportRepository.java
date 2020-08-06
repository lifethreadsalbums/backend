package com.poweredbypace.pace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.Report;

public interface ReportRepository extends JpaRepository<Report,Long>{

	Report findByCode(String code);
	List<Report> findByParentIsNull();
}
