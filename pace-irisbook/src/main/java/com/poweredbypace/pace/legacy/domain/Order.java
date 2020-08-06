package com.poweredbypace.pace.legacy.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Formula;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "`order`")
@SuppressWarnings("serial")
public class Order implements Serializable {

	public static class OrderType {
		public static final String LUXE = "Luxe";
		public static final String SOUL = "Soul";
		public static final String PURE	= "Pure";
		public static final String FM_LUXE = "FM-Luxe";
		public static final String FM_SOUL = "FM-Soul";
		public static final String FM_PURE	= "FM-Pure";
		
		public static final String PORT	= "Port";
		public static final String CUSTOM = "Custom";
		public static final String TS = "TS"; 
		public static final String HEMLOCK_TS = "HEMLOCK-TS"; 
	}
	
	public static class JobType {
		public static final String REPRINT = "Reprint";
		public static final String STANDARD = "Standard"; 
		public static final String COVER = "Cover"; 
		public static final String PACE = "Pace";
	}
	
	public static class FileType
    {
        public static final String PDF = "PDF";
		public static final String JPEGS = "JPEGS";
    }
	
	public static class OrderCategory
    {
        public static final String COVER = "Cover";
        public static final String HOLD = "Hold";
        public static final String REPRINT = "Reprint";
        public static final String READY_TO_PRINT = "ReadyToPrint";
        public static final String DESIGNED = "Designed";
        public static final String UNASSIGNED = "Unassigned";
        public static final String COMPLETED = "Completed";
        public static final String TEMPLATES = "Templates";
        public static final String BATCH = "Batch";
        public static final String PACE = "PACE";
    }
	
	private Long orderId;

	private String username;

	private Long bookId;
	
	private Date orderDate;

	private String fileType;
	
	private Boolean isLocked;
	
	private Integer numImages;
	
	private String heavyCoverage;
	
	private Long modified;
	
	private Boolean coverPdfMailSent;
	
	private Long reprintOrderId;
	
	private Boolean printReady;
	
	private Integer pageCount;
	
	private List<BookDuplicate> bookDuplicates = null;

	private BookDetails bookDetails;

	private String userFullname;
	
	private String userCompany;
	
	private Boolean isDefaultTemplate;
	
	private String reprintJobType;
	
