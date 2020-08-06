package com.poweredbypace.pace.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.order.OrderItem;
import com.poweredbypace.pace.domain.user.User;


public interface OrderService {
	
	Order save(Order order);
	Order get(Long id);
	Order getByOrderNumber(String orderNumber);
	Order getCart(User user);
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #product.user.id == principal.id")
	Order addToCart(Product product);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PreFilter("hasRole('ROLE_ADMIN') or filterObject.user.id == principal.id")
	Order addToCart(List<Product> products);
	
	Order createOrder(User user);
	List<Order> getByUserAndState(User user, OrderState state);
	void deleteOrderItem(long id);
	void deleteOrderItem(OrderItem oi);
	void setOrderState(Order order, OrderState state);
	void prepareForPayment(Order order);
	void delete(Order order);
	void delete(List<Order> orders);
	void assignOrderNumber(Order o);
	Order markAsPaid(Product p);

}