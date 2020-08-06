package com.poweredbypace.pace.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.poweredbypace.pace.mailchimp.MailChimpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.Group;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.dto.PasswordInfo;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.exception.EmailAlreadyExistsException;
import com.poweredbypace.pace.exception.EmailAlreadyVerifiedException;
import com.poweredbypace.pace.exception.EmailNotFoundException;
import com.poweredbypace.pace.exception.EmailNotVerifiedException;
import com.poweredbypace.pace.exception.InvalidVerificationCodeException;
import com.poweredbypace.pace.exception.LinkExpiredException;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.util.SpringContextUtil;

@Controller
public class UserController  {

	@Autowired
	@Qualifier("authenticationManager")
	private AuthenticationManager authenticationManager;

	@Autowired
	private SecurityContextRepository repository;

	@Autowired
	private UserService userService;
	
	
	@Autowired
	private Env env;

	@Autowired
	private MailChimpService mailChimpService;
	
	@RequestMapping(value="/api/userGroup", method=RequestMethod.GET)
	@ResponseBody
	public List<Group> getUserGroups() throws Exception 
	{
		return userService.getUserGroups();
	}
	
	@RequestMapping(value="/api/isEmailRegistered", method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> isEmailRegistered(
			@RequestParam("email") String email, 
			@RequestParam(name="userId", required=false) Long userId) throws Exception 
	{
		User user = userService.getByEmail(email);
		Map<String,Object> status = new HashMap<String, Object>();
		status.put("result", user!=null && (userId==null || userId.longValue()!=user.getId().longValue()));
		
		return status;
	}
	
	@RequestMapping(value="/api/user/register", method=RequestMethod.POST)
	@ResponseBody
	public User register(@RequestBody User user) throws EmailAlreadyExistsException  
	{
		mailChimpService.signUp(user);
		return userService.register(user);
	}
	
	@RequestMapping(value="/api/user/registeradmin", method=RequestMethod.POST)
	@ResponseBody
	public User registerAdmin(@RequestBody User user) throws EmailAlreadyExistsException
	{
		return userService.registerAdmin(user);
	}
	
	@RequestMapping(value="/api/user/resendVerificationEmail", method=RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void resendVerificationEmail(@RequestParam("email") String email) throws EmailNotFoundException  
	{
		User user = userService.getByEmail(email);
		if (user==null)
			throw new EmailNotFoundException("Email not found");
		
		userService.resendVerificationEmail(user);
	}
	
	@RequestMapping(value = "/verifyEmail", method = RequestMethod.GET)
	public RedirectView handleVerify(
			@RequestParam("id") String id, 
			@RequestParam("email") String email) throws EmailAlreadyVerifiedException, InvalidVerificationCodeException
	{
		userService.verifyEmail(id, email);
		
	    Map<String, Object> model = new HashMap<String, Object>();
		model.put("store", SpringContextUtil.getEnv().getStore() );
	    model.put("email", email);
	    
	    return new RedirectView(getAbsUrl("/#/email-verified"));	
    }
	
	@RequestMapping(value = "/api/user/password/reset", method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void passwordResetRequest(
			@RequestParam("email") String email) throws EmailNotFoundException, EmailNotVerifiedException
	{
		userService.requestPasswordReset( 
				userService.getByEmail(email) );
    }
	
	@RequestMapping(value = "/api/user/password", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void changePassword(@RequestBody PasswordInfo pass) throws EmailNotFoundException
	{
		User user = userService.getCurrentUser();
		userService.changePassword(user, pass.getPassword());
    }
	
	@RequestMapping(value = "/api/user/reauth", method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void reauth(@RequestBody PasswordInfo passwordInfo) throws EmailNotFoundException
	{
		userService.reauth(passwordInfo);
    }
	
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public ModelAndView resetPassword(
			@RequestParam("id") String id, 
			@RequestParam("email") String email,
			HttpServletRequest request,
	        HttpServletResponse response) throws EmailNotFoundException, InvalidVerificationCodeException, LinkExpiredException
	{
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("store", SpringContextUtil.getEnv().getStore());
	    model.put("email", email);
		
		if (id==null || email==null)
			return new ModelAndView("error", model);
		
		String newPassword = userService.resetPassword(id, email);
		
		Authentication req = new UsernamePasswordAuthenticationToken(email, newPassword);
		Authentication result = authenticationManager.authenticate(req);
    	SecurityContextHolder.getContext().setAuthentication(result);
    	repository.saveContext(SecurityContextHolder.getContext(), request, response);
    		
		return new ModelAndView("redirect:" + getAbsUrl("#/change-password"));
    }
	
	@RequestMapping(value = "/api/user/remove", method = RequestMethod.DELETE)
	@ResponseBody
	@ResponseStatus(value = HttpStatus.OK)
	public void removeUsers(@RequestParam List<Long> ids) {
		for(Long id: ids) {
			userService.delete(id);
		}
	}
	
//	@RequestMapping(value = "/api/user/patch", method = RequestMethod.PATCH)
//	@ResponseBody
//	@ResponseStatus(value = HttpStatus.OK)
//	public void applyPatches(@RequestParam List<Long> userIds, @RequestParam List<Patch> patches)
//			throws NumberFormatException, JsonParseException, JsonMappingException, IllegalAccessException,
//				IllegalArgumentException, InvocationTargetException, NoSuchFieldException, IOException {
//		for(Long id: userIds) {
//			userService.save(
//				partialUpdate.getUpdated(userService.get(id), patches));
//		}
//	}
	
	private String getAbsUrl(String url) {
		Store store = env.getStore();
		return "https://" + store.getDomainName() + url;
	}
	
}
