package com.poweredbypace.pace.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.batch.OrderNumberingStrategy;
import com.poweredbypace.pace.batch.PACEOrderNumberingStrategy;
import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.Address.AddressType;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.PrototypeProduct.ProductType;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.order.Invoice;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.shipping.Shipment;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.event.OrderCreatedEvent;
import com.poweredbypace.pace.event.ProductPurchasedEvent;
import com.poweredbypace.pace.exception.ProductNameExistsException;
import com.poweredbypace.pace.expression.ExpressionEvaluator;
import com.poweredbypace.pace.expression.impl.OrderContext;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.repository.AddressRepository;
import com.poweredbypace.pace.repository.InvoiceRepository;
import com.poweredbypace.pace.repository.OrderItemRepository;
import com.poweredbypace.pace.repository.OrderRepository;
import com.poweredbypace.pace.repository.ProductRepository;
import com.poweredbypace.pace.repository.ShipmentRepository;
import com.poweredbypace.pace.repository.TransactionLogEntryRepository;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.InvoiceService;
import com.poweredbypace.pace.service.LayoutService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.PricingService;
import com.poweredbypace.pace.service.ProductService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.util.SpringContextUtil;


@Service
public class OrderServiceImpl implements OrderService {
	
	private final Log log = LogFactory.getLog(getClass());

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired(required=false)
	private ShippingManager shippingManager;
	
	@Autowired
	private ShipmentRepository shipmentRepository;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private LayoutService layoutService;
	
	@Autowired
	private TransactionLogEntryRepository transactionLogEntryRepo;
	
	@Autowired
	private InvoiceRepository invoiceRepo;
	
	@Autowired
	private GenericRuleService genericRuleService;
	
	@Autowired
	private ExpressionEvaluator expressionEvaluator;
	
	@Autowired
	private OrderItemRepository orderItemRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GenericRuleService ruleService;
	
	@Override
	public Order get(Long id) {
		return orderRepository.findOne(id);
	}
	
	@Override
	public Order createOrder(User user) {
		Order order = new Order();
		order.setUser(user);
		order.setInternalId(UUID.randomUUID().toString());
		order.setStore(SpringContextUtil.getEnv().getStore());
		//copy billing address
		if (user.getBillingAddress()!=null) {
			order.getAddresses().add(new Address(user.getBillingAddress()));	
		}
		//copy shipping address
		if (user.getShippingAddress()!=null) {
			order.getAddresses().add(new Address(user.getShippingAddress()));
		} else if (user.isShippingAddressSameAsBillingAddress()) {
			Address address = new Address(user.getBillingAddress());
			address.setAddressType(AddressType.ShippingAddress);
			order.getAddresses().add(address);
		}
		
		for(Address address:order.getAddresses()) {
			address.setCompanyName(user.getCompanyName());
			address.setFirstName(user.getFirstName());
			address.setLastName(user.getLastName());
			address.setPhone(user.getPhone());
			address.setEmail(user.getEmail());
		}
		return order;
	}
	
	@Override
	public Order getCart(User user) {
		//cart is an order in pending state, should always be one
		List<Order> orders = orderRepository.findByUserAndState(user, OrderState.Pending);
		if (orders!=null && orders.size()>0) {
			Order order = orders.get(0);
			
			if (order.getState()==OrderState.Pending && order.getOrderItems().size()==0) {
				//reset drop shipping address
				Address dropShippingAddress = order.getDropShippingAddress();
				if (dropShippingAddress!=null) {
					order.getAddresses().remove(dropShippingAddress);
					
					Address shippingAddress = null;
					//copy shipping address
					if (user.getShippingAddress()!=null) {
						shippingAddress = new Address(user.getShippingAddress());
					} else if (user.isShippingAddressSameAsBillingAddress()) {
						shippingAddress = new Address(user.getBillingAddress());
						shippingAddress.setAddressType(AddressType.ShippingAddress);
					}
					if (shippingAddress!=null) {
						shippingAddress.setCompanyName(user.getCompanyName());
						shippingAddress.setFirstName(user.getFirstName());
						shippingAddress.setLastName(user.getLastName());
						shippingAddress.setPhone(user.getPhone());
						shippingAddress.setEmail(user.getEmail());
						order.getAddresses().add(shippingAddress);
					}
					
					order = orderRepository.save(order);
					addressRepository.delete(dropShippingAddress);
				}
			}
			
			return order;
		}
		
		//create a new order if we cannot find any pending order
		Order order = createOrder(user);
		order.setState(OrderState.Pending);
		
		order = orderRepository.save(order);
		
		return order;
	}
	
