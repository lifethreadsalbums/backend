package com.poweredbypace.pace.job.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.poweredbypace.pace.event.ApplicationEvent;
import com.poweredbypace.pace.service.EventService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ApplicationEventTask extends AbstractTask {
	
	@Autowired
	private EventService eventService;
	
	@Override
	public int getTimeout() {
		return 60 * 15; //15 minutes
	}

	@Override
	public void run() {
		
		final ApplicationEvent event = (ApplicationEvent)job.getParams();
		eventService.processEvent(event);
		
	}

}
