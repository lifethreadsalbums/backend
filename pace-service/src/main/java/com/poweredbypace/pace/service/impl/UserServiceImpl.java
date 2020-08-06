package com.poweredbypace.pace.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.Group;
import com.poweredbypace.pace.domain.user.Role;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.domain.user.User.UserStatus;
import com.poweredbypace.pace.dto.PasswordInfo;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.event.UserAccountEnabledEvent;
import com.poweredbypace.pace.event.UserLoggedInEvent;
import com.poweredbypace.pace.event.UserRegisteredEvent;
import com.poweredbypace.pace.exception.EmailAlreadyExistsException;
import com.poweredbypace.pace.exception.EmailAlreadyVerifiedException;
import com.poweredbypace.pace.exception.EmailNotFoundException;
import com.poweredbypace.pace.exception.EmailNotVerifiedException;
import com.poweredbypace.pace.exception.InvalidVerificationCodeException;
import com.poweredbypace.pace.exception.LinkExpiredException;
import com.poweredbypace.pace.mail.EmailNotification;
import com.poweredbypace.pace.mail.EmailNotification.Recipient;
import com.poweredbypace.pace.mail.EmailNotificationParam;
import com.poweredbypace.pace.mail.EmailNotificationRecipient;
import com.poweredbypace.pace.manager.CurrencyManager;
import com.poweredbypace.pace.notifications.Notification;
import com.poweredbypace.pace.notifications.Notification.NotificationType;
import com.poweredbypace.pace.notifications.NotificationBroadcaster;
import com.poweredbypace.pace.repository.GroupRepository;
import com.poweredbypace.pace.repository.RoleRepository;
import com.poweredbypace.pace.repository.UserRepository;
import com.poweredbypace.pace.service.EventService;
import com.poweredbypace.pace.service.StoreService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.service.ViewService;

import sk.nociar.jpacloner.JpaCloner;
import sk.nociar.jpacloner.PropertyFilter;


