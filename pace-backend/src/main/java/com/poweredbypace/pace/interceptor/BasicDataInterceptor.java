package com.poweredbypace.pace.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.manager.CurrencyManager;
import com.poweredbypace.pace.service.StoreService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.service.ViewService;

public class BasicDataInterceptor extends HandlerInterceptorAdapter {

	private final Log log = LogFactory.getLog(BasicDataInterceptor.class);
	
	@Autowired
	private ViewService viewService;
	
	@Autowired
	private Env env;
	
	@Autowired
	private CurrencyManager currencyManager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private StoreService storeService;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if(env.getStore() == null) {
			Store store = storeService.getCurrentStore(request.getServerName());
				
			store.getAddress().getCountry().getName();
			store.getAddress().getState().getName();
			env.setStore(store);
			
			log.debug("Current store: "+ store.getName());
		}
		
		if (env.getView() == null) {
			env.setView( viewService.getDefault() );
			log.debug("Setting view " + env.getView().getName());
		}
		
		if (env.getCurrency() == null) {
			User user = userService.getPrincipal();
			if (user!=null) {
				env.setCurrency(currencyManager.getCurrency(user));
				log.debug("Currency for " + user.getEmail() + " is " + env.getCurrency().getCurrencyCode());
			}
		}
		
		return super.preHandle(request, response, handler);
	}
	
	
}
