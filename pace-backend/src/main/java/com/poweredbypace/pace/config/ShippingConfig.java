package com.poweredbypace.pace.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.poweredbypace.pace.CanadaPostShippingProvider;
import com.poweredbypace.pace.service.impl.SimplePackingStrategy;
import com.poweredbypace.pace.shipping.AllInOneShipmentCreateStrategy;
import com.poweredbypace.pace.shipping.FlatRateShippingProvider;
import com.poweredbypace.pace.shipping.PackagePerShipmentCreateStrategy;
import com.poweredbypace.pace.shipping.PackingStrategy;
import com.poweredbypace.pace.shipping.ShipmentCreateStrategy;
import com.poweredbypace.pace.shipping.ShippingProvider;
import com.poweredbypace.pace.shipping.ShippingRateRulesShippingProvider;
import com.poweredbypace.pace.shipping.UpsShippingProvider;

@Configuration
public class ShippingConfig {

	@Value("${ups.accesskey}") private String upsAccessKey;
	@Value("${ups.username}") private String upsUsername;
	@Value("${ups.password}") private String upsPassword;
	@Value("${ups.rate.endpoint}") private String upsRateEndpoint;
	@Value("${ups.ship.endpoint}") private String upsShipEndpoint;
	@Value("${ups.track.endpoint}") private String upsTrackEndpoint;
	@Value("${ups.shippernumber}") private String upsShipperNumber;
	@Value("${ups.trackingEnabled}") private boolean upsTrackingEnabled;
	
	@Value("${canadapost.username}") private String cpUsername;
	@Value("${canadapost.password}") private String cpPassword;
	@Value("${canadapost.mailedBy}") private String cpMailedBy;
	@Value("${canadapost.contract}") private String cpContract;
	@Value("${canadapost.trackingEnabled}") private boolean cpTrackingEnabled;
	
	@Bean(name="shippingProviders")
	public List<ShippingProvider> shippingProviders() {
		return Arrays.asList(new ShippingProvider[] { 
			flatRateShipping(),
			shippingRateRuleShipping()
		});
	}
	
	@Bean(name="trackingProviders")
	public List<ShippingProvider> trackingProviders() {
		List<ShippingProvider> providers = new ArrayList<ShippingProvider>();
		if (upsTrackingEnabled) providers.add(upsShippingProvider());
		if (cpTrackingEnabled) providers.add(canadaPostShippingProvider());
		return providers;
	}
	
	@Bean
	public ShipmentCreateStrategy allInOneShipment() {
		return new AllInOneShipmentCreateStrategy();
	}
	
	@Bean
	public ShipmentCreateStrategy packagePerShipment() {
		return new PackagePerShipmentCreateStrategy();
	}
	
	@Bean
	public ShippingProvider upsShippingProvider() {
		UpsShippingProvider ups = new UpsShippingProvider("UPS");
		ups.setAccesskey(upsAccessKey);
		ups.setUsername(upsUsername);
		ups.setPassword(upsPassword);
		ups.setRateEndpoint(upsRateEndpoint);
		ups.setTrackEndpoint(upsTrackEndpoint);
		ups.setShipEndpoint(upsShipEndpoint);
		ups.setShipperNumber(upsShipperNumber);
		//ups.setShipmentCreateStrategy(allInOneShipment());
		//ups.setExporter(worldshipExporter());
		
		Map<String,String> serviceNames = new HashMap<String, String>();
		serviceNames.put("01", "UPS Express");
		serviceNames.put("02", "UPS Second Day Air®");
		serviceNames.put("03", "UPS Ground");
		serviceNames.put("07", "UPS Express");
		serviceNames.put("08", "UPS ExpeditedSM");
		serviceNames.put("11", "UPS Standard");
		serviceNames.put("12", "UPS Three-Day Select®");
		serviceNames.put("13", "UPS Next Day Air Saver®");
		serviceNames.put("14", "UPS Next Day Air® Early A.M. SM");
		serviceNames.put("54", "UPS Express Plus");
		serviceNames.put("59", "UPS Second Day Air A.M.®");
		serviceNames.put("65", "UPS Saver");
		serviceNames.put("82", "UPS Today StandardSM");
		serviceNames.put("83", "UPS Today Dedicated CourrierSM");
		serviceNames.put("85", "UPS Today Express");
		serviceNames.put("86", "UPS Today Express Saver");
		serviceNames.put("96", "UPS Worldwide Express Freight");
		serviceNames.put("M2", "UPS First-Class Mail");
		serviceNames.put("M3", "UPS Priority Mail");
		serviceNames.put("M4", "UPS Expedited Mail Innovations");
		serviceNames.put("M5", "UPS Priority Mail Innovations");
		serviceNames.put("M6", "UPS Economy Mail Innovations");
		
		ups.setServiceNames(serviceNames);
	
		return ups;
	}
//	
//	@Bean
//	public WorldshipShipmentConverter worldshipShipmentConverter() {
//		return new WorldshipShipmentConverter();
//	}
//	
//	@Bean
//	public WorldshipExporter worldshipExporter() {
//		WorldshipExporter exporter = new WorldshipExporter();
//		exporter.setConverter(worldshipShipmentConverter());
//		Map<String, Object> params = new HashMap<String, Object>();
//		params.put("shipperNumber", upsShipperNumber);
//		
//		Map<String,String> serviceCodes = new HashMap<String, String>();
//		serviceCodes.put("01", "ES");
//		serviceCodes.put("02", "2DA");
//		serviceCodes.put("03", "GND");
//		serviceCodes.put("07", "ES");
//		serviceCodes.put("08", "EX");
//		serviceCodes.put("11", "ST");
//		serviceCodes.put("12", "3DS");
//		serviceCodes.put("13", "1DP");
//		serviceCodes.put("14", "1DM");
//		serviceCodes.put("54", "EP");
//		serviceCodes.put("59", "2DM");
//		params.put("serviceCodes", serviceCodes);
//		
//		exporter.setParams(params);
//		return exporter;
//	}
	
