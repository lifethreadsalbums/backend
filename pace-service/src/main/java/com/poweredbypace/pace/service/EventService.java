package com.poweredbypace.pace.service;

import com.poweredbypace.pace.event.ApplicationEvent;

public interface EventService {
	void processEvent(ApplicationEvent e);
	void sendEvent(ApplicationEvent e);
}
