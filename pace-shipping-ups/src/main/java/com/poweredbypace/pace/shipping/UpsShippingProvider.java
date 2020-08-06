package com.poweredbypace.pace.shipping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShipmentAddress;
import com.poweredbypace.pace.domain.shipping.ShippingOption;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;
import com.poweredbypace.pace.util.AddressUtil;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateErrorMessage;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.AddressType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.CodeDescriptionTypeE;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.DimensionsType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.ErrorDetailType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.PackageType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.PackageWeightType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.RateRequest;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.RateResponse;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.RatedShipmentType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.RequestType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.ServiceAccessToken_type0;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.ShipFromType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.ShipToAddressType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.ShipToType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.ShipmentType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.ShipperType;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.UPSSecurity;
import com.ups.www.wsdl.xoltws.rate.v1_1.RateServiceStub.UsernameToken_type0;

public class UpsShippingProvider extends ShippingProvider {

	private final Log log = LogFactory.getLog(UpsShippingProvider.class);
	
	private String accesskey;
	private String username;
	private String password;
	private String rateEndpoint;
	private String trackEndpoint;
	private String shipEndpoint;
	private String shipperNumber;
	private Map<String, String> serviceNames;
	private UpsTrackingService trackingService = new UpsTrackingService(this);
	
	public UpsShippingProvider(String providerId) {
		super(providerId);
	}

	public RateShippingResponse rate(Shipment shipment) {
		RateShippingResponse response = new RateShippingResponse();
		response.setShipment(shipment);
		RateResponse rateResponse = null;
		try {
			RateServiceStub rateServiceStub = new RateServiceStub(getRateEndpoint());
			rateResponse = rateServiceStub.processRate(populateRateRequest(shipment), populateUPSSecurity() );
		} catch (RateErrorMessage e) {
			StringBuffer buf = new StringBuffer();
			for(ErrorDetailType o : e.getFaultMessage().getErrorDetail()) {
				buf.append("UPS rate ws error: [SEVERITY=");
				buf.append(o.getSeverity());
				buf.append(", CODE=");
				buf.append(o.getPrimaryErrorCode().getCode());
				buf.append(", DESC=");
				buf.append(o.getPrimaryErrorCode().getDescription());
			}
			log.error(buf.toString(), e);
			response.setMessage(buf.toString());
			response.setResponseEnum(ShippingResponse.ResponseEnum.UNCATEGORIZED);
		} catch (Exception e) {
			log.error(e, e);
			response.setMessage(e.getMessage());
			response.setResponseEnum(ShippingResponse.ResponseEnum.UNCATEGORIZED);
		}
		if(rateResponse != null) {
			for(RatedShipmentType rateShipment : rateResponse.getRatedShipment()) {
				RateShippingResponseEntry entry = new RateShippingResponseEntry();
				Money money = new Money(Float.parseFloat(rateShipment.getTotalCharges().getMonetaryValue()), rateShipment.getTotalCharges().getCurrencyCode());
				entry.setMoney(money);
				ShippingOption upsShippingOption = new ShippingOption();
				upsShippingOption.setCode(rateShipment.getService().getCode());
				upsShippingOption.setName(getServiceName(rateShipment.getService().getCode()));
				upsShippingOption.setProviderId(getProviderId());
				entry.setShippingOption(upsShippingOption);
				response.getEntries().add(entry);
			}
			response.setShipperName("UPS");
			response.setResponseEnum(ShippingResponse.ResponseEnum.OK);
		}
		return response;
	}

	public ShippingResponse ship(Shipment shipment, ShippingOption shippingOption) {
		return null;

	}

	private String getServiceName(String code) {
		for(String key : getServiceNames().keySet()) {
			if(code.equals(key)) return getServiceNames().get(key);
		}
		return null;
	}
	
