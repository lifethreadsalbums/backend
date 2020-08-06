package com.poweredbypace.pace.shipping;

import java.util.Date;

public class TrackingResponse {

	public static enum DeliveryStatus {
		Delivered,
		InTransit
	}
	
	private DeliveryStatus deliveryStatus;
	private String trackingId;
	private Date deliveryDate;
	private Date shippingDate;
	
	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}
	
	public DeliveryStatus getDeliveryStatus() {
		return deliveryStatus;
	}

	public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
		this.deliveryStatus = deliveryStatus;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	
	public Date getShippingDate() {
		return shippingDate;
	}

	public void setShippingDate(Date shippingDate) {
		this.shippingDate = shippingDate;
	}

	public TrackingResponse() { }

}
