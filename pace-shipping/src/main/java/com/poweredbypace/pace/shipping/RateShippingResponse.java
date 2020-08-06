package com.poweredbypace.pace.shipping;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.poweredbypace.pace.domain.shipping.Shipment;


public class RateShippingResponse extends ShippingResponse {
	
	private String shipperName;
	private Shipment shipment;
	private List<RateShippingResponseEntry> entries = new ArrayList<RateShippingResponseEntry>();
	
	public String getShipperName() {
		return shipperName;
	}

	public void setShipperName(String shipperName) {
		this.shipperName = shipperName;
	}

	@JsonIgnore
	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public List<RateShippingResponseEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<RateShippingResponseEntry> entries) {
		this.entries = entries;
	}
}