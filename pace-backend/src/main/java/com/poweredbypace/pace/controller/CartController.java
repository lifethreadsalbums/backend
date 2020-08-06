package com.poweredbypace.pace.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;
import com.poweredbypace.pace.dto.OrderDto;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.PricingService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.service.impl.ShippingManager;
import com.poweredbypace.pace.shipping.PackingStrategy;
import com.poweredbypace.pace.shipping.RateShippingResponse;
import com.poweredbypace.pace.shipping.RateShippingResponseEntry;
import com.poweredbypace.pace.shipping.ShippingProvider;

@Controller
public class CartController {
	
	private Log log = LogFactory.getLog(getClass());
	
	private static final String SHIPPING_OPTIONS_ATTRIBUTE_NAME = CartController.class.getName() + ".SHIPPING_OPTIONS";
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private ShippingManager shippingManager;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private PackingStrategy packingStrategy;
	 
	
	@RequestMapping(value = "/api/cart", method = RequestMethod.GET)
	@ResponseBody
	public Order getCart() {
		Order cart = orderService.getCart(userService.getCurrentUser());
		pricingService.executePricing(cart);
		return cart;
	}
	
	@RequestMapping(value = "/api/cart/add", method = RequestMethod.POST)
	@ResponseBody
	public Order addToCart(@RequestBody Product p) {
		return orderService.addToCart(p);
	}
	
	@RequestMapping(value = "/api/cart/addMultiple", method = RequestMethod.POST)
	@ResponseBody
	public Order addMultipleToCart(@RequestBody List<Product> products) {
		return orderService.addToCart(products);
	}
	
	@RequestMapping(value = "/api/cart/orderItem/{id}/remove", method = RequestMethod.POST)
	@ResponseBody
	public Order deleteOrderItem(@PathVariable long id) {
		orderService.deleteOrderItem(id);
		return this.getCart();
	}
	
	@RequestMapping(value = "/api/cart/empty", method = RequestMethod.POST)
	@ResponseBody
	public Order emptyCart() {
		
		Order cart = orderService.getCart(userService.getCurrentUser());
		cart.getOrderItems().clear();
		cart.setCouponCode(null);
		pricingService.executePricing(cart);
		orderService.save(cart);
		
		return cart;
	}
	
	@RequestMapping(value = "/api/cart/product/{id}/setQuantity/{qty}", method = RequestMethod.POST)
	@ResponseBody
	public Order setQuantity(@PathVariable long id, @PathVariable int qty) {
		
		Product product = productService.findOne(id);
		product.setQuantity(qty);
		productService.save(product);
		return this.getCart();
	
	}
	
	
	@RequestMapping(value = "/api/cart", method = RequestMethod.POST)
	@ResponseBody
	public Order saveCart(@RequestBody OrderDto orderDto, HttpSession session) {
		
		Order order = orderService.getCart(userService.getCurrentUser());
		
		if (!StringUtils.equals(order.getCouponCode(), orderDto.getCouponCode())) {
			pricingService.checkCoupon(order, orderDto.getCouponCode());
		}
		
		order.setAddresses(orderDto.getAddresses());
		order.setCouponCode(orderDto.getCouponCode());
		
		if (orderDto.getNotes()!=null && order.getOrderItems().size()>0) {
			Product p = order.getOrderItems().get(0).getProduct();
			p.setUserNotes(orderDto.getNotes());
		}
		
		//handle shipping option selectiom
		@SuppressWarnings("unchecked")
		List<RateShippingResponse> result = (List<RateShippingResponse>) session.getAttribute(SHIPPING_OPTIONS_ATTRIBUTE_NAME);
		if (result!=null && orderDto.getShippingOption()!=null) {
			for(RateShippingResponse rate:result) {
				for(RateShippingResponseEntry entry:rate.getEntries()) {
					if (orderDto.getShippingOption().getName().equals(entry.getShippingOption().getName())) {
						order.setShippingCost(entry.getMoney());
						order.setShippingOption(entry.getShippingOption());
					}
				}
			}	
		}
		
		pricingService.executePricing(order);
		orderService.save(order);
		return order;
	}
	
	@RequestMapping(value = "/api/cart/pay", method = RequestMethod.POST)
	@ResponseBody
	public Order pay(@RequestBody OrderDto orderDto) {
		
		Order order = orderService.getCart(userService.getCurrentUser());
		order.setDateCreated(new Date());
		order.setAddresses(orderDto.getAddresses());
		order.setCouponCode(orderDto.getCouponCode());
		if (orderDto.getNotes()!=null)
			order.getOrderItems().get(0).getProduct().setUserNotes(orderDto.getNotes());
		orderService.prepareForPayment(order);
		
		return order;
		
	}
	
	@RequestMapping(value = "/api/cart/shipping", method = RequestMethod.GET)
	@ResponseBody
	public List<RateShippingResponse> getShippingOptions(HttpSession session) {
		
		List<RateShippingResponse> result = new ArrayList<RateShippingResponse>();
		Order order = orderService.getCart(userService.getCurrentUser());
		List<ShippingPackage> packages = packingStrategy.pack(order);
		for(ShippingProvider p:shippingManager.getAvailableShippingProviders()) {
			
			try {
				List<RateShippingResponse> response = shippingManager.rateShipment(order, p, packages);
				result.addAll(response);
			} catch (Exception ex) {
				log.warn("Shipping rate not available for "+p.getProviderId() + ". " + ex.getMessage());
				log.error("", ex);
			}
		}
		
		session.setAttribute(SHIPPING_OPTIONS_ATTRIBUTE_NAME, result);
		return result;
		
	}
	
}
