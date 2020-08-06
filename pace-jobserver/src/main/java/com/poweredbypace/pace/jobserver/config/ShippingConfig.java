package com.poweredbypace.pace.jobserver.config;

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
import com.poweredbypace.pace.shipping.PackagePerShipmentCreateStrategy;
import com.poweredbypace.pace.shipping.PackingStrategy;
import com.poweredbypace.pace.shipping.ShipmentCreateStrategy;
import com.poweredbypace.pace.shipping.ShippingProvider;
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
//			flatRateShipping(),
//			shippingRateRuleShipping()
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
		return cp;
	}
	
	@Bean
	public PackingStrategy packingStrategy() {
		SimplePackingStrategy strategy = new SimplePackingStrategy();
		return strategy;
	}
}
