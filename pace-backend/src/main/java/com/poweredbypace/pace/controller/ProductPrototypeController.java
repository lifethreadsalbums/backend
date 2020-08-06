package com.poweredbypace.pace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.repository.PrototypeProductRepository;
import com.poweredbypace.pace.service.CrudService;
import com.poweredbypace.pace.service.ProductPrototypeService;

@Controller
@RequestMapping(value = "/api/productPrototype")
public class ProductPrototypeController extends CrudController<PrototypeProduct> {

	@Autowired
	PrototypeProductRepository repo;
	
	@Autowired
	ProductPrototypeService svc;
	
	@Autowired
	private Env env;
	
	@Override
	protected CrudService<PrototypeProduct> getService() { return null; }

	@Override
	protected JpaRepository<PrototypeProduct, Long> getRepository() { return repo;	}
	
	
	
	@RequestMapping(value = "", params={"default"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public PrototypeProduct getDefault() {
		return svc.getDefault(env.getStore());
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<PrototypeProduct> getAll() {
		return repo.findByStore(env.getStore());
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@Override
	public PrototypeProduct get(@PathVariable long id) {
		return svc.getById(id);
	}
	
	@RequestMapping(value = "", params={"code"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public PrototypeProduct getByCode(@RequestParam String code) {
		return svc.getByCode(code);
	}
	
}
