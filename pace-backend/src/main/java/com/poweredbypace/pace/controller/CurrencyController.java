package com.poweredbypace.pace.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.CurrencyInfo;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.dto.CurrencyDto;
import com.poweredbypace.pace.manager.CurrencyManager;
import com.poweredbypace.pace.util.SpringContextUtil;

@Controller
public class CurrencyController {
	
	@Autowired
	private CurrencyManager currencyManager;
	
	@RequestMapping(value = "/api/currency", method = RequestMethod.GET)
	@ResponseBody
	public CurrencyDto getCurrency() {
		return new CurrencyDto( SpringContextUtil.getEnv().getCurrency() );
	}
	
	@RequestMapping(value = "/api/currency/available", method = RequestMethod.GET)
	@ResponseBody
	public List<CurrencyInfo> getAvailableCurrencies() {
		return currencyManager.getAvailableCurrencies();
	}
	
	@RequestMapping(value = "/api/currency/format", method = RequestMethod.POST)
	@ResponseBody
	public Money format(@RequestBody Money money) {
		
		if (money.getCurrency()==null)
			money.setCurrency(SpringContextUtil.getEnv().getCurrency().getCurrencyCode());
		
		return money;
	}

}
