package com.poweredbypace.pace.manager;

import java.util.Currency;
import java.util.List;

import com.poweredbypace.pace.domain.CurrencyInfo;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;

public interface CurrencyManager {
	
	Money convertTo(Money money, Currency currency);
	List<CurrencyInfo> getAvailableCurrencies();
	Currency getCurrency(User user);
	Currency getCurrency(Store store);
	
}
