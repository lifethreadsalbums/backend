package com.poweredbypace.pace.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poweredbypace.pace.domain.CustomScript;
import com.poweredbypace.pace.repository.CustomScriptRepository;
import com.poweredbypace.pace.service.ScriptingService;

@Controller
@RequestMapping(value = "/api/export")
public class ExportController {
	
	/*
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private LayoutRepository layoutRepo;
	
	
	@RequestMapping(value = "/layout/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Layout getLayoutById(@PathVariable long id) {
		return layoutRepo.findOne(id);		
	}
	
	@RequestMapping(value = "/product/{id}", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Product getProductById(@PathVariable long id) {
		return productRepo.findOne(id);		
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/product", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Product> getAllProducts() {
		ProductState[] states = {ProductState.Generated, ProductState.Generating};
		return IteratorUtils.toList( productRepo.findByProductStatesAndParentIsNull(states).iterator() );
	}
	*/
	@Autowired
	private ScriptingService scriptingService;
	
	@Autowired
	private CustomScriptRepository scriptRepository;
	
	
	@RequestMapping(value = "/order-{id}.json", method = RequestMethod.GET, produces="application/json")
	public void getOrderById(@PathVariable String id, HttpServletResponse response) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", id);
		export(params, response);
	}
	
	@RequestMapping(value = "/product-{id}.json", method = RequestMethod.GET, produces="application/json")
	public void getProductById(@PathVariable String id, HttpServletResponse response) throws IOException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("productId", id);
		export(params, response);
	}
	
	private void export(Map<String, Object> params, HttpServletResponse response) throws IOException {
		CustomScript script = scriptRepository.findByCode("ORDER_EXPORT");
		String json = scriptingService.runScript(script.getScript(), params, String.class);
		response.setContentType("application/json");
		response.setContentLength(json.getBytes().length);
        
		IOUtils.copy(new StringReader(json), response.getOutputStream());
	}
}
