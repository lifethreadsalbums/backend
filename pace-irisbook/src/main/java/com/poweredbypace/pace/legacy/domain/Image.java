package com.poweredbypace.pace.legacy.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "image")
@SuppressWarnings("serial")
public class Image implements Serializable, Cloneable {
	
	public static final String PREFLIGHTING = "PREFLIGHTING";
	public static final String PENDING_UPLOAD = "PENDING_UPLOAD";
	public static final String UPLOAD_IN_PROGRESS = "UPLOAD_IN_PROGRESS";
	public static final String UPLOAD_COMPLETED = "UPLOAD_COMPLETED";
	public static final String UPLOAD_ERROR = "UPLOAD_ERROR";
	public static final String UPLOAD_REJECTED = "UPLOAD_REJECTED";
	public static final String READY = "READY";

	/**
	 * Attribute imageId.
	 */
	private Long imageId;
	
	/**
	 * Attribute username
	 */
	 private String username;	

	/**
	 * Attribute book
	 */
	 private Book book;	

	/**
	 * Attribute dpiX.
	 */
	private Integer dpiX;
	
	/**
	 * Attribute dpiY.
	 */
	private Integer dpiY;
	
	/**
	 * Attribute width.
	 */
	private Integer width;
	
	/**
	 * Attribute height.
	 */
	private Integer height;
	
	/**
	 * Attribute urlOriginal.
	 */
	private String urlOriginal;
	
	/**
	 * Attribute urlLowRes.
	 */
	private String urlLowRes;
	
	/**
	 * Attribute urlThumbnail.
	 */
	private String urlThumbnail;
	
	/**
	 * Attribute order.
	 */
	private Integer listOrder;
	
	private String filename;

	private Boolean isDeleted;
	
	private Long size;
	
	private String colorSpace;
	
	private String iccProfile;
	
	private String status;
	
	private String errorString;
	
	private Date creationDate;
	
	private String batchId;
	
	private Boolean isCoverImage;
	
	private Boolean isDoublePageSpread;
	
	private Long modified;
	
	private String group;
	
	private String cmykConversionMethod;
	
