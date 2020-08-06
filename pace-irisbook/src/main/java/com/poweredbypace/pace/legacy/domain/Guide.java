package com.poweredbypace.pace.legacy.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "guide")
@SuppressWarnings("serial")
public class Guide implements Serializable {

	private Long guideId;
	private Book book;	
	private Integer spreadIndex;
	private Double x1;
	private Double y1;
	private Double x2;
	private Double y2;

	@Basic
	@Id
	@GeneratedValue
	@Column(name = "guide_id")
	public Long getGuideId() {
		return guideId;
	}

	public void setGuideId(Long guideId) {
		this.guideId = guideId;
	}

	@ManyToOne
	@JoinColumn(name = "book_id")
	@JsonIgnore
	public Book getBook() {
		return this.book;
	}

	/**
	 * set book
	 */
	public void setBook(Book book) {
		this.book = book;
	}

	@Basic
	@Column(name = "spread_index")
	public Integer getSpreadIndex() {
		return spreadIndex;
	}

	public void setSpreadIndex(Integer spreadIndex) {
		this.spreadIndex = spreadIndex;
	}
	
	@Basic
	@Column(name = "x1")
	public Double getX1() {
		return x1;
	}

	public void setX1(Double x1) {
		this.x1 = x1;
	}

	@Basic
	@Column(name = "y1")
	public Double getY1() {
		return y1;
	}

	public void setY1(Double y1) {
		this.y1 = y1;
	}

	@Basic
	@Column(name = "x2")
	public Double getX2() {
		return x2;
	}

	public void setX2(Double x2) {
		this.x2 = x2;
	}

	@Basic
	@Column(name = "y2")
	public Double getY2() {
		return y2;
	}

	public void setY2(Double y2) {
		this.y2 = y2;
	}
	
	public Guide()
	{
		
	}
	
	public Guide(Guide guide, Book book)
	{
		super();
		this.book = book;	
		this.spreadIndex = guide.spreadIndex;
		this.x1 = guide.x1;
		this.y1 = guide.y1;
		this.x2 = guide.x2;
		this.y2 = guide.y2;
	}


}