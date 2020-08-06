package com.poweredbypace.pace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poweredbypace.pace.domain.layout.CoverType;
import com.poweredbypace.pace.repository.CoverTypeRepository;
import com.poweredbypace.pace.service.CrudService;

@Controller
@RequestMapping(value = "/api/coverType")
public class CoverTypeController extends CrudController<CoverType> {

	@Autowired
	private CoverTypeRepository repo;
	
	@Override
	protected CrudService<CoverType> getService() {
		return null;
	}

	@Override
	protected JpaRepository<CoverType, Long> getRepository() {
		return repo;
	}
	
}
