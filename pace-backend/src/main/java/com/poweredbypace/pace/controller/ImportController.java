package com.poweredbypace.pace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.legacy.IrisbookImportService;

@Controller
@RequestMapping(value = "/api/import")
public class ImportController {
	
	@Autowired
	private IrisbookImportService svc;
	

	public ImportController() { }
	
	@RequestMapping(value = "/order/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public String importOrder(@PathVariable long id) {
		svc.importOrder(id);
		return "ok";
	}
	
	@RequestMapping(value = "/order/all", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public String importOrders() {
		svc.importOrders();
		return "ok";
	}
	
	@RequestMapping(value = "/order/fix", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public String fixOrders() {
		svc.fixOrders();
		return "ok";
	}
	
	@RequestMapping(value = "/fix/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public String fixOrder(@PathVariable long id) {
		svc.fixOrder(id);
		return "ok";
	}

}
