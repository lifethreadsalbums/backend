package com.poweredbypace.pace.currency;

import java.rmi.RemoteException;
import java.util.List;

import com.poweredbypace.pace.domain.CurrencyRate;

public interface CurrencyRateProvider {

	public abstract List<CurrencyRate> getRates(String baseCurrency)
			throws RemoteException;

}