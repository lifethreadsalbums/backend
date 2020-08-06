package com.poweredbypace.pace.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.service.OrderService;

public class SkipPaymentGateway implements PaymentGateway {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private Env env;
	
	private String getAbsUrl(String url) {
		Store store = env.getStore();
		return "https://" + store.getDomainName() + url;
	}
	
	public SkipPaymentGateway() {
	}

	@Override
	public PaymentGatewayConfiguration getConfiguration() {
		PaymentGatewayConfiguration config = new PaymentGatewayConfiguration();
		config.getFormFields().put("OrderID", "order.id");
		config.setHttpMethod("GET");
		config.setGatewayUrl(getAbsUrl("/api/payment/skipgate/paymentComplete"));
		return config;
	}

	@Override
	public Order handlePaymentCompleteRequest(HttpServletRequest request,
			HttpServletResponse response) {
		
		String orderId = request.getParameter("OrderID");
		if (orderId==null) paymentError(null, "Order not found.");
		
		Order order = orderService.get(Long.parseLong(orderId));
		orderService.setOrderState(order, OrderState.PaymentComplete);
		
		return order;
		
	}

	@Override
	public void handlePaymentErrorRequest(HttpServletRequest request,
			HttpServletResponse response) {
	}
	
	private void paymentError(Order order, String message) {
		throw new PaymentException("Payment error: " + message);
	}

}
