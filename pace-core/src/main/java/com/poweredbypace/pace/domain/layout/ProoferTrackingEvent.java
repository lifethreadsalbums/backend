package com.poweredbypace.pace.domain.layout;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.Product;

@Entity
@Table(name="P_PROOFER_TRACKING_EVENT")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProoferTrackingEvent extends BaseEntity {
	
	private static final long serialVersionUID = -3719903108584970493L;

	public static enum ProoferTrackingEventType {
		EditsCompleted,
		EditsCompletedReminder,
		EditsPending
	}
	
	private ProoferTrackingEventType type;
	private Date date;
	private Long checksum;
	private Product product;

	
	@Column(name="TYPE")
	@Enumerated(EnumType.STRING)
	public ProoferTrackingEventType getType() {
		return type;
	}

	public void setType(ProoferTrackingEventType type) {
		this.type = type;
	}
	
	@Column(name="DATE")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name="CHECKSUM")
	public Long getChecksum() {
		return checksum;
	}
	public void setChecksum(Long checksum) {
		this.checksum = checksum;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUCT_ID")
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
}
