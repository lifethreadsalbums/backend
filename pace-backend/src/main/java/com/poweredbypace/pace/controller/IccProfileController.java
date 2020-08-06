package com.poweredbypace.pace.controller;

import java.util.List;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.IccProfile;
import com.poweredbypace.pace.repository.IccProfileRepository;

@Controller
@RequestMapping(value = "/api/iccprofile")
public class IccProfileController {

	@Autowired
	private IccProfileRepository repo;
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<IccProfile> getAll() {
		return IteratorUtils.toList( repo.findAll().iterator() );
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public IccProfile get(@PathVariable long id) {
		return repo.findOne(id);
	}
	
}