	private RateRequest populateRateRequest(Shipment shipment){
		RateRequest rateRequest = new RateRequest();
		RequestType request = new RequestType();

		ShippingOption shippingOption = shipment.getShippingOption();

		String[] requestOption = new String[] { "shop" };
		if(shippingOption != null) {
			requestOption = new String[] { "rate" };
		}
		request.setRequestOption(requestOption);
		rateRequest.setRequest(request);

		ShipmentType shpmnt = new ShipmentType();

		/** *******Shipper*********************/
		ShipperType shipper = new ShipperType();
		ShipmentAddress billingAddress = shipment.getBillingAddress();
		shipper.setName(billingAddress.getFullName());
		shipper.setShipperNumber(getShipperNumber());
		AddressType shipperAddress = new AddressType();
		String[] addressLines = { billingAddress.getAddressLine1(), billingAddress.getAddressLine2() };
		shipperAddress.setAddressLine(addressLines);
		shipperAddress.setCity(billingAddress.getCity());
		shipperAddress.setPostalCode(billingAddress.getZipCode());
		shipperAddress.setStateProvinceCode(billingAddress.getState());
		shipperAddress.setCountryCode(billingAddress.getCountry());
		shipper.setAddress(shipperAddress);
		shpmnt.setShipper(shipper);
		/** ******Shipper**********************/

		/** ************ShipFrom*******************/
		ShipmentAddress fromAddress = shipment.getFromAddress();
		ShipFromType shipFrom = new ShipFromType();
		shipFrom.setName(fromAddress.getFullName());
		AddressType shipFromAddress = new AddressType();
		shipFromAddress.setAddressLine(addressLines);
		shipFromAddress.setCity(fromAddress.getFullName());
		shipFromAddress.setPostalCode(fromAddress.getZipCode());
		shipFromAddress.setStateProvinceCode(fromAddress.getState());
		shipFromAddress.setCountryCode(fromAddress.getCountry());
		shipFrom.setAddress(shipFromAddress);
		shpmnt.setShipFrom(shipFrom);
		/** ***********ShipFrom**********************/

		/** ************ShipTo*******************/
		ShipToType shipTo = new ShipToType();
		ShipmentAddress toAddress = shipment.getToAddress();
		shipTo.setName(toAddress.getFullName());
		ShipToAddressType shipToAddress = new ShipToAddressType();
		String[] shipToAddressLines = { toAddress.getAddressLine1(), toAddress.getAddressLine2() };
		shipToAddress.setAddressLine(shipToAddressLines);
		shipToAddress.setCity(toAddress.getCity());
		shipToAddress.setPostalCode(AddressUtil.getCleanZipcode(toAddress.getZipCode()));
		shipToAddress.setStateProvinceCode(toAddress.getState());
		shipToAddress.setCountryCode(toAddress.getCountry());
		shipTo.setAddress(shipToAddress);
		shpmnt.setShipTo(shipTo);
		/** ***********ShipTo**********************/

		/**********Service********************** */
		if(shippingOption != null) {
			CodeDescriptionTypeE service = new CodeDescriptionTypeE();
			service.setCode(shippingOption.getCode());
			service.setDescription(shippingOption.getName());
			shpmnt.setService(service);
		}
		/** ********Service***********************/

		/********************Package***************** */
		List<PackageType> pckgs = new ArrayList<PackageType>();
		for(ShippingPackage shippingPackage : shipment.getPackages()) {
			PackageType pkg1 = new PackageType();
			CodeDescriptionTypeE pkgingType = new CodeDescriptionTypeE();
			pkgingType.setCode("02");
			pkgingType.setDescription("Package");
			pkg1.setPackagingType(pkgingType);
			PackageWeightType pkgWeight = new PackageWeightType();
			CodeDescriptionTypeE UOMType = new CodeDescriptionTypeE();
			UOMType.setCode("lbs");
			UOMType.setDescription("Kilograms");
			pkgWeight.setUnitOfMeasurement(UOMType);
			pkgWeight.setWeight(shippingPackage.getWeight().toString());
			pkg1.setPackageWeight(pkgWeight);
			CodeDescriptionTypeE dimensionsUOMType = new CodeDescriptionTypeE();
			dimensionsUOMType.setCode("IN");
			if(shippingPackage.getLength() != null && shippingPackage.getWidth() != null && shippingPackage.getHeight() != null) {
				DimensionsType dimensionsType = new DimensionsType();
				dimensionsType.setLength(shippingPackage.getLength().toString());
				dimensionsType.setWidth(shippingPackage.getWidth().toString());
				dimensionsType.setHeight(shippingPackage.getHeight().toString());
				dimensionsType.setUnitOfMeasurement(dimensionsUOMType);
				pkg1.setDimensions(dimensionsType);
			}
			pckgs.add(pkg1);
		}
		shpmnt.setPackage(pckgs.toArray(new PackageType[pckgs.size()]));
		/********************Package******************/
		rateRequest.setShipment(shpmnt);

		return rateRequest;
	}

	private UPSSecurity populateUPSSecurity(){

		UPSSecurity upss = new UPSSecurity();
		ServiceAccessToken_type0 upsSvcToken = new ServiceAccessToken_type0();
		upsSvcToken.setAccessLicenseNumber(getAccesskey());
		upss.setServiceAccessToken(upsSvcToken);
		UsernameToken_type0 upsSecUsrnameToken = new UsernameToken_type0();
		upsSecUsrnameToken.setUsername(getUsername());
		upsSecUsrnameToken.setPassword(getPassword());
		upss.setUsernameToken(upsSecUsrnameToken);

		return upss;
	}

	public String getAccesskey() {
		return accesskey;
	}

	public void setAccesskey(String accesskey) {
		this.accesskey = accesskey;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRateEndpoint() {
		return rateEndpoint;
	}

	public void setRateEndpoint(String rateEndpoint) {
		this.rateEndpoint = rateEndpoint;
	}

	public String getShipperNumber() {
		return shipperNumber;
	}

	public void setShipperNumber(String shipperNumber) {
		this.shipperNumber = shipperNumber;
	}

	public Map<String, String> getServiceNames() {
		return serviceNames;
	}

	public void setServiceNames(Map<String, String> serviceNames) {
		this.serviceNames = serviceNames;
	}
	
	public String getTrackEndpoint() {
		return trackEndpoint;
	}

	public void setTrackEndpoint(String trackEndpoint) {
		this.trackEndpoint = trackEndpoint;
	}
	
	public String getShipEndpoint() {
		return shipEndpoint;
	}

	public void setShipEndpoint(String shipEndpoint) {
		this.shipEndpoint = shipEndpoint;
	}

	@Override
	public TrackingResponse track(String trackingId) {
		return trackingService.track(trackingId);
	}
	
	
}
