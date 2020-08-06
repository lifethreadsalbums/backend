package com.poweredbypace.pace.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="P_BATCH")
public class Batch extends BaseEntity {

	public static enum BatchState {
		Queued,
		Printed,
		Processing
	}
	private static final long serialVersionUID = -4451318557339572793L;

	private String name;
	private Date dateCreated;
	private Date datePrinted;
	private List<Product> products = new ArrayList<Product>();
	private BatchState state;
	
	@Column(name="NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="DATE_CREATED")
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	@Column(name="DATE_PRINTED")
	public Date getDatePrinted() {
		return datePrinted;
	}
	public void setDatePrinted(Date datePrinted) {
		this.datePrinted = datePrinted;
	}
	
	@JsonIgnore
	@OneToMany(
		fetch = FetchType.LAZY, 
		mappedBy = "batch" 
	)
	@LazyCollection(LazyCollectionOption.EXTRA)
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	
	@Column(name="STATE")
	@Enumerated(EnumType.STRING)
	public BatchState getState() {
		return state;
	}
	public void setState(BatchState state) {
		this.state = state;
	}
	
}
