package com.poweredbypace.pace.payment;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poweredbypace.pace.domain.order.Order;

public interface PaymentGateway {
	
	public PaymentGatewayConfiguration getConfiguration();
	public Order handlePaymentCompleteRequest(HttpServletRequest request, HttpServletResponse response);
	public void handlePaymentErrorRequest(HttpServletRequest request, HttpServletResponse response);

}
