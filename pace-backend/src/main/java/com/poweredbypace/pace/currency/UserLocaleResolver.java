package com.poweredbypace.pace.currency;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.LocaleResolver;

import com.poweredbypace.pace.domain.user.User;

public class UserLocaleResolver implements LocaleResolver {

	private Locale defaultLocale;
	private String defaultLanguage;
	
	
	public Locale getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(Locale defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

	public String getDefaultLanguage() {
		return defaultLanguage;
	}

	public void setDefaultLanguage(String defaultLanguage) {
		this.defaultLanguage = defaultLanguage;
	}

	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth!=null && auth.getPrincipal() instanceof User) {
			User user = (User)auth.getPrincipal();
			if (user.getBillingAddress()!=null && user.getBillingAddress().getCountry()!=null) {
				String countryCode = user.getBillingAddress().getCountry().getIsoCountryCode();
				return new Locale(defaultLanguage, countryCode);
			}
		}
		return defaultLocale;
	}

	@Override
	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		
		throw new UnsupportedOperationException();

	}

}
