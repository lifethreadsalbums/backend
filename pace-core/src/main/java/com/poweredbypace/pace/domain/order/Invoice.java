package com.poweredbypace.pace.domain.order;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name = "O_INVOICE")
public class Invoice extends BaseEntity {

	private static final long serialVersionUID = 2830219005938296545L;
	private String invoiceNumber;
	private Order order;
	private Date dateCreated;
	
	public Invoice() { }

	@Column(name="INVOICE_NR")
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	@OneToOne
	@JoinColumn(name = "ORDER_ID", nullable=false)
	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Column(name="DATE_CREATED")
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	

}
