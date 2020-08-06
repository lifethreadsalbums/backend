package com.poweredbypace.pace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poweredbypace.pace.domain.CurrencyRate;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate,Long>{

	CurrencyRate findByCurrencyFromAndCurrencyTo(String currencyFrom, String currencyTo);	
}
