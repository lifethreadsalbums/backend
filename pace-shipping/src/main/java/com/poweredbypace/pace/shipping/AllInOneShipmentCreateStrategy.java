package com.poweredbypace.pace.shipping;

import java.util.Arrays;
import java.util.List;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;

public class AllInOneShipmentCreateStrategy extends ShipmentCreateStrategy {

	public List<Shipment> doCreate(Order order, List<ShippingPackage> packages) {
		Shipment shipment = new Shipment();
		shipment.setPackages(packages);
		for(ShippingPackage shippingPackage : packages) {
			shippingPackage.setShipment(shipment);
		}
		return Arrays.<Shipment>asList(shipment);
	}
}
