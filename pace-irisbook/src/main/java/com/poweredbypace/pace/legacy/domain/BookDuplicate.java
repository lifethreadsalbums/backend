package com.poweredbypace.pace.legacy.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "book_duplicate")
@SuppressWarnings("serial")
public class BookDuplicate implements Serializable {

	private Long bookDuplicateId;
	private Order order;	
	private BookDetails bookDetails;
	
	
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "book_duplicate_id")
	public Long getBookDuplicateId() {
		return bookDuplicateId;
	}

	public void setBookDuplicateId(Long bookDuplicateId) {
		if (bookDuplicateId!=null && bookDuplicateId==0)
			bookDuplicateId = null;
		this.bookDuplicateId = bookDuplicateId;
	}

	@ManyToOne
	@JoinColumn(name = "order_id")
	@JsonIgnore
	public Order getOrder() {
		return this.order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "book_details_id")
	public BookDetails getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(BookDetails bookDetails) {
		this.bookDetails = bookDetails;
	}
	
	public BookDuplicate() {
		super();
	}

	public BookDuplicate(BookDuplicate dup, Order o)
	{
		this.bookDetails = new BookDetails(dup.bookDetails);
		this.order = o;
	}
	
}