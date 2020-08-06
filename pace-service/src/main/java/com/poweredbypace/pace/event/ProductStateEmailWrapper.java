package com.poweredbypace.pace.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.service.ProductService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ProductStateEmailWrapper extends HashMap<String, Object> {
	
	private static final long serialVersionUID = -1681995902640973756L;
	
	@Autowired
	private ProductService productService;
	
	private BulkProductStateChangedEvent event;

	public ProductStateEmailWrapper(BulkProductStateChangedEvent e) {
		this.event = e;
	}
	
	@PostConstruct
	private void postConstruct() {
		List<Product> products = new ArrayList<Product>();
		for(Long id:event.getProductIds()) {
			Product p = productService.findOne(id);
			if (p!=null) products.add(p);
		}
		this.put("event", event);
		if (products.size()==1) {
			Product p = products.get(0);
			OrderItem orderItem = p.getParent()!=null ? p.getParent().getOrderItem() : p.getOrderItem();
			this.put("productNumber", p.getProductNumber());
			this.put("productName", p.getName());
			this.put("productNumberAndName", p.getProductNumber() + " - " + p.getName());
			this.put("order", orderItem.getOrder());
			this.put("user", p.getUser());
			fillTrackingInfo(p);
		} else {
			Product p = products.get(0);
			OrderItem orderItem = p.getParent()!=null ? p.getParent().getOrderItem() : p.getOrderItem();
			Order order = orderItem.getOrder();
			
			this.put("productNumber", order.getOrderNumber());
			this.put("productName", order.getOrderNumber());
			this.put("productNumberAndName", order.getOrderNumber());
			this.put("order", order);
			this.put("user", order.getUser());
			fillTrackingInfo(products.get(0));
		}
	}
	
	private void fillTrackingInfo(Product p) {
		String carrier = p.getProductOptionDisplayValue("carrier");
		String carrierCode = p.getProductOptionCode("carrier");
		this.put("carrier", carrier);
		
		String trackingId = p.getProductOptionDisplayValue("trackingId");
		this.put("trackingId", trackingId);
		
		String trackingUrl = "";
		
		if ("UPS".equals(carrierCode)) {
			trackingUrl = "http://wwwapps.ups.com/WebTracking/track?track=yes&trackNums=" + trackingId;
		} else if ("CP".equals(carrierCode)) {
			trackingUrl = "http://www.canadapost.ca/cpotools/apps/track/personal/findByTrackNumber?LOCALE=en&trackingNumber=" + trackingId;
		} else if ("M1MI".equals(carrierCode)) {
			trackingUrl = "http://www.m1mi.ca";
		}
			
		this.put("trackingUrl", trackingUrl);
	}
	

}
