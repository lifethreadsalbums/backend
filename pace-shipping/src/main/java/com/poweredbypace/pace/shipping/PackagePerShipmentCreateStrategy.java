package com.poweredbypace.pace.shipping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;

public class PackagePerShipmentCreateStrategy extends ShipmentCreateStrategy {

	public List<Shipment> doCreate(Order order, List<ShippingPackage> packages) {
		List<Shipment> shipments = new ArrayList<Shipment>();
		for(ShippingPackage shippingPackage : packages) {
			Shipment shipment = new Shipment();
			shippingPackage.setShipment(shipment);
			shipment.setPackages(Arrays.<ShippingPackage>asList(shippingPackage));
			shipments.add(shipment);
		}
		return shipments;
	}
}
