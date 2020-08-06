package com.poweredbypace.pace.shipping;

import java.util.List;

import javax.transaction.Transactional;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShipmentAddress;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;
import com.poweredbypace.pace.util.SpringContextUtil;

@SuppressWarnings("unused")
public abstract class ShipmentCreateStrategy {

	@Transactional
	public List<Shipment> create(Order order, List<ShippingPackage> packages) {
		List<Shipment> shipments = doCreate(order, packages);
		for(Shipment shipment : shipments) {
			
			if(order.getDropShippingAddress() != null) {
				shipment.setToAddress(new ShipmentAddress(order.getDropShippingAddress()));
			} else {
				shipment.setToAddress(new ShipmentAddress(order.getShippingAddress()));
			}
			
			shipment.setBillingAddress(new ShipmentAddress(order.getBillingAddress()));
			//TODO: fix store address - it throws no session error sometimes
			//shipment.setFromAddress(new ShipmentAddress(SpringContextUtil.getEnv().getStore().getAddress()));
			
			if(order.getUser() != null) {
				shipment.setUser(order.getUser());
			}
			shipment.setDropShipment(order.getDropShipment());
		}
		return shipments;
	}
	
	public abstract List<Shipment> doCreate(Order order, List<ShippingPackage> packages);

}