package com.poweredbypace.pace.legacy.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;


@Entity
@Table(name = "book")
@SuppressWarnings("serial")
public class Book implements Serializable {

	private Long bookId;
	
	private String username;	

	private Integer pageCount;
	
	private Integer coverPageCount;

	private List<Image> images = null;

	private List<Page> pages = null;
	
	private List<Guide> guides = null;

	private Boolean isAutoFlowChanged;
	
	private Long modified;
	

	/**
	 * <p> 
	 * </p>
	 * @return bookId
	 */
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "book_id")
	public Long getBookId() {
		return bookId;
	}

	/**
	 * @param bookId new value for bookId 
	 */
	public void setBookId(Long bookId) {
		if (bookId!=null && bookId==0)
			bookId = null;
		this.bookId = bookId;
	}

	@Basic
	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	/**
	 * <p> 
	 * </p>
	 * @return pageCount
	 */
	@Basic
	@Column(name = "page_count")
	public Integer getPageCount() {
		return pageCount;
	}

	/**
	 * @param pageCount new value for pageCount 
	 */
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}

	@Basic
	@Column(name = "cover_page_count")
	public Integer getCoverPageCount() {
		return coverPageCount;
	}

	public void setCoverPageCount(Integer coverPageCount) {
		this.coverPageCount = coverPageCount;
	}

	/**
	 * Get the list of images
	 */
	@OneToMany(mappedBy="book", fetch=FetchType.EAGER)
	@Fetch(value=FetchMode.JOIN)
	@NotFound(action=NotFoundAction.IGNORE) 
	public List<Image> getImages() {
		return this.images;
	}

	/**
	 * Set the list of images
	 */
	public void setImages(List<Image> images) {
		this.images = images;
	}
	/**
	 * Get the list of pages
	 */
	@OneToMany(mappedBy="book", fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@Fetch(value=FetchMode.SELECT)
	@OrderBy("pageNumber")
	public List<Page> getPages() {
		return this.pages;
	}

	/**
	 * Set the list of pages
	 */
	public void setPages(List<Page> pages) {
		this.pages = pages;
	}
	
	
	/**
	 * Get the list of guides
	 */
	@OneToMany(mappedBy="book", fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@Fetch(value=FetchMode.SELECT)
	public List<Guide> getGuides() {
		return this.guides;
	}

	/**
	 * Set the list of guides
	 */
	public void setGuides(List<Guide> guides) {
		this.guides = guides;
	}
	
	@Basic
	@Column(name = "is_auto_flow_changed")
	public Boolean getIsAutoFlowChanged() {
		return isAutoFlowChanged;
	}

	public void setIsAutoFlowChanged(Boolean isAutoFlowChanged) {
		this.isAutoFlowChanged = isAutoFlowChanged;
	}
	
	@Column(name = "modified")
	public Long getModified() {
		return modified;
	}

	public void setModified(Long modified) {
		this.modified = modified;
	}

	public Book()
	{
		super();
	}
	
	@Transient
	public String getModelType()
	{
		return "Book";
	}

}