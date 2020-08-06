 package com.poweredbypace.pace.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.payment.PaymentGateway;
import com.poweredbypace.pace.payment.PaymentGatewayConfiguration;
import com.poweredbypace.pace.util.MessageUtil;
import com.poweredbypace.pace.util.SpringContextUtil;

@Controller
public class PaymentGatewayController {

	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private Env env;
	
	private String getAbsUrl(String url) {
		Store store = env.getStore();
		return "https://" + store.getDomainName() + url;
	}
	
	@RequestMapping(value = "/api/payment/{gateway}/paymentComplete", method = RequestMethod.GET)
	public RedirectView handlePaymentCompleteRequest(@PathVariable String gateway, HttpServletRequest request, 
			HttpServletResponse response, RedirectAttributes redirectAttrs) {
		
		
		PaymentGateway paymentGateway = SpringContextUtil.getApplicationContext().getBean(gateway, PaymentGateway.class);
		try {
			Order order = paymentGateway.handlePaymentCompleteRequest(request, response);
			redirectAttrs.addFlashAttribute("infoMessage", MessageUtil.getMessage("payment.complete"));
			return new RedirectView(getAbsUrl("/#/orders/history/" + order.getId()));
		} catch (Throwable ex) {
			log.error("", ex);
			redirectAttrs.addFlashAttribute("errorMessage", MessageUtil.getMessage(ex));
			return new RedirectView(getAbsUrl("/#/paymentError"));
		}
		
	}
	
	@RequestMapping(value = "/api/payment/{gateway}/paymentComplete", method = RequestMethod.POST)
	public RedirectView handlePaymentCompletePostRequest(@PathVariable String gateway, HttpServletRequest request, 
			HttpServletResponse response, RedirectAttributes redirectAttrs) {
		
		
		PaymentGateway paymentGateway = SpringContextUtil.getApplicationContext().getBean(gateway, PaymentGateway.class);
		try {
			Order order = paymentGateway.handlePaymentCompleteRequest(request, response);
			redirectAttrs.addFlashAttribute("infoMessage", MessageUtil.getMessage("payment.complete"));
			return new RedirectView(getAbsUrl("/#/orders/history/" + order.getId()));
		} catch (Throwable ex) {
			log.error("", ex);
			redirectAttrs.addFlashAttribute("errorMessage", MessageUtil.getMessage(ex));
			return new RedirectView(getAbsUrl("/#/paymentError"));
		}
		
	}
	
	@RequestMapping(value = "/api/payment/{gateway}/config", method = RequestMethod.GET)
	@ResponseBody
	public PaymentGatewayConfiguration getPaymentGatewayConfiguration(@PathVariable String gateway, HttpServletRequest request, HttpServletResponse response) {
		
		PaymentGateway paymentGateway = SpringContextUtil.getApplicationContext().getBean(gateway, PaymentGateway.class);
		return paymentGateway.getConfiguration();
		
	}
	
	@RequestMapping(value = "/api/payment/{gateway}/paymentError", method = RequestMethod.GET)
	public RedirectView handlePaymentErrorRequest(@PathVariable String gateway, HttpServletRequest request, 
			HttpServletResponse response, RedirectAttributes redirectAttrs) {
		
		PaymentGateway paymentGateway = SpringContextUtil.getApplicationContext().getBean(gateway, PaymentGateway.class);
		
		try {
			paymentGateway.handlePaymentErrorRequest(request, response);
		} catch (Throwable ex) {
			log.error("", ex);
			redirectAttrs.addFlashAttribute("errorMessage", MessageUtil.getMessage(ex));
			return new RedirectView(getAbsUrl("/#/paymentError"));
		}
		
		redirectAttrs.addFlashAttribute("errorMessage", "Payment Error");
		return new RedirectView(getAbsUrl("/#/paymentError"));
		
	}
	
}
