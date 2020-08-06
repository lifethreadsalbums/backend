package com.poweredbypace.pace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poweredbypace.pace.domain.layout.LayoutSize;
import com.poweredbypace.pace.repository.LayoutSizeRepository;
import com.poweredbypace.pace.service.CrudService;

@Controller
@RequestMapping(value = "/api/layoutSize")
public class LayoutSizeController extends CrudController<LayoutSize>{

	@Autowired
	private LayoutSizeRepository repo;
	
	public LayoutSizeController() { }

	@Override
	protected CrudService<LayoutSize> getService() { return null; }

	@Override
	protected JpaRepository<LayoutSize, Long> getRepository() { return repo; }

}
