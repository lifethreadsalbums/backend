package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.poweredbypace.pace.event.ApplicationEvent;
import com.poweredbypace.pace.event.EventHandler;

@Entity
@Table(name = "APP_EVENT_HOOK")
public class EventHook extends BaseEntity {
	
	private static final long serialVersionUID = 4752060738814635394L;
	
	private Class<? extends ApplicationEvent> eventClass;
	private Class<? extends EventHandler> handlerClass;
	private String condition;
	private String paramsJson;
	
	@Column(name = "CONDITION_EXPRESSION", nullable = true, columnDefinition = "TEXT")
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	
	@Column(name = "PARAMS_JSON", nullable = false, columnDefinition = "TEXT")
	public String getParamsJson() {
		return paramsJson;
	}
	public void setParamsJson(String paramsJson) {
		this.paramsJson = paramsJson;
	}
	
	@Column(name = "EVENT_CLASS", nullable = false)
	public Class<? extends ApplicationEvent> getEventClass() {
		return eventClass;
	}
	public void setEventClass(Class<? extends ApplicationEvent> eventClass) {
		this.eventClass = eventClass;
	}
	
	@Column(name = "HANDLER_CLASS", nullable = false)
	public Class<? extends EventHandler> getHandlerClass() {
		return handlerClass;
	}
	public void setHandlerClass(Class<? extends EventHandler> handlerClass) {
		this.handlerClass = handlerClass;
	}

}
