package com.poweredbypace.pace.service.impl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.ProductOptionDate;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;
import com.poweredbypace.pace.repository.OrderRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.repository.ShipmentRepository;
import com.poweredbypace.pace.repository.TShippingPackageTypeRepository;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.shipping.PackingStrategy;
import com.poweredbypace.pace.shipping.RateShippingResponse;
import com.poweredbypace.pace.shipping.ShipmentExporter;
import com.poweredbypace.pace.shipping.ShippingProvider;
import com.poweredbypace.pace.shipping.TrackingResponse;
import com.poweredbypace.pace.shipping.TrackingResponse.DeliveryStatus;

@Service
public class ShippingManager {
	
	private final Log log = LogFactory.getLog(ShippingManager.class);
	
	@Autowired
	ShipmentRepository shipmentRepo;
	
	@Autowired
	TShippingPackageTypeRepository packageRepo;
	
	@Resource(name="shippingProviders")
	List<ShippingProvider> shippingProviders;
	
	@Resource(name="trackingProviders")
	List<ShippingProvider> trackingProviders;
	
	@Autowired(required=false)
	PackingStrategy packingStrategy;
	
	@Autowired
	UserService userService;
	
	@Autowired
	EventService eventService;
	
	@Autowired
	ProductService productService;
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	OrderRepository orderRepo;
	
	public List<Shipment> createShipment(Order order) {
		Preconditions.checkNotNull(order.getShippingOption(), "shipping option not selected for order");
		
		ShippingProvider shippingProvider = findShippingProviderById(order.getShippingOption().getProviderId());
		return createShipment(order, shippingProvider);
	}
	
	public List<Shipment> createShipment(Order order, ShippingProvider shippingProvider) {
		List<ShippingPackage> packages = packingStrategy.pack(order);
		return shippingProvider.createShipment(order, packages);
	}
	
	public List<Shipment> createShipment(Order order, ShippingProvider shippingProvider, List<ShippingPackage> packages) {
		return shippingProvider.createShipment(order, packages);
	}
	
	public List<RateShippingResponse> rateShipment(Order order, ShippingProvider shippingProvider) {
		List<ShippingPackage> packages = packingStrategy.pack(order);
		return rateShipment(order, shippingProvider, packages);
	}
	
	public List<RateShippingResponse> rateShipment(Order order, ShippingProvider shippingProvider, List<ShippingPackage> packages) {
		List<Shipment> shipments = createShipment(order, shippingProvider, packages);
		List<RateShippingResponse> responses = new ArrayList<RateShippingResponse>();
		for(Shipment shipment : shipments) {
			RateShippingResponse response = shippingProvider.rate(shipment);
			if (response!=null)	responses.add(response);
		}
		return responses;
	}
	
	public List<ShippingProvider> getAvailableShippingProviders() {
		return shippingProviders;
	}
	
	public void storeShipment(List<Shipment> shipments) {
		shipmentRepo.save(shipments);
	}
	
	public void storeShipment(Shipment shipment) {
		shipmentRepo.save(shipment);
	}
	
	public ShippingProvider findShippingProviderForShipment(Shipment shipment) {
		String shippingProviderId = shipment.getShippingProviderId();
		if(shippingProviderId != null) {
			for(ShippingProvider provider : shippingProviders) {
				if(shippingProviderId.equals(provider.getProviderId())) return provider;
			}
		}
		return null;
	}
	
	public ShippingProvider findShippingProviderById(String providerId) {
		for(ShippingProvider provider : getAvailableShippingProviders()) {
			if(providerId.equals(provider.getProviderId())) {
				return provider;
			}
		}
		return null;
	}
	
	public String getExportFileExtension(String shippingProviderId) {
		ShippingProvider shippingProvider = findShippingProviderById(shippingProviderId);
		ShipmentExporter exporter = shippingProvider.getExporter();
		if(exporter != null) {
			return exporter.getFileType();
		}
		return null;
	}
	
	public void exportShipments(String shippingProviderId, List<Shipment> shipments, Writer writer) {
		ShippingProvider shippingProvider = findShippingProviderById(shippingProviderId);
		try {
			shippingProvider.getExporter().export(shipments, writer);
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	public void trackOrder(Order o) {
		//track products
		
	}
	
	private boolean isProductDelivered(Product p) {
		//track products
		log.info("Tracking product "+ p.getProductNumber());
		String trackingId = p.getTrackingId();
		if (trackingId==null) return false;
		for(ShippingProvider sp:trackingProviders) {
			TrackingResponse res = sp.track(trackingId);
			if (res!=null && res.getDeliveryStatus()==DeliveryStatus.Delivered) {
				log.info("Product "+p.getProductNumber()+" delivered");
				ProductOptionDate date = (ProductOptionDate) p.getProductOptionByCode("dateDelivered");
				if (date!=null && res.getDeliveryDate()!=null) {
					date.setValue(res.getDeliveryDate());
				}
				
				date = (ProductOptionDate) p.getProductOptionByCode("dateShipped");
				if (date!=null && res.getShippingDate()!=null) {
					date.setValue(res.getShippingDate());
				}
				return true;
			}
		}
		return false;
	}
	
	@Transactional
	public void trackProducts() {
		log.info("Tracking shipped products");
		ProductState[] states = { ProductState.Shipped };
		List<Order> orders = orderRepo.findByProductStates(states);
		for(Order o:orders) {
			List<Product> deliveredProducts = new ArrayList<Product>();
			for(OrderItem oi:o.getOrderItems()) {
				Product p = oi.getProduct();
				if (isProductDelivered(p)) {
					deliveredProducts.add(p);
				}
				productService.changeState(deliveredProducts, ProductState.Completed);
			}
		}
	}
	
}