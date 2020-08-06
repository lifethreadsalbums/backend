package com.poweredbypace.pace.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.order.Order;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.user.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	List<Order> findByUser(User user);
	List<Order> findByUserAndState(User user, OrderState state);
	
	List<Order> findByState(OrderState state);
	List<Order> findByState(OrderState state, Pageable pageRequest);
	
	Order findByOrderNumber(String orderNumber);
	
	long countByCouponCodeAndIdNot(String couponCode, Long id);
	long countByCouponCodeAndUserAndIdNot(String couponCode, User user, Long id);
	
	List<Order> findByCouponCodeAndIdNot(String couponCode, Long id);
	List<Order> findByCouponCodeAndUserAndIdNot(String couponCode, User user, Long id);
	
	@Query(value="SELECT DISTINCT o FROM Order o INNER JOIN o.orderItems oi "
		+ "LEFT JOIN oi.product.children child "
		+ "WHERE oi.product.state in (?1) OR child.state in (?1)")
	List<Order> findByProductStates(ProductState[] states, Pageable pageRequest);
	
	@Query(value="SELECT DISTINCT o FROM Order o INNER JOIN o.orderItems oi "
			+ "LEFT JOIN oi.product.children child "
			+ "WHERE oi.product.state in (?1) OR child.state in (?1)")
	List<Order> findByProductStates(ProductState[] states);
	
	@Query(value="SELECT DISTINCT o FROM Order o INNER JOIN o.orderItems oi "
		+ "WHERE oi.product.batch.id=?1")
	List<Order> findByBatch(long batchId, Pageable pageRequest);
	
	@Query(value="SELECT DISTINCT o FROM Order o INNER JOIN o.orderItems oi INNER JOIN oi.product.productOptions po "
		+ "WHERE ( (CONCAT(o.user.firstName, ' ', o.user.lastName) LIKE ?1%) OR (oi.product.productNumber LIKE ?1%) "
		+ "OR (po.stringValue LIKE ?1%)) ")
	List<Order> findByQuery(String query, Pageable pageRequest);
	
	@Query(value="SELECT DISTINCT o FROM Order o INNER JOIN o.orderItems oi INNER JOIN oi.product.productOptions po "
		+ "LEFT JOIN oi.product.children child "
		+ "WHERE (oi.product.state in (?2) OR child.state in (?2)) AND ( "
		+ "(o.user.firstName LIKE CONCAT(?1,'%')) OR (o.user.lastName LIKE CONCAT(?1,'%')) OR "
		+ "(oi.product.productNumber LIKE CONCAT(?1,'%')) OR (po.stringValue LIKE CONCAT(?1,'%')) )")
	List<Order> findByQueryAndProductStates(String query, ProductState[] states, Pageable pageRequest);
	
	@Query(value="SELECT DISTINCT o FROM Order o INNER JOIN o.orderItems oi INNER JOIN oi.product.productOptions po "
		+ "WHERE (o.state in (?2)) AND ( "
		+ "(o.user.firstName LIKE CONCAT(?1,'%')) OR (o.user.lastName LIKE CONCAT(?1,'%')) OR "
		+ "(oi.product.productNumber LIKE CONCAT(?1,'%')) OR (po.stringValue LIKE CONCAT(?1,'%')) )")
	List<Order> findByQueryAndOrderStates(String query, OrderState[] states, Pageable pageRequest);

	@Query(value="SELECT DISTINCT o FROM Order o INNER JOIN o.orderItems oi INNER JOIN oi.product.productOptions po "
		+ "WHERE o.state in (?1) AND o.total.amount>=?2 AND o.total.amount<=?3")
	List<Order> findByOrderStatesAndTotal(OrderState[] states, BigDecimal totalFrom, BigDecimal totalTo, Pageable pageRequest);

	@Query(value="SELECT DISTINCT o FROM Order o INNER JOIN o.orderItems oi INNER JOIN oi.product.productOptions po "
			+ "WHERE (o.state in (?3)) AND ( "
			+  "o.dateCreated >= ?1 and o.dateCreated <= ?2)")
	List<Order> findByOrderDatesAndStates(Date from, Date to, OrderState[] states, Pageable pageRequest);
}
