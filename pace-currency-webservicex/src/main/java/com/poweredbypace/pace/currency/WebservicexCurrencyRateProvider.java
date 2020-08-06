package com.poweredbypace.pace.currency;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub;
import com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRate;
import com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.ConversionRateResponse;
import com.poweredbypace.pace.currency.webservicex.CurrencyConvertorStub.Currency;
import com.poweredbypace.pace.domain.CurrencyRate;

public class WebservicexCurrencyRateProvider implements CurrencyRateProvider {

	@SuppressWarnings("unused")
	private Log log = LogFactory.getLog(WebservicexCurrencyRateProvider.class);

	private List<String> currencyCodes;

	@Override
	public List<CurrencyRate> getRates(String baseCurrency) throws RemoteException {
		CurrencyConvertorStub currencyConvertorStub = new CurrencyConvertorStub();
		ConversionRate conversionRate = new ConversionRate();
		Currency fromCurrency = Currency.Factory.fromValue(baseCurrency);
		List<CurrencyRate> results = new ArrayList<CurrencyRate>();
		for(String currencyCode : currencyCodes) {
			Currency toCurrency = Currency.Factory.fromValue(currencyCode);
			conversionRate.setFromCurrency(fromCurrency);
			conversionRate.setToCurrency(toCurrency);
			ConversionRateResponse response = currencyConvertorStub.conversionRate(conversionRate);
			results.add(new CurrencyRate(fromCurrency.getValue(), toCurrency.getValue(), BigDecimal.valueOf(response.getConversionRateResult())));
		}
		return results;
	}

	public List<String> getCurrencyCodes() {
		return currencyCodes;
	}

	public void setCurrencyCodes(List<String> currencyCodes) {
		this.currencyCodes = currencyCodes;
	}
	
}
