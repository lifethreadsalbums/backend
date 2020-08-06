package com.poweredbypace.pace.notifications;

import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.poweredbypace.pace.util.JsonUtil;


public class Notification {
	
	public enum NotificationType {
		//EntityAdd,
		EntityChange,
		EntityDelete,
		PartialNotification,
		JobProgress,
		OrderCreated,
		UserRegistered,
		OrderStateChanged,
		ProductStateChanged,
		BatchSentToPrint,
		IccProfileConverted,
		CommentTyping,
		LayoutApproved,
		LayoutUnapproved
	}
	
	public enum NotificationTarget {
		All,
		AllExceptSender
	}
	
	private String id;
	private NotificationType type;
	private String senderId;
	private String entityType;
	private NotificationTarget target;
	private String body;
	private int numParts;
	private int partIndex;
	private NotificationType originalType;
	
	public int getNumParts() {
		return numParts;
	}
	public void setNumParts(int numParts) {
		this.numParts = numParts;
	}
	public int getPartIndex() {
		return partIndex;
	}
	public void setPartIndex(int partIndex) {
		this.partIndex = partIndex;
	}
	
	@Enumerated(EnumType.STRING)
	public NotificationType getOriginalType() {
		return originalType;
	}
	public void setOriginalType(NotificationType originalType) {
		this.originalType = originalType;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	@Enumerated(EnumType.STRING)
	public NotificationType getType() {
		return type;
	}
	public void setType(NotificationType type) {
		this.type = type;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	@Enumerated(EnumType.STRING)
	public NotificationTarget getTarget() {
		return target;
	}
	public void setTarget(NotificationTarget target) {
		this.target = target;
	}
	
	public Notification() {
		this.id = UUID.randomUUID().toString();
	}
	
	public static Notification create(NotificationType type, Object o) {
		return create(type, o, NotificationTarget.All);
	}
	
	public static Notification create(NotificationType type, Object o, NotificationTarget target) {
		Notification n = new Notification();
		n.setType(type);
		n.setSenderId(getSessionId());
		n.setTarget(target);
		n.setBody(JsonUtil.serialize(o));
		
		String entityType = o.getClass().getName();
		if (o instanceof HibernateProxy) { 
			entityType = ((HibernateProxy) o).getHibernateLazyInitializer().getImplementation().getClass().getName();
		}
		
		n.setEntityType(entityType);
		return n;
	}
	
	private static String getSessionId() {
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) 
					RequestContextHolder.currentRequestAttributes();
			
			if (attr!=null)
				return attr.getSessionId();
		} catch(Throwable t) {}
		
		return null;
	}
	
	
}
