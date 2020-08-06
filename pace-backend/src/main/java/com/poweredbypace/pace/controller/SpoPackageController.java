package com.poweredbypace.pace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poweredbypace.pace.domain.layout.SpoPackage;
import com.poweredbypace.pace.repository.SpoPackageRepository;
import com.poweredbypace.pace.service.CrudService;

@Controller
@RequestMapping(value = "/api/spoPackage")
public class SpoPackageController extends CrudController<SpoPackage>{

	@Autowired
	private SpoPackageRepository repo;
	
	public SpoPackageController() { }

	@Override
	protected CrudService<SpoPackage> getService() { return null; }

	@Override
	protected JpaRepository<SpoPackage, Long> getRepository() { return repo; }

}
