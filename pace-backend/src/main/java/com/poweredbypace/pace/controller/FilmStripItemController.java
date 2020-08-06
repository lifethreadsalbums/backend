package com.poweredbypace.pace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.poweredbypace.pace.domain.layout.FilmStripItem;
import com.poweredbypace.pace.repository.FilmStripItemRepository;
import com.poweredbypace.pace.service.CrudService;

@Controller
@RequestMapping(value = "/api/filmstripitem")
public class FilmStripItemController extends CrudController<FilmStripItem> {

	@Autowired
	private FilmStripItemRepository repo;

	@Override
	protected JpaRepository<FilmStripItem, Long> getRepository() {
		return repo;
	}
	
	@Override
	protected CrudService<FilmStripItem> getService() {
		return null;
	}
	
}
