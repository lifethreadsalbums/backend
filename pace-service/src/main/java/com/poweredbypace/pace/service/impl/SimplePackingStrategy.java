package com.poweredbypace.pace.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.TShippingPackageType;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.JavaScriptExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.ProductContext;
import com.poweredbypace.pace.repository.TShippingPackageTypeRepository;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.shipping.PackingStrategy;

public class SimplePackingStrategy implements PackingStrategy {
	
	@Autowired
	TShippingPackageTypeRepository packageTypeRepo;
	
	@Autowired
	GenericRuleService genericRuleService;
	
	private ExpressionEvaluator evaluator = new JavaScriptExpressionEvaluator();

	public SimplePackingStrategy() { }

	@Override
	public List<ShippingPackage> pack(Order order) {
		List<TShippingPackageType> packageTypes = packageTypeRepo.findAll();
		Collections.sort(packageTypes, 
			new Comparator<TShippingPackageType>() {
				@Override
				public int compare(TShippingPackageType o1,
						TShippingPackageType o2) {
					return o2.getMaxQuantity().compareTo(o1.getMaxQuantity());
				}
			}
		);
		
		List<ShippingPackage> packages = new ArrayList<ShippingPackage>();
		int totalItems = 0;
		List<Product> products = new ArrayList<Product>();
		for(OrderItem item : order.getOrderItems()) {
			Integer qty = item.getProduct().getQuantity();
			if (qty!=null) {
				int qtyInt = qty;
				for(int i=0;i<qtyInt;i++) {
					products.add(item.getProduct());
				}
				totalItems += qtyInt;
			}
		}
		
		int index=0;
		while (totalItems>0) {
			TShippingPackageType pt = findPackageType(packageTypes, totalItems);
			int numItems = Math.min(totalItems, pt.getMaxQuantity());
			
			float weight = 0f;
			for(int i=0;i<numItems;i++) {
				Product product = products.get(index + i);
				weight += getWeight(product);
			}
			
			index+=numItems;
			totalItems -= numItems;
			
			ShippingPackage pack = new ShippingPackage();
			pack.setPackageType(pt);
			pack.setWeight(weight);
			packages.add(pack);
		}
	
		return packages;
	}
	
	private float getWeight(Product p) {
		try {
			GenericRule rule = genericRuleService.findRule(p, "WEIGHT");
			
			Float weight = evaluator.evaluate(new ProductContext(p), rule.getJsonData(), Float.class);
			return weight!=null ? weight.floatValue() : 0f;
		} catch(Exception ex) { }
		return 0f;
	}
	
	private TShippingPackageType findPackageType(List<TShippingPackageType> packageTypes, int numItems) {
		TShippingPackageType min = null;
		for(TShippingPackageType pt:packageTypes) {
			if (numItems <= pt.getMaxQuantity()) {
				min = pt;
			}
		}
		if (min==null)
			min = packageTypes.get(0);
		return min;
	}

}