	private String jobType;
	
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "order_id")
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		if (orderId!=null && orderId==0)
			orderId=null;
		this.orderId = orderId;
	}

	@Basic
	@Column(name = "username")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	@Basic
	@Column(name = "book_id")
	public Long getBookId() {
		return bookId;
	}

	public void setBookId(Long bookId) {
		this.bookId = bookId;
	}

	@Basic
	@Column(name = "order_date")
	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	

	@Column(name = "file_type", length = 200)
	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

		
	@Column(name = "is_locked")
	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	@Column(name="num_images")
	public Integer getNumImages() {
		return numImages;
	}

	public void setNumImages(Integer numImages) {
		this.numImages = numImages;
	}

	@Column(name = "heavy_coverage")
	public String getHeavyCoverage() {
		return heavyCoverage;
	}

	public void setHeavyCoverage(String heavyCoverage) {
		this.heavyCoverage = heavyCoverage;
	}
	
	@Column(name = "modified")
	public Long getModified() {
		return modified;
	}

	public void setModified(Long modified) {
		this.modified = modified;
	}
	
	@Column(name = "cover_pdf_mail_sent")
	public Boolean getCoverPdfMailSent() {
		return coverPdfMailSent;
	}

	public void setCoverPdfMailSent(Boolean coverPdfMailSent) {
		this.coverPdfMailSent = coverPdfMailSent;
	}

	@Transient
	public String getJobName()
	{
		return bookDetails!=null ? bookDetails.getJobName() : null;
	}
	
	@Transient
	public String getJobId() {
		return bookDetails!=null ? bookDetails.getJobId() : null;
	}

	@Column(name = "reprint_order_id")
	public Long getReprintOrderId() {
		return reprintOrderId;
	}

	public void setReprintOrderId(Long reprintOrderId) {
		if (reprintOrderId!=null && reprintOrderId==0)
			reprintOrderId=null;
		this.reprintOrderId = reprintOrderId;
	}
	
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
	
	/**
	 * Get the list of BookDuplicate
	 */
	@OneToMany(mappedBy="order", fetch=FetchType.EAGER, cascade={CascadeType.ALL})
	@Fetch(value=FetchMode.SELECT)
	public List<BookDuplicate> getBookDuplicates() {
		return this.bookDuplicates;
	}

	/**
	 * Set the list of BookDuplicate
	 */
	public void setBookDuplicates(List<BookDuplicate> bookDuplicates) {
		this.bookDuplicates = bookDuplicates;
	}
	
	@ManyToOne(cascade={CascadeType.MERGE, CascadeType.REMOVE})
	@JoinColumn(name = "book_details_id")
	public BookDetails getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(BookDetails bookDetails) {
		this.bookDetails = bookDetails;
	}

	@Column(name = "print_ready")
	public Boolean getPrintReady() {
		return printReady;
	}

	public void setPrintReady(Boolean printReady) {
		this.printReady = printReady;
	}

	@Formula("(select concat(u.first_name, ' ', u.last_name) from user u where u.username = username)")
	public String getUserFullname() {
		return userFullname;
	}

	public void setUserFullname(String userFullname) {
		this.userFullname = userFullname;
	}
	
	@Formula("(select u.company_name from user u where u.username = username)")
	public String getUserCompany() {
		return userCompany;
	}

	public void setUserCompany(String userCompany) {
		this.userCompany = userCompany;
	}
	
	
	@Column(name = "is_default_template")
	public Boolean getIsDefaultTemplate() {
		return isDefaultTemplate;
	}

	public void setIsDefaultTemplate(Boolean isDefaultTemplate) {
		this.isDefaultTemplate = isDefaultTemplate;
	}
	
	
	@Column(name = "job_type")
	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public Order() {
		super();
	}

	public Order(Order o)
	{
		super();
		this.username = o.username;
		this.bookId = o.bookId;
		this.orderDate = o.orderDate;
		this.fileType = o.fileType;
		this.isLocked = o.isLocked;
		this.numImages = o.numImages;
		this.heavyCoverage = o.heavyCoverage;
		this.modified = o.modified;
		this.coverPdfMailSent = o.coverPdfMailSent;
		this.reprintOrderId = o.reprintOrderId;
		this.printReady = o.printReady;
		this.pageCount = o.pageCount;
		this.userFullname = o.userFullname;
		this.userCompany = o.userCompany;
		this.isDefaultTemplate = o.isDefaultTemplate;
		this.bookDetails = new BookDetails(o.bookDetails);
		this.bookDuplicates = new ArrayList<BookDuplicate>();
		this.reprintJobType = o.reprintJobType;
		this.jobType = o.jobType;
		
		if (o.bookDuplicates!=null)
		{
			for(BookDuplicate dup:o.bookDuplicates)
			{
				this.bookDuplicates.add(new BookDuplicate(dup, this));
			}
		}
	}
	
	@Transient
	@JsonIgnore
	public BookDuplicate getBookDuplicate(Long bookDuplicateId)
	{
		for(BookDuplicate dup:this.getBookDuplicates())
		{
			if (dup.getBookDuplicateId().equals(bookDuplicateId))
			{
				return dup;
			}
		}
		return null;
	}
	
	@Transient 
	@JsonIgnore
	public BookDetails getBookDetails(Long bookDuplicateId)
	{
		if (bookDuplicateId!=null)
		{
			BookDuplicate dup = getBookDuplicate(bookDuplicateId);
			if (dup!=null)
				return dup.getBookDetails();
			else
				return null;
		} else
			return getBookDetails();
	}
	
	@Transient
	@JsonIgnore
	public List<BookDetails> getAllBookDetails()
	{
		List<BookDetails> result = new ArrayList<BookDetails>();
		result.add(getBookDetails());
		if (getBookDuplicates()!=null)
		{
			for(BookDuplicate dup:getBookDuplicates())
				result.add(dup.getBookDetails());
		}
		return result;
	}
	
	@Transient
	public String getModelType()
	{
		return "Order";
	}
}