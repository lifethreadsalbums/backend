package com.poweredbypace.pace.service;

import com.poweredbypace.pace.domain.JobProgressInfo;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.order.Order;

public interface BinderyFormService {
	void generate(Product p, JobProgressInfo job) throws InterruptedException;
	void generate(Order order, JobProgressInfo job) throws InterruptedException; 
}
