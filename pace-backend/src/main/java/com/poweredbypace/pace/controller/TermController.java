package com.poweredbypace.pace.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.Product.SystemAttribute;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.PrototypeProductOption;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;
import com.poweredbypace.pace.domain.TCountry;
import com.poweredbypace.pace.domain.TProductOptionValue;
import com.poweredbypace.pace.domain.TState;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.repository.PrototypeProductRepository;
import com.poweredbypace.pace.repository.TCountryRepository;

@Controller
public class TermController {
	
	@SuppressWarnings("unused")
	private final Log log = LogFactory.getLog(TermController.class);
	
	@Autowired
	private TCountryRepository tCountryRepo;
	
	@Autowired
	private PrototypeProductRepository prototypeProductRepo;
	
	@Autowired
	private Env env;
	
	@RequestMapping(value="/api/country", method=RequestMethod.GET)
	@ResponseBody
	public List<TCountry> getCountries() 
	{
		return tCountryRepo.findAll();
	}
	
	@RequestMapping(value="/api/country/{id}", method=RequestMethod.GET)
	@ResponseBody
	public TCountry getCountry(@PathVariable long id) 
	{
		TCountry country = tCountryRepo.findOne(id);
		return country;
	}
	
	@RequestMapping(value="/api/country/{id}/provinces", method=RequestMethod.GET)
	@ResponseBody
	public List<TState> getProvinces(@PathVariable long id) 
	{
		TCountry country = tCountryRepo.findOne(id);
		return new ArrayList<TState>( country.getStates() );
	}
	
	@RequestMapping(value="/api/categories", method=RequestMethod.GET)
	@ResponseBody
	public List<TProductOptionValue> getCategories() {
		List<TProductOptionValue> result = new ArrayList<TProductOptionValue>();
		PrototypeProduct prototype = prototypeProductRepo.findDefault(env.getStore());
		for(PrototypeProductOption po:prototype.getPrototypeProductOptions()) {
			if (po.getProductOptionType().getSystemAttribute()==SystemAttribute.ProductCategory) {
				for(PrototypeProductOptionValue value: po.getPrototypeProductOptionValues()) {
					result.add(value.getProductOptionValue());
				}
			}
		}
		return result;
	}
	
}
