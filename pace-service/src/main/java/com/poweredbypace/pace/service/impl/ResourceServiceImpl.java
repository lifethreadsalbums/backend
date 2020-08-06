package com.poweredbypace.pace.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.TResource;
import com.poweredbypace.pace.manager.ResourceManager;
import com.poweredbypace.pace.repository.TResourceRepository;

@Service
public class ResourceServiceImpl implements ResourceManager {

	@Autowired
	private TResourceRepository repo;
	
	public ResourceServiceImpl() {	}

	@Override
	@Cacheable(value="tresource", key="#id")
	public TResource getResource(long id) {
		TResource res = repo.findOne(id);
		res.getTranslations().size();
		return res;
	}

}
