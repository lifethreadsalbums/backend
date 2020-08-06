package com.poweredbypace.pace.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.settings.Settings;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.repository.SettingsRepository;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.UserService;

@Controller
@RequestMapping(value = "/api/settings")
public class SettingsController {

	@Autowired
	private SettingsRepository repo;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Env env;

	@RequestMapping(value = "", method = RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public Settings save(@RequestBody Settings data) {
		if (data.getUser()!=null) {
			data.setUser(userService.get(data.getUser().getId()));
		}
		
		if (data.getProduct()==null) {
			Settings settings = repo.findByUser(data.getUser());
			if (settings!=null) {
				settings.setSettingsAsString(data.getSettingsAsString());
				data = settings;
			}
		}
		
		return repo.save(data);
	}
	
	@RequestMapping(value = "", params={"productId"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Settings> getByProduct(@RequestParam long productId) {
		
		List<Settings> result = new ArrayList<Settings>();
		Product product = productService.findOne(productId);
		for(Product p:product.getProductAndChildren()) {
			Settings settings = repo.findByProduct(p);
			result.add(settings);
		}
		return result;
	}
	
	@RequestMapping(value = "", params={"userId"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Settings getByUser(@RequestParam Long userId) {
		Settings settings = repo.findByUser(userService.get(userId));
		return settings;
	}
	
	@RequestMapping(value = "", params={"!userId", "!productId"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Settings getCurrentUserSettings() {
		Settings settings = repo.findByUser(userService.getCurrentUser());
		return settings;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Settings getByStore() {
		Settings settings = repo.findByStore( env.getStore() );
		return settings;
	}
	
}
