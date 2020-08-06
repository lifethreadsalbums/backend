package com.poweredbypace.pace.shipping;

import java.util.Map;

import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.ObjectFactory;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.PackageModel;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.PackagesModel;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.ProducerModel;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.ShipFromModel;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.ShipToModel;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.ShipmentInformationModel;
import com.ups.xmlschema.ct.worldship.impexp.shipmentimport.v1_0_0.ShipmentModel;

public class WorldshipShipmentConverter {

	private static final String PARAM_NAME_SERVICE_CODES = "serviceCodes";
	private static final String PARAM_NAME_SHIPPER_NUMBER = "shipperNumber";

	public ShipmentModel convert(Shipment shipment, Map<String, Object> params) {
		ObjectFactory objectFactory = new ObjectFactory();
		ShipmentModel shipmentModel = objectFactory.createShipmentModel();

		ShipToModel shipToModel = objectFactory.createShipToModel();
		shipToModel.setCompanyOrName(shipment.getToAddress().getFullName());
		shipToModel.setAddress1(shipment.getToAddress().getAddressLine1());
		shipToModel.setAddress2(shipment.getToAddress().getAddressLine2());
		shipToModel.setCountryTerritory(shipment.getToAddress().getCountry());
		shipToModel.setCityOrTown(shipment.getToAddress().getCity());
		shipToModel.setStateProvinceCounty(shipment.getToAddress().getState());
		shipToModel.setPostalCode(shipment.getToAddress().getZipCode());
		shipToModel.setTelephone(shipment.getToAddress().getPhone());
		shipmentModel.setShipTo(shipToModel);
		
		ShipFromModel shipFromModel = objectFactory.createShipFromModel();
		shipFromModel.setCompanyOrName(shipment.getFromAddress().getFullName());
		shipFromModel.setAddress1(shipment.getFromAddress().getAddressLine1());
		shipFromModel.setAddress2(shipment.getFromAddress().getAddressLine2());
		shipFromModel.setCountryTerritory(shipment.getFromAddress().getCountry());
		shipFromModel.setCityOrTown(shipment.getFromAddress().getCity());
		shipFromModel.setStateProvinceCounty(shipment.getFromAddress().getState());
		shipFromModel.setPostalCode(shipment.getToAddress().getZipCode());
		shipFromModel.setTelephone(shipment.getFromAddress().getPhone());
		shipmentModel.setShipFrom(shipFromModel);
		
		ProducerModel producerModel = objectFactory.createProducerModel();
		producerModel.setCustomerID(getShipperNumber(params));
		producerModel.setCompanyOrName(shipment.getBillingAddress().getFullName());
		producerModel.setAddress1(shipment.getBillingAddress().getAddressLine1());
		producerModel.setAddress2(shipment.getBillingAddress().getAddressLine2());
		producerModel.setCountryTerritory(shipment.getBillingAddress().getCountry());
		producerModel.setCityOrTown(shipment.getBillingAddress().getCity());
		producerModel.setStateProvinceCounty(shipment.getBillingAddress().getState());
		shipFromModel.setPostalCode(shipment.getToAddress().getZipCode());
		producerModel.setTelephone(shipment.getBillingAddress().getPhone());
		shipmentModel.setProducer(producerModel);
		
		ShipmentInformationModel shipmentInformationModel = objectFactory.createShipmentInformationModel();
		shipmentInformationModel.setShipperNumber(getShipperNumber(params));
		shipmentInformationModel.setServiceType(getTranslatedServiceCode(params, shipment.getShippingOption().getCode()));
		shipmentInformationModel.setBillingOption("CP");
		shipmentInformationModel.setNumberOfPackages(String.valueOf(shipment.getPackages().size()));
		shipmentInformationModel.setActualWeight(shipment.getTotalWeight().toString());
		shipmentModel.setShipmentInformation(shipmentInformationModel);
		
		PackagesModel packagesModel = objectFactory.createPackagesModel();
		for(ShippingPackage shippingPackage : shipment.getPackages()) {
			PackageModel packageModel = objectFactory.createPackageModel();
			packageModel.setWeight( shippingPackage.getWeight()!=null ? shippingPackage.getWeight().toString() : "");
			packageModel.setHeight(shippingPackage.getHeight().toString());
			packageModel.setWidth(shippingPackage.getWidth().toString());
			packageModel.setLength(shippingPackage.getLength().toString());
			packagesModel.getPackage().add(packageModel);
		}
		shipmentModel.setPackages(packagesModel);
		
		return shipmentModel;
	}
	
	private String getShipperNumber(Map<String, Object> params) {
		return (String) params.get(PARAM_NAME_SHIPPER_NUMBER);
	}
	
	@SuppressWarnings("unchecked")
	private String getTranslatedServiceCode(Map<String, Object> params, String code) {
		Map<String, String> serviceCodes = (Map<String, String>)params.get(PARAM_NAME_SERVICE_CODES);
		return serviceCodes.get(code);
	}
}
