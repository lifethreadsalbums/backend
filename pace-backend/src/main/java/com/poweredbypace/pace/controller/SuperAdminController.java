package com.poweredbypace.pace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.ProductService;

@Controller
@RequestMapping(value = "/api/sa")
public class SuperAdminController {
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	private ProductService productService;

	public SuperAdminController() { }
	
	
	@RequestMapping(value = "/pay/{id}", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public Order markAsPaid(@PathVariable long id) {
		Product p = productService.findOne(id);
		return orderService.markAsPaid(p);
	}
	
}
