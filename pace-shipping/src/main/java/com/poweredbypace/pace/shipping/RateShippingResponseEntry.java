package com.poweredbypace.pace.shipping;

import com.poweredbypace.pace.domain.Money;
import com.poweredbypace.pace.domain.shipping.ShippingOption;

public class RateShippingResponseEntry {

	private ShippingOption shippingOption;
	private Money money;

	public ShippingOption getShippingOption() {
		return shippingOption;
	}

	public void setShippingOption(ShippingOption shippingOption) {
		this.shippingOption = shippingOption;
	}

	public Money getMoney() {
		return money;
	}

	public void setMoney(Money money) {
		this.money = money;
	}
}