	//private Boolean bw;
		/**
	 * <p> 
	 * </p>
	 * @return imageId
	 */
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "image_id")
		public Long getImageId() {
		return imageId;
	}

	/**
	 * @param imageId new value for imageId 
	 */
	public void setImageId(Long imageId) {
		if (imageId!=null && imageId==0)
			imageId = null;
		this.imageId = imageId;
	}
	
	/**
	 * get username
	 */
	@Basic
	@Column(name = "username", length=200)
	public String getUsername() {
		return this.username;
	}
	
	/**
	 * set username
	 */
	public void setUsername(String username) {
		this.username = username;
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
	 * @return dpiX
	 */
	@Basic
	@Column(name = "dpi_x")
		public Integer getDpiX() {
		return dpiX;
	}

	/**
	 * @param dpiX new value for dpiX 
	 */
	public void setDpiX(Integer dpiX) {
		this.dpiX = dpiX;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return dpiY
	 */
	@Basic
	@Column(name = "dpi_y")
		public Integer getDpiY() {
		return dpiY;
	}

	/**
	 * @param dpiY new value for dpiY 
	 */
	public void setDpiY(Integer dpiY) {
		this.dpiY = dpiY;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return width
	 */
	@Basic
	@Column(name = "width")
		public Integer getWidth() {
		return width;
	}

	/**
	 * @param width new value for width 
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return height
	 */
	@Basic
	@Column(name = "height")
		public Integer getHeight() {
		return height;
	}

	/**
	 * @param height new value for height 
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return urlOriginal
	 */
	@Basic
	@Column(name = "url_original", length = 200)
	public String getUrlOriginal() {
		return urlOriginal;
	}

	/**
	 * @param urlOriginal new value for urlOriginal 
	 */
	public void setUrlOriginal(String urlOriginal) {
		this.urlOriginal = urlOriginal;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return urlLowRes
	 */
	@Basic
	@Column(name = "url_low_res", length = 200)
	public String getUrlLowRes() {
		return urlLowRes;
	}

	/**
	 * @param urlLowRes new value for urlLowRes 
	 */
	public void setUrlLowRes(String urlLowRes) {
		this.urlLowRes = urlLowRes;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return urlThumbnail
	 */
	@Basic
	@Column(name = "url_thumbnail", length = 200)
		public String getUrlThumbnail() {
		return urlThumbnail;
	}

	/**
	 * @param urlThumbnail new value for urlThumbnail 
	 */
	public void setUrlThumbnail(String urlThumbnail) {
		this.urlThumbnail = urlThumbnail;
	}
	
	/**
	 * <p> 
	 * </p>
	 * @return order
	 */
	@Basic
	@Column(name = "list_order")
		public Integer getListOrder() {
		return listOrder;
	}

	/**
	 * @param order new value for order 
	 */
	public void setListOrder(Integer listOrder) {
		this.listOrder = listOrder;
	}

	@Basic
	@Column(name = "filename", length = 256)
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Basic
	@Column(name = "is_deleted")
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	@Basic
	@Column(name = "size")
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Basic
	@Column(name = "color_space")
	public String getColorSpace() {
		return colorSpace;
	}

	public void setColorSpace(String colorSpace) {
		this.colorSpace = colorSpace;
	}

	@Basic
	@Column(name = "icc_profile", length=200)
	public String getIccProfile() {
		return iccProfile;
	}

	public void setIccProfile(String iccProfile) {
		this.iccProfile = iccProfile;
	}
	
	
	@Basic
	@Column(name = "status", length=100)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Basic
	@Column(name = "error_string", length=1000)
	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	
	@Basic
	@Column(name = "creation_date")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	
	@Basic
	@Column(name = "batch_id", length=36)
	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	
	
	@Basic
	@Column(name = "is_cover_image")
	public Boolean getIsCoverImage() {
		return isCoverImage;
	}

	public void setIsCoverImage(Boolean isCoverImage) {
		this.isCoverImage = isCoverImage;
	}
	
	@Basic
	@Column(name = "is_double_page_spread")
	public Boolean getIsDoublePageSpread() {
		return isDoublePageSpread;
	}

	public void setIsDoublePageSpread(Boolean isDoublePageSpread) {
		this.isDoublePageSpread = isDoublePageSpread;
	}

	@Column(name = "modified")
	public Long getModified() {
		return modified;
	}

	public void setModified(Long modified) {
		this.modified = modified;
	}
	
	@Column(name = "`group`")
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	@Column(name = "cmyk_conversion_method")
	public String getCmykConversionMethod() {
		return cmykConversionMethod;
	}

	public void setCmykConversionMethod(String cmykConversionMethod) {
		this.cmykConversionMethod = cmykConversionMethod;
	}
	
//	@Column(name = "bw")
//	public Boolean getBw() {
//		return bw;
//	}
//
//	public void setBw(Boolean bw) {
//		this.bw = bw;
//	}

	public Image()
	{
		super();
	}
	
	public Image(Image image, Book book)
	{
		super();
		
		this.setBook(book);
		this.setColorSpace(image.colorSpace);
		this.setDpiX(image.dpiX);
		this.setDpiY(image.dpiY);
		this.setFilename(image.filename);
		this.setHeight(image.height);
		this.setIccProfile(image.iccProfile);
		this.setIsDeleted(image.isDeleted);
		this.setListOrder(image.listOrder);
		this.setSize(image.size);
		this.setUrlLowRes(image.urlLowRes);
		this.setUrlOriginal(image.urlOriginal);
		this.setUrlThumbnail(image.urlThumbnail);
		this.setUsername(image.username);
		this.setWidth(image.width);
		this.setStatus(image.status);
		this.setErrorString(image.errorString);
		this.setCreationDate(image.creationDate);
		this.setBatchId(image.batchId);
		this.setIsCoverImage(image.isCoverImage);
		this.setIsDoublePageSpread(image.isDoublePageSpread);
		this.setModified(image.modified);
		this.setGroup(image.group);
		this.setCmykConversionMethod(image.cmykConversionMethod);
	}


	public Object clone()
	{
		Image obj = new Image();
		obj.setBook(book);
		obj.setColorSpace(colorSpace);
		obj.setDpiX(dpiX);
		obj.setDpiY(dpiY);
		obj.setFilename(filename);
		obj.setHeight(height);
		obj.setIccProfile(iccProfile);
		obj.setImageId(imageId);
		obj.setIsDeleted(isDeleted);
		obj.setListOrder(listOrder);
		obj.setSize(size);
		obj.setUrlLowRes(urlLowRes);
		obj.setUrlOriginal(urlOriginal);
		obj.setUrlThumbnail(urlThumbnail);
		obj.setUsername(username);
		obj.setWidth(width);
		obj.setStatus(status);
		obj.setErrorString(errorString);
		obj.setCreationDate(creationDate);
		obj.setBatchId(batchId);
		obj.setIsCoverImage(isCoverImage);
		obj.setIsDoublePageSpread(isDoublePageSpread);
		obj.setModified(modified);
		obj.setGroup(group);
		obj.setCmykConversionMethod(cmykConversionMethod);
		return obj;
	}
	
	@Transient
	public String getModelType()
	{
		return "Image";
	}

}