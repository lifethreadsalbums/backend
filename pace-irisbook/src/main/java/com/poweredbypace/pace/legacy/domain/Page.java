package com.poweredbypace.pace.legacy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "page")
@SuppressWarnings("serial")
public class Page implements Serializable {

	/**
	 * Attribute pageId.
	 */
	private Long pageId;

	/**
	 * Attribute book
	 */
	private Book book;	

	/**
	 * Attribute pageNumber.
	 */
	private Integer pageNumber;

	/**
	 * Attribute backgroundColor.
	 */
	private Integer backgroundColor;

	/**
	 * List of PlacedImage
	 */
	private List<PlacedElement> placedImages = null;

	private Integer numPages;

	/**
	 * <p> 
	 * </p>
	 * @return pageId
	 */
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "page_id")
	public Long getPageId() {
		return pageId;
	}

	/**
	 * @param pageId new value for pageId 
	 */
	public void setPageId(Long pageId) {
		if (pageId!=null && pageId==0)
			pageId = null;

		this.pageId = pageId;
	}

	/**
	 * get book
	 */
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

	/**
	 * <p> 
	 * </p>
	 * @return pageNumber
	 */
	@Basic
	@Column(name = "page_number")
	public Integer getPageNumber() {
		return pageNumber;
	}

	/**
	 * @param pageNumber new value for pageNumber 
	 */
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}

	/**
	 * <p> 
	 * </p>
	 * @return backgroundColor
	 */
	@Basic
	@Column(name = "background_color")
	public Integer getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor new value for backgroundColor 
	 */
	public void setBackgroundColor(Integer backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	/**
	 * Get the list of PlacedImage
	 */
	@OneToMany(mappedBy="page", fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@Fetch(value=FetchMode.SUBSELECT)
	@OrderBy("indexZ")
	public List<PlacedElement> getPlacedImages() {
		return this.placedImages;
	}

	/**
	 * Set the list of PlacedImage
	 */
	public void setPlacedImages(List<PlacedElement> placedImages) {
		this.placedImages = placedImages;
	}

	@Basic
	@Column(name = "num_pages")
	public Integer getNumPages() {
		return numPages;
	}

	public void setNumPages(Integer numPages) {
		this.numPages = numPages;
	}

	public Page()
	{
		super();
	}
	
	public Page(Page page, Book book)
	{
		super();
		this.book = book;	
		this.pageNumber = page.pageNumber;
		this.backgroundColor = page.backgroundColor;
		this.numPages = page.numPages;
		if (page.placedImages!=null)
		{
			this.placedImages = new ArrayList<PlacedElement>();
			
			for(PlacedElement el:page.placedImages)
			{
				this.placedImages.add( new PlacedElement(el, this) );
			}
		}
	}


}