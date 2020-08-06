package com.poweredbypace.pace.service.impl;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poweredbypace.pace.currency.CurrencyRateProvider;
import com.poweredbypace.pace.domain.CurrencyInfo;
import com.poweredbypace.pace.domain.CurrencyRate;
import com.poweredbypace.pace.domain.GenericRule;
import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.store.Store;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.expression.impl.UserContext;
import com.poweredbypace.pace.manager.CurrencyManager;
import com.poweredbypace.pace.repository.CurrencyRateRepository;
import com.poweredbypace.pace.service.GenericRuleService;
import com.poweredbypace.pace.service.UserService;
import com.poweredbypace.pace.util.SpringContextUtil;

@Service
public class CurrencyServiceImpl implements CurrencyManager {
	
	private Log log = LogFactory.getLog(getClass());

	private List<CurrencyRate> rates;
	
	@Autowired
	private CurrencyRateRepository currencyRateRepo;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	//@Qualifier("webservicex-currency-rate-provider")
	private CurrencyRateProvider currencyRateProvider;
	
	@Autowired
	private GenericRuleService ruleService;
	
	public Money convertTo(Money money, Currency currency) {
		
		BigDecimal amount = money.getAmount();
		CurrencyRate rate = getRate(money.getCurrency(), currency.getCurrencyCode());
		if (rate==null)
			throw new IllegalStateException("Cannot find conversion rate from " + money.getCurrency() + " to " + currency);
		amount = amount.multiply(rate.getRate());
		
		return new Money(amount, currency.getCurrencyCode());
	}
	
	private CurrencyRate getRate(String from, String to) {
		if (rates==null)
			rates = currencyRateRepo.findAll();
		
		for(CurrencyRate rate:rates) {
			if (rate.getCurrencyFrom().equalsIgnoreCase(from) &&
				rate.getCurrencyTo().equalsIgnoreCase(to))
			return rate;
		}
		return null;
	}
	
	public void updateRates() {
		try {
			Currency baseCurrency = getCurrency(SpringContextUtil.getEnv().getStore());
			List<CurrencyRate> newRates = currencyRateProvider.getRates(baseCurrency.getCurrencyCode());
			for(CurrencyRate rate : newRates) {
				CurrencyRate oldRate = currencyRateRepo.findByCurrencyFromAndCurrencyTo(rate.getCurrencyFrom(), rate.getCurrencyTo());
				if(oldRate != null) {
					rate.setFactor(oldRate.getFactor());
					currencyRateRepo.delete(oldRate);
				}
				currencyRateRepo.save(rate);
			}
		} catch (RemoteException e) {
			log.error("", e);
		}
	}
	
	@Override
	public Currency getCurrency(Store store) {
		return Currency.getInstance(store.getBaseCurrency());
	}
	
	@Override
	public List<CurrencyInfo> getAvailableCurrencies() {
		User user = userService.getCurrentUser();
		List<CurrencyInfo> result = new ArrayList<CurrencyInfo>();
		if (user!=null) {
			result.add( new CurrencyInfo(getCurrency(user)) );
		} else {
			Store store = SpringContextUtil.getEnv().getStore();
			result.add( new CurrencyInfo(getCurrency(store)) );
		}
		return result;
	}
	
	@Override
	public Currency getCurrency(User user) {
		
		if (user.getCurrency()!=null) {
			return Currency.getInstance(user.getCurrency());
		}
		
		GenericRule rule = ruleService.findRule(new UserContext(user), "CURRENCY");
		if (rule!=null) {
			return Currency.getInstance(rule.getJsonData());
		}
		//obtain default currency by store address
		return getCurrency( SpringContextUtil.getEnv().getStore() );
		
	}
}