	@Override
	public List<Order> getByUserAndState(User user, OrderState state) {
		return orderRepository.findByUserAndState(user, state);
	}
	
	@Override
	public Order save(Order order) {
		return orderRepository.save(order);
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public Order addToCart(List<Product> products) {
		assert(products!=null && products.size()>0);
		Order order = null;
		for(Product p:products) {
			order = this.addToCart(p);
		}
		return order;
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public Order addToCart(Product product) {
		User currentUser = userService.getCurrentUser();
		Order cart = getCart(currentUser);
		
		//don't allow to add duplicate only
		if (product.getParent()!=null) 
			product = product.getParent();
		
		//check if the project has been included in an order
		if (product.getId()!=null) {
			OrderItem prevItem = orderItemRepo.findByProduct(product);
			if (prevItem!=null)
				return cart;
		} else {
			product = productService.save(product);
		}
		
		if (!productService.checkUniqueName(product))
			throw new ProductNameExistsException();
		
		//check layouts
		if (product.getPrototypeProduct().getProductType()==ProductType.DesignableProduct ||
			product.getPrototypeProduct().getProductType()==ProductType.SinglePrintProduct) {
			layoutService.checkLayout(product);
			
			for(Product p:product.getChildren()) {
				layoutService.checkLayout(p);
			}
		}
		
		if (cart.getRush()) {
			for(Product p:product.getProductAndChildren())
				p.setRush(true);
		}
		
		//make sure pages are synced
		productService.updatePageCount(product);
		
		OrderItem orderItem = new OrderItem();
		orderItem.setProduct(product);
		orderItem.setOrder(cart);
		//initialize collection
		
		cart.getOrderItems().size();
		cart.getOrderItems().add(orderItem);
		
		if (cart.getOrderItems().size()==1)
			cart.setCouponCode(null); //reset coupon
		
		//calculate price and save order
		pricingService.executePricing(cart);
		updateFreeShipping(cart);
		
		save(cart);
		product.setOrderItem(orderItem);
		productRepository.save(product);
		
		return cart;
	}
	
	@Override
	public void deleteOrderItem(OrderItem oi) {
		Order o = oi.getOrder();
		o.getOrderItems().remove(oi);
		
		if (o.getState()==OrderState.Pending) {
			//reset shipping info
			o.setShippingCost(null);
			o.setShippingOption(null);
			updateFreeShipping(o);
		} else {
			assignOrderNumber(o);
		}
		
		orderRepository.save(o);
	}
	
	@Override
	public void deleteOrderItem(long id) {
		deleteOrderItem(orderItemRepository.findOne(id));
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void prepareForPayment(Order order) {
		
		//save order first to make sure there is no detached entities
		orderRepository.save(order);
		
		//check layouts
		for(OrderItem orderItem:order.getOrderItems()) {
			Product product = orderItem.getProduct();
			if (product.getPrototypeProduct().getProductType()==ProductType.DesignableProduct) {
				for(Product p:product.getProductAndChildren()) {
					layoutService.checkLayout(p);
				}
			}
		}
		
		//assign order number
		assignOrderNumber(order);
		
		//update page counts
		for(OrderItem orderItem:order.getOrderItems()) {
			productService.updatePageCount(orderItem.getProduct());
		}
		
		//update price
		pricingService.executePricing(order);
		
		for(OrderItem orderItem:order.getOrderItems()) {
			Product product = orderItem.getProduct();
			for(Product p:product.getProductAndChildren()) {
				productRepository.save(p);
			}
		}
		
		orderRepository.save(order);
		
		//if total==0 skip the payment and make the order PaymentComplete
		if (order.getTotal().getAmount().compareTo(BigDecimal.ZERO) == 0) {
			setOrderState(order, OrderState.PaymentComplete);
		}
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public void setOrderState(Order order, OrderState state) {
		
		//transition from PendingPayment to Processed
		if (order.getState()==OrderState.Pending && state==OrderState.PaymentComplete) {
			
			if (order.getOrderNumber()==null) {
				assignOrderNumber(order);
			}
			
			for(OrderItem orderItem:order.getOrderItems()) {
				Product product = orderItem.getProduct();
				
				if (product.getPrototypeProduct().getProductType()==ProductType.SinglePrintProduct) {
					List<Layout> layouts = new ArrayList<Layout>();
					Layout layout = product.getLayout();
					for(Product p:product.getChildren()) {
						Layout l = p.getLayout();
						if (l!=null) layouts.add(l);
					}
					try {
						layoutService.deleteUnusedImages(layout.getFilmStrip(), layouts);
					} catch (Exception ex) {
						log.error("Error while deleting images", ex);
					}
				}
				
				for(Product p:product.getProductAndChildren()) {
					p.setState(ProductState.Preflight);
					
					//update carrier
					try {
						if (order.getShippingOption()!=null) {
							String shippingOptionCode = order.getShippingOption().getCode();
							p.setProductOptionCode("carrier", shippingOptionCode);
							
							if ("LOCAL_PICKUP".equals(shippingOptionCode)) {
								p.setProductOptionValue("trackingId", p.getProductNumber());
							}
						}
					} catch (Exception ex) {
						log.error("Cannot set shipping info for product " + p.getProductNumber(), ex);
					}
					productRepository.save(p);
					
					
					Layout layout = p.getLayout();
					if (layout!=null) { 
						layout.setLocked(true);
						if (p.getPrototypeProduct().getProductType()!=ProductType.SinglePrintProduct) {
							try {
								layoutService.deleteUnusedImages(layout);
							} catch (Exception ex) {
								log.error("Error while deleting images", ex);
							}
												
							Layout coverLayout = p.getCoverLayout();
							if (coverLayout!=null) coverLayout.setLocked(true);
						}
						layoutService.save(layout);
					}
					
					//send app event
					eventService.sendEvent(new ProductPurchasedEvent(p));
				}
				
			}
			
			order.setState(state);
			order.setDateCreated(new Date());
			//check shipping address
			if (order.getShippingAddress()==null) {
				Address shippingAddress = new Address(order.getBillingAddress());
				shippingAddress.setAddressType(AddressType.ShippingAddress);
				order.getAddresses().add(shippingAddress);
			}
			
			if (order.getShippingOption()!=null && 
				BooleanUtils.isTrue(order.getShippingOption().getFreeShipping())) {
				order.setFreeShipping(true);
			}
			
			orderRepository.save(order);
			
			//create invoice
			Invoice invoice = invoiceService.create(order);
			invoiceService.emailInvoice(invoice);
			
			//create shipment
			if (BooleanUtils.isFalse(order.getFreeShipping())) {
				List<Shipment> shipments = shippingManager.createShipment(order);
				shipmentRepository.save(shipments);
			}
			
			//broadcast notification
			notificationBroadcaster.broadcast(Notification.create(NotificationType.OrderCreated, order));
			
			//send app event
			eventService.sendEvent(new OrderCreatedEvent(order));
		}
		
		if (order.getState()==OrderState.PaymentComplete && state==OrderState.OnHold) {
			
		}
		
		if (order.getState()==OrderState.PaymentComplete && state==OrderState.Canceled) {
			
		}
		
		if ((order.getState()==OrderState.PaymentComplete && state==OrderState.Completed) ||
			(order.getState()==OrderState.PaymentComplete && state==OrderState.Shipped) ||
			(order.getState()==OrderState.Shipped && state==OrderState.Completed)){
			
			ProductState productState = state==OrderState.Shipped ? ProductState.Shipped : ProductState.Completed;
			
			for(OrderItem orderItem:order.getOrderItems()) {
				Product product = orderItem.getProduct();
				for(Product p:product.getProductAndChildren()) {
					p.setState(productState);
					productRepository.save(p);
				}
			}
			
			order.setState(state);
			orderRepository.save(order);
		}
		
	}

	@Override
	public Order getByOrderNumber(String orderNumber) {
		return orderRepository.findByOrderNumber(orderNumber);
	}

	@Override
	@Transactional
	public void delete(Order order) {
		transactionLogEntryRepo.delete(transactionLogEntryRepo.findByOrder(order));
		shipmentRepository.delete(shipmentRepository.findByOrder(order));
		Invoice invoice = invoiceRepo.findByOrder(order);
		if (invoice!=null) {
			invoiceRepo.delete(invoice);
		}
		orderRepository.delete(order);
	}

	@Override
	@Transactional
	public void delete(List<Order> orders) {
		for(Order o:orders) {
			delete(o);
		}
	}
	
	@Override
	@Transactional(value=TxType.REQUIRED)
	public Order markAsPaid(Product product) {
		User user = product.getUser();
		Order order = createOrder(user);
		order.setState(OrderState.Pending);
		order.setDateCreated(new Date());
		order.setFreeShipping(true);
		//don't allow to add duplicate only
		if (product.getParent()!=null) 
			product = product.getParent();
		
		//check if the project has been included in an order
		if (product.getId()!=null) {
			OrderItem prevItem = orderItemRepo.findByProduct(product);
			if (prevItem!=null) {
				product.setOrderItem(null);
				orderItemRepository.delete(prevItem);
			}
		} 
		
		OrderItem orderItem = new OrderItem();
		orderItem.setProduct(product);
		orderItem.setOrder(order);
		//initialize collection
		
		order.getOrderItems().size();
		order.getOrderItems().add(orderItem);
		
		//calculate price and save order
		pricingService.executePricing(order);
		save(order);
		
		product.setOrderItem(orderItem);
		productRepository.save(product);
		
		setOrderState(order, OrderState.PaymentComplete);
		return order;
	}
	
	public void assignOrderNumber(Order o) {
		getOrderNumberingStrategy(o).assignOrderNumber(o);
	}
	
	private OrderNumberingStrategy getOrderNumberingStrategy(Order order) {
		OrderNumberingStrategy orderNumberingStrategy = null;
		try {
			OrderContext ctx = new OrderContext(order);
			GenericRule rule = genericRuleService.findRule(ctx, "ORDER_NUMBERING_STRATEGY_INSTANCE");
			if (rule!=null) {
				orderNumberingStrategy = expressionEvaluator.evaluate(ctx, rule.getJsonData(), OrderNumberingStrategy.class);
			}
		} catch (Exception e) { 
			log.error("Error", e);
		}
		
		if (orderNumberingStrategy==null) {
			log.debug("Using default PACEOrderNumberingStrategy");
			orderNumberingStrategy = SpringContextUtil.getApplicationContext().getBean(PACEOrderNumberingStrategy.class);
		}
		
		return orderNumberingStrategy;
	}
	
	private void updateFreeShipping(Order o) {
		boolean freeShipping = true;
		for(OrderItem item:o.getOrderItems()) {
			if (BooleanUtils.isFalse(item.getProduct().getPrototypeProduct().getFreeShipping())) {
				freeShipping = false;
				break;
			}
		}
		if (!freeShipping) {
			try {
				OrderContext ctx = new OrderContext(o);
				Boolean value = ruleService.getRuleValue(ctx, GenericRule.FREE_SHIPPING, Boolean.class);
				if (BooleanUtils.isTrue(value))
					freeShipping = true;
			} catch(Exception ex) {
				log.warn("Cannot calculate free shipping rule", ex);
			}
		}
		
		o.setFreeShipping(freeShipping);
	}

}
