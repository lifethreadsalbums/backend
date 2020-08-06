package com.poweredbypace.pace.push;

import java.util.UUID;


public class Notification {
	
	public static final String ALL = "ALL";
	public static final String ALL_EXCEPT_SENDER = "ALL_EXCEPT_SENDER";
	
	private String id;
	private String type;
	private String senderId;
	private String target;
	private String body;
	private int numParts;
	private int partIndex;
	private String originalType;
	
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
	
	public String getOriginalType() {
		return originalType;
	}
	public void setOriginalType(String originalType) {
		this.originalType = originalType;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
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
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public Notification() {
		this.id = UUID.randomUUID().toString();
	}
	public Notification(String type) {
		super();
		this.type = type;
	}
	
	
}
