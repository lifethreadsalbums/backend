package com.poweredbypace.pace.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.poweredbypace.pace.service.ProoferService;

public class CronService {
	
	@Autowired
	ShippingManager shippingManager;
	
	@Autowired
	ProoferService prooferService;
	
	@Scheduled(cron = "0 0/15 * * * ?")
	@Transactional
	public void trackProducts() {
		shippingManager.trackProducts();
	}
	
	@Scheduled(cron = "0 0/10 * * * ?")
	@Transactional
	public void trackComments() {
		prooferService.trackComments();
	}
	
	@Scheduled(cron = "0 0 0/6 * * ?")
	@Transactional
	public void trackReplies() {
		prooferService.trackReplies();
	}
}
