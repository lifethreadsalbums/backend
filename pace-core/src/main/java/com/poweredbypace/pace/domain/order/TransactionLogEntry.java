package com.poweredbypace.pace.domain.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.user.User;

@Entity
@Table(name = "O_TRANSACTION_LOG_ENTRY")
public class TransactionLogEntry extends BaseEntity {
	
	private static final long serialVersionUID = -3006713565019421053L;
	private Order order;
	private String type;
	private String transactionId;
	private String message;
	private Date date;
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "ORDER_ID")
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "TRANSACTION_ID")
	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	
	@Column(name = "MESSAGE", columnDefinition = "TEXT")
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Column(name = "DATE")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@ManyToOne
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
