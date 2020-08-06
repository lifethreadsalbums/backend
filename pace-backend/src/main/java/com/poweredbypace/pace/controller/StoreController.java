package com.poweredbypace.pace.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.service.StoreService;

@Controller
public class StoreController {
	
	@Autowired
	private StoreService storeService;
	
	@RequestMapping(value="/api/store/current", method=RequestMethod.GET)
	@ResponseBody
	public Store getStore(HttpServletRequest request) {
		return storeService.getCurrentStore(request.getServerName());
	}
	
}
