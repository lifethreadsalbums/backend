package com.poweredbypace.pace.repository;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poweredbypace.pace.domain.Batch;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.order.Order.OrderState;
import com.poweredbypace.pace.domain.user.User;


public interface ProductRepository extends JpaRepository<Product, Long>
{
	@Query("select p from Product p where p.user = ?1 and p.isReprint<>1 and p.parent is null and p.state in ?2")
	List<Product> findByUserAndStateAndParentIsNull(User user, ProductState state);
	
	@Query("select p from Product p where p.user = ?1 and p.isReprint<>1 and p.parent is null and p.state in ?2")
	List<Product> findByUserAndStateAndParentIsNull(User user, ProductState state, Pageable pageRequest);
	
	List<Product> findByStateAndParentIsNull(ProductState state);
	List<Product> findByStateAndParentIsNull(ProductState state, Pageable pageRequest);
	
	List<Product> findByUserAndParentIsNull(User user);
	List<Product> findByUserAndIsFavouriteTrueAndParentIsNull(User user);
	List<Product> findByBatchAndParentIsNull(Batch batch);
	List<Product> findByLayout(Layout layout);
	
	List<Product> findByLayoutAndParentIsNull(Layout layout);
	
	int countByOriginalAndIsReprintTrue(Product p);
	
	@Query(value="select p from Product p where p.state in ?1")
	List<Product> findByProductStates(ProductState[] states, Pageable pageRequest);
	
	@Query(value="select p from Product p where p.parent is null and p.state in ?1")
	List<Product> findByProductStatesAndParentIsNull(ProductState[] states);
	
	@Query(value="select p from Product p where p.isReprint<>1 and p.user = ?1 and p.parent is null and p.state in ?2")
	List<Product> findByUserAndStates(User user, ProductState[] states, Pageable pageRequest);
	
	@Query("select count(p) from Product p where p.user = ?1 and p.isReprint<>1 and p.parent is null and p.state in ?2")
	int countByUserAndStates(User user, ProductState[] states);
	
	@Query(value="select p from Product p where p.user = ?1 and p.parent is null and p.orderItem.order.state = ?2")
	List<Product> findByUserAndOrderState(User user, OrderState state, Pageable pageRequest);
	
	@Query(value="select count(p) from Product p where p.user = ?1 and p.parent is null and p.orderItem.order.state = ?2")
	int countByUserAndOrderState(User user, OrderState state);
	
	@Query("select count(p) from Product p where p.user = ?1 and p.isReprint<>1 and p.state = ?2 and p.parent is null")
	int countByUserAndState(User user, ProductState state);
	
	@Query("select count(p) from Product p where p.state = ?1 and p.parent is null")
	int countByState(ProductState state);
	
	@Query("select count(p) from Product p where p.state in ?1")
	int countByStates(ProductState[] states);
	
	//query to determine if the image file has been used first time as a die
	@Query(value="select P.ID from P_ELEMENT E inner join P_PRODUCT_OPTION_ELEMENT PE on PE.ELEMENT_ID=E.ID "
			+ "inner join P_PRODUCT_OPTION O on O.ID = PE.ID "
			+ "inner join P_PRODUCT P on P.ID = O.PRODUCT_ID "
			+ "left join O_ORDER_ITEM OI on OI.PRODUCT_ID = P.ID "
			+ "where E.IMAGE_FILE_ID = ?1 order by coalesce(OI.ID,9999999999999999999999999999999) asc, E.ID", nativeQuery = true)
	List<BigInteger> findByImageFileId(long id);
	
	@Query(value="SELECT DISTINCT p FROM Product p INNER JOIN p.productOptions po "
			+ "WHERE ("
			+ "(CONCAT(p.user.firstName, ' ', p.user.lastName) LIKE ?1%) "
			+ "OR (p.productNumber LIKE ?1%) "
			+ "OR (po.stringValue LIKE ?1%) )")
	List<Product> findByQuery(String query, Pageable pageRequest);
	
	@Query(value="SELECT DISTINCT p FROM Product p INNER JOIN p.productOptions po "
			+ "WHERE p.user=?2 AND "
			+ "( (p.productNumber LIKE %?1%) OR (po.stringValue LIKE %?1%) )")
	List<Product> findByQuery(String query, User user, Pageable pageRequest);
	
	@Query(value="SELECT DISTINCT p FROM Product p INNER JOIN p.productOptions po "
			+ "WHERE (p.state in (?2)) AND ("
			+ "  (p.user.firstName LIKE ?1%) OR "
			+ "  (p.user.lastName LIKE ?1%) OR "
			+ "  (p.user.companyName LIKE ?1%) OR "
			+ "  (p.productNumber LIKE ?1%) OR "
			+ "  (po.stringValue LIKE ?1%)"
			+ ")")
	List<Product> findByQueryAndProductStates(String query, ProductState[] states, Pageable pageRequest);
	
	@Query(value="SELECT DISTINCT p FROM Product p INNER JOIN p.productOptions po "
			+ "WHERE p.user=?2 AND p.isReprint<>1 AND p.state in (?3) AND ("
			+ "  (p.user.firstName LIKE ?1%) OR "
			+ "  (p.user.lastName LIKE ?1%) OR "
			+ "  (p.user.companyName LIKE ?1%) OR "
			+ "  (p.productNumber LIKE ?1%) OR "
			+ "  (po.stringValue LIKE ?1%) "
			+ ")")
	List<Product> findByQueryAndUserAndProductStates(String query, User user, ProductState[] states, Pageable pageRequest);

    @Query(value = "SELECT DISTINCT p FROM Product p INNER JOIN p.productOptions po "
            + "inner join p.orderItem oi inner join oi.order ord "
            + "WHERE p.state in ?3 AND ord.dateCreated >= ?1 AND ord.dateCreated  <= ?2")
    List<Product> findProductsByFromAndToDate(Date fromDate, Date toDate, ProductState[] states, Pageable pageRequest);
}
