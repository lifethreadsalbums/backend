package com.poweredbypace.pace.env;

import java.io.Serializable;
import java.util.Currency;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.domain.View;
import com.poweredbypace.pace.domain.store.Store;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Env implements Serializable {
	
	private static final long serialVersionUID = -7239095938859150767L;
	
	private Store store;
	private Currency currency;
	private View view;
	
	public Store getStore() { return store; }
	public void setStore(Store store) { this.store = store; }

	public Currency getCurrency() { return currency; }
	public void setCurrency(Currency currency) { this.currency = currency; }

	public View getView() { return view; }
	public void setView(View view) { this.view = view; }
	
}