@SuppressWarnings("deprecation")
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService, ApplicationListener<AuthenticationSuccessEvent>  {
	
	private final Log log = LogFactory.getLog(getClass());
	
	private static final String PASSWORD_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789=!#%&/";
	private static final int LINK_EXPIRATION_TIME = 1000 * 60 * 60 * 24;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private GroupRepository groupRepository;
	
	@Autowired(required=false)
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private NotificationBroadcaster notificationBroadcaster;
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	@PersistenceContext(unitName="paceUnit")
	private EntityManager entityManager;
	
	
	@Autowired(required=false)
	private Env env;
	
	@Autowired
	private ViewService viewService;
	
	@Autowired
	private CurrencyManager currencyManager;
	
	private ConcurrentMap<String, Long> lastLoginEvent = new ConcurrentHashMap<String, Long>();
	
	@Autowired
	private StoreService storeService;
	
	@Override
	public void onApplicationEvent(AuthenticationSuccessEvent event) {
		
		long timestamp = new Date().getTime();
		
		String userName = ((UserDetails) event.getAuthentication().
                getPrincipal()).getUsername();
		if (lastLoginEvent.containsKey(userName)) {
			long loginTime = lastLoginEvent.get(userName);
			if (timestamp - loginTime < 1000 * 5) 
				return;
		}
		
		User user = userRepo.findByEmail(userName);
		if (user!=null) {
			user.setLastLoginDate(new Date());
			userRepo.save(user);
		} else {
			user = (User) event.getAuthentication().getPrincipal();
		}
		
		//set view and currency
		env.setView( viewService.getView(user) );
		env.setCurrency( currencyManager.getCurrency(user) );
		
		Store store = storeService.getUserStore(user);
		store.getAddress().getCountry().getName();
		store.getAddress().getState().getName();
		env.setStore(store);
		
		log.debug("User " + user.getEmail() + " logged in. Currency = " + 
			env.getCurrency().getCurrencyCode() + ", view = " + env.getView().getName() + ", store = "+store.getName());
		if (user.getId()!=null) {
			eventService.sendEvent(new UserLoggedInEvent(user, store));
		}
		lastLoginEvent.put(userName, timestamp);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		
		User user = this.getByEmail(username);
		if (user == null) {
	       throw new UsernameNotFoundException("User " + username + " not found.");
	    }
		
		PropertyFilter myFilter = new PropertyFilter() {
		    public boolean test(Object entity, String property) {
		        return !"savedLayoutTemplates".equals(property);
		    }
		};
		
		User clonedUser = JpaCloner.clone(user, myFilter, "*+");
		return clonedUser;
	}

	public User getByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
	public List<Group> getUserGroups() 
	{
		return groupRepository.findByStore(env.getStore());
	}
	
	private User prepareNewUser(User user) throws EmailAlreadyExistsException {
		//check if user exists
		User existingUser = userRepo.findByEmail(user.getEmail());
		if (existingUser!=null)
			throw new EmailAlreadyExistsException("Email address is already registered");
				
		//assign group
		Group group = groupRepository.findOne(user.getGroup().getId());
		user.setGroup(group);
		user.setCreateDate(new Date());
		
		User principal = getPrincipal();
		if (principal==null || !principal.isAdmin()) {
			user.setStatus(UserStatus.New);
		}
		
		user.getRoles().add( roleRepository.findOne(1l) );
		String hash = passwordEncoder.encodePassword(user.getPassword(), "");
		user.setPassword(hash);
			
		for(Address address:user.getAddresses())
		{
			address.setUser(user);
		}
		
		return user;
	}
	
	@Override
	public User register(User user) throws EmailAlreadyExistsException {
		User newUser = userRepo.save(prepareNewUser(user));
		notificationBroadcaster.broadcast(Notification.create(NotificationType.UserRegistered, newUser));
		Store store = storeService.getUserStore(newUser);
		eventService.sendEvent(new UserRegisteredEvent(newUser, store));
		return newUser;
	}
	
	@Override
	public User registerAdmin(User user) throws EmailAlreadyExistsException {
		user = prepareNewUser(user);
		user.getRoles().add(roleRepository.findOne(2l));
		return userRepo.save(user);
	}
	
	@Override
	@EmailNotification(template="USER_REGISTRATION", to=Recipient.MethodParam)
	public void resendVerificationEmail(
			@EmailNotificationParam("user") @EmailNotificationRecipient("email") User user) {
		//do nothing, just send the email via annottations
	}
	
	
	public void verifyEmail(String id, String email) 
			throws EmailAlreadyVerifiedException, InvalidVerificationCodeException
	{
		if (id==null || email==null)
			throw new InvalidVerificationCodeException("Invalid verification code");
		
		User user = userRepo.findByEmail(email);
		
		if (user==null)
			throw new InvalidVerificationCodeException("Invalid verification code");
		
		if (user!=null && user.isVerified())
			throw new EmailAlreadyVerifiedException("Email already verified");
		
		String hash = user.getVerificationHash();
		if (hash.equals(id))
		{
			user.setStatus(UserStatus.Verified);
			userRepo.save(user);
		} else {
			throw new InvalidVerificationCodeException("Invalid verification code");
		}
	}
	
	@EmailNotification(template="USER_PASSWORD_RESET", to=Recipient.MethodParam)
	public void requestPasswordReset(
			@EmailNotificationParam("user") 
			@EmailNotificationRecipient("email") final User user) throws EmailNotFoundException, EmailNotVerifiedException
	{
		if (user==null)
			throw new EmailNotFoundException("Email not found");
		
		if (BooleanUtils.isFalse(user.isEnabled()))
			throw new DisabledException("User is disabled");
		
		if (BooleanUtils.isFalse(user.isVerified()))
			throw new EmailNotVerifiedException("Email address has not been verified");
		
	}
	
	
	public String resetPassword(String id, String email) 
			throws InvalidVerificationCodeException, LinkExpiredException
	{
		User user = userRepo.findByEmail(email);
		
		if (user==null)
			throw new InvalidVerificationCodeException("Invalid verification code");
		
		String[] parts = id.split("x");
		if (parts.length!=2)
			throw new InvalidVerificationCodeException("Invalid verification code");
		
		long timestamp = Long.parseLong(parts[1], 16);
		long currentTime = new Date().getTime();
		
		if (timestamp > currentTime)
			throw new InvalidVerificationCodeException("Invalid verification code");
		
		if (currentTime - timestamp > LINK_EXPIRATION_TIME)
			throw new LinkExpiredException("Link has expired");
		
		String hash = user.getPasswordResetHash(timestamp);
		if (hash.equals(id))
		{
			String newPassword = RandomStringUtils.random(10, PASSWORD_CHARS);
			changePassword(user, newPassword);
			user.setChangePasswordOnNextLogin(true);
			userRepo.save(user);
			return newPassword;
		} else {
			throw new InvalidVerificationCodeException("Invalid verification code");
		}

	}
	
	public User getCurrentUser() {
		User user = getPrincipal();
		if (user==null) return null;
		
		return this.getByEmail(user.getEmail());
	}
	
	public User getPrincipal() {
		if (SecurityContextHolder.getContext().getAuthentication()!=null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth.getPrincipal() instanceof User)
				return (User)auth.getPrincipal();
		}
		return null;
	}
	
	public void changePassword(User user, String newPassword) {
		String hash = passwordEncoder.encodePassword(newPassword, "");
		user.setPassword(hash);
		user.setChangePasswordOnNextLogin(false);
		userRepo.save(user);
	}

	@Override
	public User get(long id) {
		return userRepo.findOne(id);
	}
	
	@Override
	public void reauth(PasswordInfo passInfo) {
		User user = getCurrentUser();
		String hash = passwordEncoder.encodePassword(passInfo.getPassword(), "");
		if (!StringUtils.equals(user.getPassword(), hash)) {
			
			throw new RuntimeException("Invalid password");
		}
	}

	@Override
	public User save(User user) {
		
		if (user.getId()==null) 
			throw new IllegalStateException("User doesn't exist");
			
		User principal = getPrincipal();
		User oldUser = userRepo.findOne(user.getId());
		
		//set new password or copy the old one
		if (user.getNewPassword()!=null) {
			String hash = passwordEncoder.encodePassword(user.getNewPassword(), "");
			user.setPassword(hash);
		} else {
			user.setPassword(oldUser.getPassword());
		}
		//do not touch these fields
		user.setCreateDate(oldUser.getCreateDate());
		
		boolean accountEnabled = false;
		if (principal.hasRole(Role.ROLE_ADMIN)) {
			
			if (BooleanUtils.isFalse(oldUser.isEnabled()) && BooleanUtils.isTrue(user.isEnabled())) {
				//enable user
				user.setApprovalDate(new Date());
				accountEnabled = true;
			}
			
		} else if (principal.hasRole(Role.ROLE_USER)) {
			if (!ObjectUtils.equals(user.getId(), oldUser.getId()))
				throw new AuthorizationServiceException("Not allowed");
			
			//do not touch these fields
			user.setEmail(oldUser.getEmail());
			user.setRoles(oldUser.getRoles());
			user.setStatus(oldUser.getStatus());
		}
		
		user = userRepo.save(user);
		
		if (accountEnabled) {
			//send event
			Store store = storeService.getUserStore(user);
			UserAccountEnabledEvent e = new UserAccountEnabledEvent(user, store);
			eventService.sendEvent(e);
		}
		
		return user;
	}
	

	@Override
	@Transactional
	public List<User> save(List<User> entities) {
		for(User user: entities) {
			save(user);
		}
		return entities;
	}

	@Override
	public User findOne(long id) {
		return userRepo.findOne(id);
	}

	@Override
	public List<User> findAll() {
		return userRepo.findAll();
	}
	
	@Override
	@Transactional
	public void delete(long id) {
		User user = userRepo.findOne(id);
		delete(user);
	}

	@Override
	@Transactional
	public void delete(User user) {
		user.setIsDeleted(true);
		userRepo.save(user);
	}

	@Override
	@Transactional
	public void delete(List<User> users) {
		for(User u:users) {
			delete(u);
		}
	}

	@Override
	public List<User> findAll(List<Long> ids) {
		return userRepo.findAll(ids);
	}

}
