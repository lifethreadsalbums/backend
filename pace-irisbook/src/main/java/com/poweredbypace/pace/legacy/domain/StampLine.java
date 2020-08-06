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
@Table(name = "stamp_line")
@SuppressWarnings("serial")
public class StampLine implements Serializable {

	/**
	 * Attribute stampLineId.
	 */
	private Long stampLineId;
	
	/**
	 * Attribute bookDetails
	 */
	private BookDetails bookDetails;	

	/**
	 * Attribute stampFoil.
	 */
	private String stampFoil;
	
	/**
	 * Attribute stampFont.
	 */
	private String stampFont;
	
	/**
	 * Attribute stampCase.
	 */
	private String stampCase;
	
	/**
	 * Attribute stampText.
	 */
	private String stampText;
	
	private Boolean bookStamp;
	
	private Boolean boxStamp;
	
	
	/**
	 * <p> 
	 * </p>
	 * @return stampLineId
	 */
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "stamp_line_id")
		public Long getStampLineId() {
		return stampLineId;
	}

	/**
	 * @param stampLineId new value for stampLineId 
	 */
	public void setStampLineId(Long stampLineId) {
		if (stampLineId!=null && stampLineId==0)
			stampLineId = null;
		this.stampLineId = stampLineId;
	}
	
	/**
	 * get bookDetails
	 */
	@ManyToOne
	@JoinColumn(name = "book_details_id")
	@JsonIgnore
	public BookDetails getBookDetails() {
		return this.bookDetails;
	}
	
	/**
	 * set bookDetails
	 */
	public void setBookDetails(BookDetails bookDetails) {
		this.bookDetails = bookDetails;
	}

	/**
	 * <p> 
	 * </p>
	 * @return stampFoil
	 */
	@Basic
	@Column(name = "stamp_foil", length = 200)
		public String getStampFoil() {
		return stampFoil;
	}

	/**
	 * @param stampFoil new value for stampFoil 
	 */
	public void setStampFoil(String stampFoil) {
		this.stampFoil = stampFoil;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return stampFont
	 */
	@Basic
	@Column(name = "stamp_font", length = 200)
		public String getStampFont() {
		return stampFont;
	}

	/**
	 * @param stampFont new value for stampFont 
	 */
	public void setStampFont(String stampFont) {
		this.stampFont = stampFont;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return stampCase
	 */
	@Basic
	@Column(name = "stamp_case", length = 200)
		public String getStampCase() {
		return stampCase;
	}

	/**
	 * @param stampCase new value for stampCase 
	 */
	public void setStampCase(String stampCase) {
		this.stampCase = stampCase;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return stampText
	 */
	@Basic
	@Column(name = "stamp_text", length = 1000)
		public String getStampText() {
		return stampText;
	}

	/**
	 * @param stampText new value for stampText 
	 */
	public void setStampText(String stampText) {
		this.stampText = stampText;
	}

	@Basic
	@Column(name = "book_stamp")
	public Boolean getBookStamp() {
		return bookStamp;
	}

	public void setBookStamp(Boolean bookStamp) {
		this.bookStamp = bookStamp;
	}

	@Basic
	@Column(name = "box_stamp")
	public Boolean getBoxStamp() {
		return boxStamp;
	}

	public void setBoxStamp(Boolean boxStamp) {
		this.boxStamp = boxStamp;
	}
	
	
	
	public StampLine() {
		super();
	}

	public StampLine(StampLine sl)
	{
		this.stampFoil = sl.stampFoil;
		this.stampFont = sl.stampFont;
		this.stampCase = sl.stampCase;
		this.stampText = sl.stampText;
		this.bookStamp = sl.bookStamp;
		this.boxStamp = sl.boxStamp;
	}
	
	public StampLine(StampLine sl, BookDetails bd) 
	{
		this(sl);
		this.bookDetails = bd;
	}


}