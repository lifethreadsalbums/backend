package com.poweredbypace.pace.shipping;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShippingOption;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;

public abstract class ShippingProvider {

	private final Log log = LogFactory.getLog(ShippingProvider.class);
	
	private String providerId;
	private ShipmentCreateStrategy shipmentCreateStrategy;
	private ShipmentExporter exporter;

	public ShippingProvider(String providerId) {
		this.providerId = providerId;
	}

	public abstract RateShippingResponse rate(Shipment shipment);
	
	public abstract ShippingResponse ship(Shipment shipment, ShippingOption shippingOption);
	
	public abstract TrackingResponse track(String trackingId);

	
	protected boolean checkAvailability(Shipment shipment) {
		return true;
	}
	
	public List<Shipment> createShipment(Order order, List<ShippingPackage> packages) {
		List<ShippingPackage> clonedPackages = new ArrayList<ShippingPackage>();
		for(ShippingPackage shippingPackage : packages) {
			try {
				clonedPackages.add((ShippingPackage)BeanUtils.cloneBean(shippingPackage));
			} catch (Exception e) {
				log.error(e.getStackTrace(), e);
			}
		}
		List<Shipment> shipments = getShipmentCreateStrategy().create(order, clonedPackages);
		List<Shipment> results = new ArrayList<Shipment>();
		for(Shipment shipment : shipments) {
			if(checkAvailability(shipment)) {
				shipment.setShippingProviderId(getProviderId());
				shipment.setShipmentStatus(Shipment.ShipmentStatus.New);
				shipment.setOrder(order);
				shipment.setShippingOption(order.getShippingOption());
				shipment.setStore(order.getStore());
				results.add(shipment);
			}
		}
		return results;
	}
	
	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public ShipmentCreateStrategy getShipmentCreateStrategy() {
		return shipmentCreateStrategy;
	}

	public void setShipmentCreateStrategy(
			ShipmentCreateStrategy shipmentCreateStrategy) {
		this.shipmentCreateStrategy = shipmentCreateStrategy;
	}

	public ShipmentExporter getExporter() {
		return exporter;
	}

	public void setExporter(ShipmentExporter exporter) {
		this.exporter = exporter;
	}
}
