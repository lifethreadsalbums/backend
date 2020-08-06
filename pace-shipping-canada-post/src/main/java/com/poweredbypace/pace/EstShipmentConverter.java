package com.poweredbypace.pace;

import java.util.HashMap;
import java.util.Map;

import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.util.UnitConverterUtil;

public class EstShipmentConverter {

	private Map<String, String> serviceCodes = new HashMap<String, String>();
	
	public EstExportBean convert(Shipment shipment) {
		EstExportBean exportBean = new EstExportBean();
		exportBean.setRecordType("3");
		exportBean.setService(getServiceCodes().get(shipment.getShippingOption().getCode()));
		exportBean.setImportedOrderId("PACE-"+shipment.getOrder().getId().toString());
		exportBean.setFirstName(shipment.getToAddress().getReceiverFirstName());
		exportBean.setLastName(shipment.getToAddress().getReceiverLastName());
		exportBean.setCompanyName(shipment.getToAddress().getCompanyName());
		exportBean.setAddressLine1(shipment.getToAddress().getAddressLine1());
		exportBean.setAddressLine2(shipment.getToAddress().getAddressLine2());
		exportBean.setCity(shipment.getToAddress().getCity());
		exportBean.setProvinceorState(shipment.getToAddress().getState());
		exportBean.setPostalCodeorZipCode(shipment.getToAddress().getZipCode());
		exportBean.setCountryCode(shipment.getToAddress().getCountry());
		exportBean.setClientVoicePhone(shipment.getToAddress().getPhone());
		exportBean.setClientEmailAddress(shipment.getToAddress().getEmail());
		Float totalWeight = UnitConverterUtil.convertLbToKg(shipment.getTotalWeight()) * 1000;
		exportBean.setWeight(totalWeight.toString());
		exportBean.setLength(UnitConverterUtil.convertInchToCm(shipment.getPackages().get(0).getLength()).toString());
		exportBean.setWidth(UnitConverterUtil.convertInchToCm(shipment.getPackages().get(0).getWidth()).toString());
		exportBean.setHeight(UnitConverterUtil.convertInchToCm(shipment.getPackages().get(0).getHeight()).toString());
		
		return exportBean;
	}

	public Map<String, String> getServiceCodes() {
		return serviceCodes;
	}

	public void setServiceCodes(Map<String, String> serviceCodes) {
		this.serviceCodes = serviceCodes;
	}
}
