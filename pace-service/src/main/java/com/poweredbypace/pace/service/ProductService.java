package com.poweredbypace.pace.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;

import com.poweredbypace.pace.domain.Attachment;
import com.poweredbypace.pace.domain.Attachment.AttachmentType;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.Product.ProductState;
import com.poweredbypace.pace.domain.PrototypeProduct;
import com.poweredbypace.pace.domain.layout.Layout;
import com.poweredbypace.pace.domain.user.User;


public interface ProductService extends CrudService<Product> {

	@PreAuthorize("hasRole('ROLE_ADMIN') or #user.id == authentication.id")
	List<Product> getByUser(User user);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PostFilter("hasRole('ROLE_ADMIN') or filterObject.user.id == principal.id")
	List<Product> getByState(ProductState state);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	List<Product> getByUserAndState(User user, ProductState state);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<Product> getByBatchId(long batchId);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Product createReprint(Product p);
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #user.id == principal.id")
	List<Product> getFavourite(User user);
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #user.id == principal.id")
	List<Product> getByUserAndName(User user, String name);
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #product.user.id == principal.id")
	boolean checkUniqueName(Product product);
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #product.user.id == principal.id")
	Product save(Product product);
	
	@PostAuthorize("hasRole('ROLE_ADMIN') or returnObject.user.id == principal.id")
	Product findOne(long id);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<Product> changeState(List<Product> products, ProductState state);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PreFilter("hasRole('ROLE_ADMIN') or filterObject.user.id == principal.id")
	List<Product> save(List<Product> entities);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PostFilter("hasRole('ROLE_ADMIN') or filterObject.user.id == principal.id")
	List<Product> findAll();
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #product.user.id == principal.id")
	void delete(Product entity);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@PreFilter("hasRole('ROLE_ADMIN') or filterObject.user.id == principal.id")
	void delete(List<Product> entities);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	Product createProductFromPrototype(PrototypeProduct prototypeProduct);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	Product createProductFromPrototype(long prototypeId);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	void updatePageCount(Product p);
	
	void generateProductThumb(Layout l);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Product copy(Product product);
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or #product.user.id == principal.id")
	Product reorder(Product product);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	Attachment saveAttachment(Product product, String url, AttachmentType attachmentType, User user);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	Attachment getAttachment(Product product, AttachmentType attachmentType);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	long getProductChecksum(Product p);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	List<Product> findByQueryAndProductStates(String query, ProductState[] states, Pageable pageRequest);
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	List<Product> findByProductStates(ProductState[] states, Pageable pageRequest);
}