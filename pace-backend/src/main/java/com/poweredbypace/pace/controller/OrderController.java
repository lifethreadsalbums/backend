package com.poweredbypace.pace.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.poweredbypace.pace.util.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.ProductPrice;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.shipping.ShippingPackage;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.exception.IllegalArgumentException;
import com.poweredbypace.pace.exception.ResourceNotFoundException;
import com.poweredbypace.pace.json.View;
import com.poweredbypace.pace.repository.OrderRepository;
import com.poweredbypace.pace.service.BatchService;
import com.poweredbypace.pace.service.OrderService;
import com.poweredbypace.pace.service.PricingService;
import com.poweredbypace.pace.service.impl.ShippingManager;
import com.poweredbypace.pace.shipping.PackingStrategy;
import com.poweredbypace.pace.shipping.RateShippingResponse;
import com.poweredbypace.pace.shipping.RateShippingResponseEntry;
import com.poweredbypace.pace.shipping.ShippingProvider;

@Controller
@RequestMapping("/api/order")
public class OrderController {
	
	private final Log log = LogFactory.getLog(getClass());
	
	private final Sort DEFAULT_SORT = new Sort(Direction.DESC, "dateCreated");
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@Autowired
	private PricingService pricingService;
	
	@Autowired
	private ShippingManager shippingManager;
	
	@Autowired
	private PackingStrategy packingStrategy;
	
	@Autowired
	private BatchService batchService;
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseBody
	public Order getOrder(@PathVariable long id) {
		return orderRepo.findOne(id);
	}
	
	@RequestMapping(value = "/status", method = RequestMethod.POST)
	@ResponseBody
	public OrderStatusDto setOrderStatus(@RequestBody OrderStatusDto orderDto) {
		Order o = orderRepo.findByOrderNumber(orderDto.orderId);
		if (o==null) {
			throw new ResourceNotFoundException("Order not found.");
		}
		if (orderDto.status!=OrderState.Shipped && orderDto.status!=OrderState.Completed) {
			throw new IllegalArgumentException("Order status can be either Shipped or Complete");
		}
		orderService.setOrderState(o, orderDto.status);
		return orderDto;
	}
	
