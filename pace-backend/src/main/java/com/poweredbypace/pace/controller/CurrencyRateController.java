package com.poweredbypace.pace.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.poweredbypace.pace.repository.CurrencyRateRepository;
import com.poweredbypace.pace.service.impl.CurrencyServiceImpl;

@Controller
public class CurrencyRateController {
	
	@Autowired
	private CurrencyRateRepository currencyRateRepo;
	
	@Autowired
	private CurrencyServiceImpl currencyManager;
	
	@RequestMapping(value = "/currencyRates.html", method = RequestMethod.GET)
	public ModelAndView currencyRates(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("currencyRates");
		mav.addObject("currencyRates", currencyRateRepo.findAll());
		return mav;
	}
	
	@RequestMapping(value = "/updateCurrencyRates.html", method = RequestMethod.GET)
	public ModelAndView updateCurrencyRates(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView(new RedirectView("/currencyRates.html", true));
		currencyManager.updateRates();
		return mav;
	}
}
