package com.poweredbypace.pace.domain;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonView;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.json.View;
import com.poweredbypace.pace.manager.CurrencyManager;
import com.poweredbypace.pace.util.SpringContextUtil;

@Embeddable
public class Money {

	private BigDecimal amount;
	private String currency;
	
	private static String getDefaultCurrency() {
		Env env = SpringContextUtil.getEnv();
		if (env!=null) {
			return env.getCurrency().getCurrencyCode();
		}
		return "CAD";
	}
	
	public Money() {
		this.amount = new BigDecimal("0.0").setScale(2, BigDecimal.ROUND_HALF_EVEN);
		//this.currency = getDefaultCurrency();
	}
	
	public Money(BigDecimal amount, String currency) {
		if (amount==null) {
			this.amount = new BigDecimal("0.0").setScale(2, BigDecimal.ROUND_HALF_EVEN);
		} else {
			this.amount = amount.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		}
		this.currency = currency;
	}
	
	public Money(Float amount, String currency) {
		this.amount = new BigDecimal(amount!=null ? amount : 0f).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		this.currency = currency;
	}
	
	public Money(BigDecimal amount) {
		this(amount, getDefaultCurrency());
	}
	
	public Money(Float amount) {
		this(amount!=null ? amount : 0f, getDefaultCurrency());
	}
	
	@Column(name="AMOUNT")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	@Column(name="CURRENCY")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Override
    public String toString() {
		return format(this, Currency.getInstance(this.getCurrency()));
    }
	
	@Transient
	@JsonView(View.OrderShortInfo.class)
	public String getDisplayPrice() {
		return format(this, Currency.getInstance(this.getCurrency()));
	}
	
	@Transient
	@JsonView(View.OrderShortInfo.class)
	public String getDisplayCurrency() {
		return this.currency;
	}
	
	@Transient
	public String getConvertedDisplayPrice() {
		Env env = SpringContextUtil.getEnv();
		if (env!=null && !this.getCurrency().equals(env.getCurrency().getCurrencyCode()))
		{
			CurrencyManager converter = SpringContextUtil.getCurrencyManager();
			Money convertedValue = converter.convertTo(this, env.getCurrency());
			return format(convertedValue, env.getCurrency());
		} else 
			return getDisplayPrice();
	}
	
	@Transient
	public String getConvertedDisplayCurrency() {
		Env env = SpringContextUtil.getEnv();
		if (env!=null) {
			Currency currency = env.getCurrency();
			return currency.getCurrencyCode();
		} else
			return this.getDisplayCurrency();
	}
	
	private String format(Money money, Currency currency) {
		Locale locale = LocaleContextHolder.getLocale();
		DecimalFormat format = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		format.setCurrency(currency);
		format.setParseBigDecimal(true);
		format.setMaximumFractionDigits(2);
		format.setMinimumFractionDigits(2);
		String curSym = format.getDecimalFormatSymbols().getCurrencySymbol();
		
		if ("CAD".equals(curSym) || "US$".equals(curSym) || "USD".equals(curSym)) {
			DecimalFormatSymbols dfs = format.getDecimalFormatSymbols();
			dfs.setCurrencySymbol("$");
			format.setDecimalFormatSymbols(dfs);
		}
		
		curSym = format.getDecimalFormatSymbols().getCurrencySymbol();
		return format.format(money.getAmount());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Money other = (Money) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
			return false;
		return true;
	}
	
}