	@Bean
	public ShippingProvider canadaPostShippingProvider() {
		CanadaPostShippingProvider cp = new CanadaPostShippingProvider("CANADA_POST");
		cp.setMailedBy(cpMailedBy);
		cp.setContract(cpContract);
		cp.setUsername(cpUsername);
		cp.setPassword(cpPassword);
		cp.setLabelFormat("8.5x11");
		cp.setMethodOfPayment("Account");
		cp.setShipmentCreateStrategy(allInOneShipment());
		//cp.setExporter(estExporter());
		return cp;
	}
	
	@Bean
	public ShippingProvider flatRateShipping() {
		FlatRateShippingProvider fr = new FlatRateShippingProvider("FLAT");
		fr.setShipmentCreateStrategy(allInOneShipment());
		return fr;
	}
	
	@Bean
	public ShippingProvider shippingRateRuleShipping() {
		ShippingRateRulesShippingProvider provider = new ShippingRateRulesShippingProvider("SHIPPING_RATE_RULES");
		provider.setShipmentCreateStrategy(allInOneShipment());
		return provider;
	}
	
//	@Bean
//	public EstShipmentConverter estShipmentConverter() {
//		EstShipmentConverter converter = new EstShipmentConverter();
//		Map<String,String> serviceCodes = new HashMap<String, String>();
//		serviceCodes.put("01", "ES");
//		
//		serviceCodes.put("DOM.RP", "966");
//		serviceCodes.put("DOM.EP", "967");
//		serviceCodes.put("DOM.XP", "908");
//		serviceCodes.put("DOM.PC", "1469");
//		serviceCodes.put("USA.EP", "1917");
//		serviceCodes.put("USA.PW.ENV", "10103");
//		serviceCodes.put("USA.PW.PAK", "10104");
//		serviceCodes.put("USA.PW.PARCEL", "10105");
//		serviceCodes.put("USA.SP.AIR", "1123");
//		serviceCodes.put("USA.XP", "1917");
//		serviceCodes.put("INT.XP", "6210");
//		serviceCodes.put("INT.IP.AIR", "985");
//		serviceCodes.put("INT.IP.SURF", "984");
//		serviceCodes.put("INT.PW.ENV", "10100");
//		serviceCodes.put("INT.PW.PAK", "10101");
//		serviceCodes.put("INT.PW.PARCEL", "10102");
//		serviceCodes.put("INT.SP.AIR", "9610");
//		serviceCodes.put("INT.SP.SURF", "9611");
//		converter.setServiceCodes(serviceCodes);
//		return converter;
//	}
//	
//	@Bean
//	public EstExporter estExporter() {
//		EstExporter exporter = new EstExporter();
//		exporter.setConverter(estShipmentConverter());
//		return exporter;
//	}
	
	@Bean
	public PackingStrategy packingStrategy() {
		SimplePackingStrategy strategy = new SimplePackingStrategy();
		return strategy;
	}
}