	@RequestMapping(value = "", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@JsonView(View.OrderShortInfo.class)
	public List<Order> getOrders(
			@RequestParam(required=false) OrderState state,
			@RequestParam(required=true) Integer pageIndex,
			@RequestParam(required=true) Integer pageSize) {
		
		OrderState oState = state!=null ? state : OrderState.PaymentComplete;

		return orderRepo.findByState(oState, 
			new PageRequest(pageIndex, pageSize, DEFAULT_SORT));
	}

    @RequestMapping(value = "historyByDate", params = {"fromDate", "toDate", "orderStates", "pageSize", "pageIndex"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    @JsonView(View.OrderShortInfo.class)
    public List<Order> getOrdersByDates(
            @RequestParam("orderStates") OrderState[] states,
            @RequestParam(value = "fromDate", required = true) String fromDate,
            @RequestParam(value = "toDate", required = true) String toDate,
            @RequestParam("pageIndex") Integer pageIndex,
            @RequestParam("pageSize") Integer pageSize) throws ParseException {
        Date dateFrom = DateUtils.getFormattedDate(fromDate, "yyyy-MM-dd");
        Date dateTo = DateUtils.getFormattedDate(toDate, "yyyy-MM-dd");
        return orderRepo.findByOrderDatesAndStates(dateFrom, dateTo, states, new PageRequest(pageIndex, pageSize, DEFAULT_SORT));
    }

	@RequestMapping(value = "", params={"orderStates"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@JsonView(View.OrderShortInfo.class)
	public List<Order> getOrders(
			@RequestParam("orderStates") OrderState[] states,
			@RequestParam(value="q", required=false) String q,
			@RequestParam("pageIndex") Integer pageIndex,
			@RequestParam("pageSize") Integer pageSize) {
		
		if (q==null) q = "";
		q = q.replaceAll("\\#", "");
		if (q.indexOf("$")>=0) {
			//find money range
			Pattern p = Pattern.compile("[\\>\\<]?\\$\\d+");
			Matcher matcher = p.matcher(q);
			int i=0;
			BigDecimal from = BigDecimal.ZERO;
			BigDecimal to = BigDecimal.valueOf(Long.MAX_VALUE);
			while (matcher.find()) {
				String grp = matcher.group();
				BigDecimal val = BigDecimal.valueOf(Long.parseLong(grp.replaceAll("[^\\d]", "")));
				if (grp.startsWith(">") || (i==0 && !grp.startsWith("<")) ) {
					from = val;
				} else if (grp.startsWith("<") || (i==1 && !grp.startsWith(">")) ) {
					to = val;
				}
				i++;
			}
			return orderRepo.findByOrderStatesAndTotal(states, from, to, new PageRequest(pageIndex, pageSize, DEFAULT_SORT));
		}
		return orderRepo.findByQueryAndOrderStates(q, states, new PageRequest(pageIndex, pageSize, DEFAULT_SORT));
	}
	
	
	@RequestMapping(value = "", params={"productStates"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	@JsonView(View.OrderShortInfo.class)
	public List<Order> getOrders(
			@RequestParam("productStates") ProductState[] states,
			@RequestParam(value="q", required=false) String q,
			@RequestParam("pageIndex") Integer pageIndex,
			@RequestParam("pageSize") Integer pageSize) {
		
		if (q!=null) {
			q = q.replaceAll("\\#", "");
			return orderRepo.findByQueryAndProductStates(q, states, 
				new PageRequest(pageIndex, pageSize, new Sort(Direction.DESC, "orderNumber")));
		} else {
			return orderRepo.findByProductStates(states, 
				new PageRequest(pageIndex, pageSize, new Sort(Direction.DESC, "orderNumber")));
		}
	}
	
	@RequestMapping(value = "", params={"batchId"}, method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public List<Order> getOrdersByBatch(
			@RequestParam("batchId") Long batchId,
			@RequestParam("pageIndex") Integer pageIndex,
			@RequestParam("pageSize") Integer pageSize) {
		
		if (batchId==0) {
			Batch batch = batchService.getPendingBatch();
			batchId = batch.getId();
		}
		
		List<Order> orders = orderRepo.findByBatch(batchId, 
				new PageRequest(pageIndex, pageSize, DEFAULT_SORT));
		
		return orders;
	}
	
	@RequestMapping(value = "/count", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public Map<String, Object> getOrderCount(@RequestParam(required=false) OrderState state) {
		OrderState oState = state!=null ? state : OrderState.PaymentComplete;
		
		Map<String, Object> result = new HashMap<String, Object>();
		List<Order> orders = orderRepo.findByState(oState);
		result.put("count", orders.size());
		
		return result;
	}
	
	@RequestMapping(value="/price", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Order calculatePrice(@RequestBody Product product, @AuthenticationPrincipal User currentUser) {
		
		Order order = makeOrder(product, currentUser);
		try {
			pricingService.executePricing(order);
		} catch (Exception e) {
			log.error("Cannot calculate price. " + e.getMessage());
			resetPrice(order);
		}
		
		return order;
	}
	
	@RequestMapping(value="/shippingPrice", method = RequestMethod.POST, consumes="application/json")
	@ResponseBody
	public Order calculateShippingPrice(@RequestBody Product product, @AuthenticationPrincipal User currentUser) {
		Order order = makeOrder(product, currentUser);
		try {
						
			List<ShippingPackage> packages = packingStrategy.pack(order);
			ShippingProvider p = shippingManager.findShippingProviderById("UPS");
			
			List<RateShippingResponse> response = shippingManager.rateShipment(order, p, packages);
			for(RateShippingResponse rate:response) {
				for(RateShippingResponseEntry entry:rate.getEntries()) {
					if (order.getShippingOption()==null) {
						order.setShippingCost(entry.getMoney());
						order.setShippingOption(entry.getShippingOption());
					}
				}
			}
			pricingService.executePricing(order);

		} catch (Exception e) {
			log.error("Cannot calculate shipping price. " + e.getMessage());
			resetPrice(order);
		}
		return order;
	}
	
	
//	@RequestMapping(value = "", method = RequestMethod.POST, consumes="application/json", produces="application/json")
//	@ResponseBody
//	public Order save(@RequestBody Order order) {
//		Order o = orderService.save(order);
//		return o;
//	}
	
	private Order makeOrder(Product product, User user) {
		Order order = orderService.createOrder(user);
		product.setUser(user);
		for(Product child:product.getChildren())
			child.setUser(user);
		
		OrderItem orderItem = new OrderItem();
		orderItem.setProduct(product);
		orderItem.setOrder(order);
		
		order.getOrderItems().add(orderItem);
		order.setShippingCost(new Money(0.0f));
		return order;
	}
	
	private void resetPrice(Order order) {
		ProductPrice price = new ProductPrice();
		price.setSubtotal(new Money(0.0f));
		price.setSubtotalIncludingAdjustements(new Money(0.0f));
		for(OrderItem item:order.getOrderItems()) {
			Product p = item.getProduct();
			p.setSubtotal(new Money(0.0f));
			p.getProductPrices().clear();
			p.getProductPrices().add(price);
		}
		order.setSubtotal(new Money(0.0f));
		order.setTotal(new Money(0.0f));
		order.setShippingCost(new Money(0.0f));
		order.setShippingOption(null);
	}
	
	public static class OrderStatusDto {
		public String orderId;
		public OrderState status;
	}
	
}
