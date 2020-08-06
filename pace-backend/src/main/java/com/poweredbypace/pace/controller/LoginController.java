package com.poweredbypace.pace.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.dto.LoginInfo;
import com.poweredbypace.pace.exception.EmailNotVerifiedException;
import com.poweredbypace.pace.service.StoreService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.util.MessageUtil;

@Controller
public class LoginController  {

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityContextRepository repository;

	@Autowired
	private RememberMeServices rememberMeServices;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StoreService storeService;
	
	
	@RequestMapping(value="/api/login", method=RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> performLogin(
		@RequestBody LoginInfo loginInfo,
		HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		//check if user's email has been validated
		User user = userService.getByEmail(loginInfo.getUsername());
		if (user!=null && BooleanUtils.isFalse(user.isVerified()))
			throw new EmailNotVerifiedException("Email address has not been verified");
		
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				loginInfo.getUsername(), loginInfo.getPassword());
		Map<String,Object> status = new HashMap<String, Object>();
		boolean rememberMe = BooleanUtils.isTrue(loginInfo.getRememberMe());
		try {
			Authentication auth = authenticationManager.authenticate(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
			repository.saveContext(SecurityContextHolder.getContext(), request, response);
			if (rememberMe) {
				rememberMeServices.loginSuccess(request, response, auth);
			}
			User currentUser = userService.getCurrentUser();
			status.put("result", true);
			status.put("user", currentUser);
			status.put("store", storeService.getUserStore(currentUser));
			
		} catch (Exception ex) {
			if (rememberMe)
				rememberMeServices.loginFail(request, response);
			
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			status.put("type", ex.getClass().getName());
			status.put("error", MessageUtil.getMessage(ex));
		} 
		
		return status;
	}
	
	@RequestMapping(value="/api/isAuthenticated", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> isAuthenticated() throws Exception 
	{
		Map<String,Object> status = new HashMap<String, Object>();
		status.put("result", true);
		status.put("user", userService.getCurrentUser());
		
		return status;
	}
	
}
