package com.poweredbypace.pace.batch;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;
import com.poweredbypace.pace.domain.Sequence;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.service.SequenceService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PACEOrderNumberingStrategy implements OrderNumberingStrategy {
	
	private static final String PACE_ORDER_NUMBER = "PACE_ORDER_NUMBER";
	private static final String DUPLICATE_NAMES = "ABCDEFGHIJKLMNOPRSTUVWXYZ";
	
	@Autowired
	private SequenceService sequenceService;
	
	private boolean singleOrderItemNumber = false;
	
	public boolean isSingleOrderItemNumber() {
		return singleOrderItemNumber;
	}

	public void setSingleOrderItemNumber(boolean singleOrderItemNumber) {
		this.singleOrderItemNumber = singleOrderItemNumber;
	}

	@Override
	public void assignOrderNumber(Order o) {
		if (o.getOrderItems().size()==0) return;
		
		String baseOrderNumber = getBaseOrderNumber(o);
		assignItemNumbers(o, baseOrderNumber);
		
		if (o.getOrderItems().size()==1) {
			o.setOrderNumber(o.getOrderItems().get(0).getOrderItemNumber().replaceFirst("\\-1", ""));
		} else {
			String orderNumber = baseOrderNumber + "_";
			String firstItemNumber = o.getOrderItems().get(0).getOrderItemNumber();
			firstItemNumber = firstItemNumber.replaceFirst(baseOrderNumber+"-", "");
			
			String lastItemNumber = o.getOrderItems().get(o.getOrderItems().size() - 1).getOrderItemNumber();
			lastItemNumber = lastItemNumber.replaceFirst(baseOrderNumber+"-", "");
			
			orderNumber += firstItemNumber + "-" + lastItemNumber;
			o.setOrderNumber(orderNumber);
		}
	}
	
	private String getBaseOrderNumber(Order o) {
		String orderNumber = o.getOrderNumber();
		if (orderNumber==null) {
			Sequence seq = sequenceService.getNext(PACE_ORDER_NUMBER);
			return seq.getValue().toString();
		} else {
			Pattern pattern = Pattern.compile("^(\\d+)");
			
			Matcher m = pattern.matcher(orderNumber);
		    if (m.find() && m.groupCount()>0) 
		    	return m.group(0);
		    	
		    return null;
		}
	}
	
	private void assignItemNumbers(Order o, String orderNumber) {
		int orderItemNumber = 1;
		for(OrderItem orderItem:o.getOrderItems()) {
			
			String itemNumber = orderNumber;
			if (o.getOrderItems().size()>1 || (singleOrderItemNumber && orderItem.getProduct().getChildren().size()>0))
				itemNumber += "-" + orderItemNumber;
			
			boolean assignLettersToDuplicates = orderItem.getProduct().getPrototypeProduct().getProductType()==ProductType.DesignableProduct;
			List<Product> productWithDuplicates = orderItem.getProduct().getProductAndChildren();
			int numDuplicates = 0;
			for(Product p:productWithDuplicates) {
				numDuplicates += p.getQuantity()!=null ? p.getQuantity() : 0;
			}
			
			int productIndex = 0;
			int lastProductIndex = 0;
			for(Product p:productWithDuplicates) {
				String productNumber = itemNumber;
				int qty = p.getQuantity()!=null ? p.getQuantity() : 1;
				
				if (assignLettersToDuplicates && numDuplicates>1) {
					//assign letters
					int index1 = productIndex;
					int index2 = productIndex + qty - 1;
					
					productNumber += DUPLICATE_NAMES.charAt(index1);
					if (index2 - index1 > 1) 
						productNumber += "-";
					if (index1!=index2)
						productNumber += DUPLICATE_NAMES.charAt(index2);
					lastProductIndex = index2;
				}
						
				p.setProductNumber(productNumber);
				productIndex += qty;
			}
			if (assignLettersToDuplicates && lastProductIndex > 0) {
				itemNumber += DUPLICATE_NAMES.charAt(0);
				if (lastProductIndex > 1) 
					itemNumber += "-";
				
				itemNumber += DUPLICATE_NAMES.charAt(lastProductIndex);
			}
			
			orderItem.setOrderItemNumber(itemNumber);
			orderItemNumber++;
		}
	}
	
	
}
