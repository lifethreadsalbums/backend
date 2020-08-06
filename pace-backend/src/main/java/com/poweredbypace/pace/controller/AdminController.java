package com.poweredbypace.pace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.user.Group;
import com.poweredbypace.pace.repository.GenericRuleRepository;
import com.poweredbypace.pace.repository.GroupRepository;
import com.poweredbypace.pace.service.ProductPrototypeService;
import com.poweredbypace.pace.service.ScriptingService;
import com.poweredbypace.pace.service.impl.PricingServiceImpl;

@Controller
@RequestMapping(value = "/api/admin")
public class AdminController {
	
	@Autowired
	ProductPrototypeService svc;
	
	@Autowired
	PricingServiceImpl pricingService;
	
	@Autowired
	GenericRuleRepository genericRuleRepo;
	
	@Autowired
	ScriptingService scriptingService;
	
	@Autowired
	private GroupRepository groupRepository;

	public AdminController() { }
	
	@RequestMapping(value="/userGroup", method=RequestMethod.GET)
	@ResponseBody
	public List<Group> getUserGroups() throws Exception 
	{
		return groupRepository.findAll();
	}
	
	@RequestMapping(value = "/clearCache", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void fillCache() {
		svc.prepopulateCache();
		pricingService.clearCache();
	}
	
	@RequestMapping(value = "/rules/{code}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<GenericRule> getRulesByCode(@PathVariable("code") String code) {
		return genericRuleRepo.findByCode(code);
	}
	
	@RequestMapping(value = "/rules", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public GenericRule saveRule(@RequestBody GenericRule rule) {
		return genericRuleRepo.save(rule);
	}
	
	@RequestMapping(value = "/script", method = RequestMethod.POST, produces="application/json")
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void runScript(@RequestBody ScriptDto dto) {
		scriptingService.runScript(dto.script);
	}
	
	public static class ScriptDto {
		public String script;
	}
	
}
