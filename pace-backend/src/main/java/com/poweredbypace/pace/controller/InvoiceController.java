package com.poweredbypace.pace.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.order.Invoice;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.InvoiceContext;
import com.poweredbypace.pace.repository.InvoiceRepository;
import com.poweredbypace.pace.repository.OrderRepository;
import com.poweredbypace.pace.service.GenericRuleService;

@Controller
public class InvoiceController {
	
	private final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	private InvoiceRepository invoiceRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
//	@Autowired
//	private PricingService pricingService;
//	
//	@Autowired
//	private InvoiceService invoiceService;
//	
//	@Autowired
//	private OrderService orderService;
//	
//	@Autowired(required=false)
//	private OrderNumberingStrategy orderNumberingStrategy;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	@Autowired
	private GenericRuleService ruleService;
	
//	@RequestMapping(value = "/api/invoice/{id}/email", method = RequestMethod.GET)
//	@ResponseBody
//	@ResponseStatus(value = HttpStatus.OK)
//	public void emailInvoice(@PathVariable long id) {
//		Order order = orderRepo.getOne(id);
//		Invoice invoice = invoiceRepo.findByOrder(order);
//		invoiceService.emailInvoice(invoice);
//	}
//	
//	@RequestMapping(value = "/api/invoice/{id}/price", method = RequestMethod.GET)
//	@ResponseBody
//	@ResponseStatus(value = HttpStatus.OK)
//	public void recalculatePrice(@PathVariable long id) {
//		Order order = orderRepo.getOne(id);
//		orderNumberingStrategy.assignOrderNumber(order);
//		pricingService.executePricing(order);
//		orderService.save(order);
//	}
//	
//	@RequestMapping(value = "/api/invoice/{id}/emailAdmin", method = RequestMethod.GET)
//	@ResponseBody
//	@ResponseStatus(value = HttpStatus.OK)
//	public void adminEmailInvoice(@PathVariable long id) {
//		Order order = orderRepo.getOne(id);
//		Invoice invoice = invoiceRepo.findByOrder(order);
//		invoiceService.emailInvoiceToAdmin(invoice);
//	}
	
	@RequestMapping(value = {"/invoice-{id}.html", "/invoice-{id}.pdf"}, method = RequestMethod.GET)
	public ModelAndView invoice(@PathVariable long id) {
		Order order = orderRepo.getOne(id);
		Invoice invoice = invoiceRepo.findByOrder(order);
		
		ModelAndView mav = new ModelAndView("INVOICE");
		mav.addObject("invoice", invoice);
		try {
			GenericRule rule = ruleService.findRule("INVOICE_PDF_NAME");
			if (rule!=null) {
				String filename = expressionEvaluator.evaluate(
					new InvoiceContext(invoice), rule.getJsonData(), String.class);
				mav.addObject("filename", filename);
			}
			
		} catch(Exception ex) {
			log.warn("Cannot eval INVOICE_PDF_NAME. " + ex.getMessage());
		}
		return mav;
	}
	
	
}
