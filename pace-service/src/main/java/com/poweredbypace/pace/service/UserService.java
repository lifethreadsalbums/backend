package com.poweredbypace.pace.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.poweredbypace.pace.domain.user.Group;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.dto.PasswordInfo;
import com.poweredbypace.pace.exception.EmailAlreadyExistsException;
import com.poweredbypace.pace.exception.EmailAlreadyVerifiedException;
import com.poweredbypace.pace.exception.EmailNotFoundException;
import com.poweredbypace.pace.exception.EmailNotVerifiedException;
import com.poweredbypace.pace.exception.InvalidVerificationCodeException;
import com.poweredbypace.pace.exception.LinkExpiredException;


public interface UserService extends CrudService<User> {

	List<Group> getUserGroups();
	
	User getByEmail(String email);
	
	User register(User user) throws EmailAlreadyExistsException;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	void reauth(PasswordInfo passInfo);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	User registerAdmin(User user) throws EmailAlreadyExistsException;
	
	void resendVerificationEmail(User user);
	
	void verifyEmail(String id, String email) throws EmailAlreadyVerifiedException, InvalidVerificationCodeException;
	
	void requestPasswordReset(User user) throws EmailNotFoundException, EmailNotVerifiedException;
	
	String resetPassword(String id, String email) throws InvalidVerificationCodeException, LinkExpiredException;
	
	void changePassword(User user, String newPassword);
	
	User getPrincipal();
	
	User getCurrentUser();
	
	User save(User user);
	
	User findOne(long id);
	
	User get(long id);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public void delete(long id);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<User> save(List<User> entities);
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	List<User> findAll();
		
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	void delete(User entity);

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	void delete(List<User> entities);
	
}