package com.poweredbypace.pace.service.impl;

import java.util.Currency;

import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.domain.View;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.Role;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.repository.StoreRepository;
import com.poweredbypace.pace.repository.ViewRepository;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.service.ViewService;


@Service
public class ViewServiceImpl implements ViewService {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ViewRepository viewRepository;
	
	@Autowired
	private StoreRepository storeRepository;

	public View getView(User user) {
		if (user!=null && user.hasRole(Role.ROLE_ADMIN))
			return viewRepository.findOne(3l);
		else if (user!=null && user.hasRole(Role.ROLE_USER))
			return viewRepository.findOne(2l);
		
		return viewRepository.findOne(1l);//default anonymous user view
	}
	
	public View getDefault() {
		User user = userService.getPrincipal();
		return getView(user);
	}
	
	@Transactional(value=TxType.REQUIRED)
	public Env getEnv() {
		Env env = new Env();
		View view = viewRepository.findOne(3l);
		Store store = storeRepository.findByIsDefaultTrue();
		store.getAddress().getCountry().getIsoCountryCode();
		store.getAddress().getState().getName();
		env.setStore(store);
		env.setCurrency( Currency.getInstance(store.getBaseCurrency()) );
		env.setView(view);
		return env;
	}
}
