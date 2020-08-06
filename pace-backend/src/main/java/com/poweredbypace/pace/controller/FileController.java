package com.poweredbypace.pace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poweredbypace.pace.domain.File;
import com.poweredbypace.pace.repository.FileRepository;
import com.poweredbypace.pace.service.CrudService;

@Controller
@RequestMapping(value = "/api/file")
public class FileController extends CrudController<File> {

	@Autowired
	private FileRepository repo;

	@Override
	protected JpaRepository<File, Long> getRepository() {
		return repo;
	}
	
	@Override
	protected CrudService<File> getService() {
		return null;
	}
	
}
