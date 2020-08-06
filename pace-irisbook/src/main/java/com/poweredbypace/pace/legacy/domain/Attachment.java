package com.poweredbypace.pace.legacy.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "attachment")
public class Attachment {
	
	public static class AttachmentType {
		public static final String JPEG_COVER = "JPEG_COVER";
		public static final String HI_RES_JPEG = "HI_RES_JPEG";
		public static final String PDF_COVER = "PDF_COVER";
		public static final String HI_RES_PDF = "HI_RES_PDF";
		public static final String LOW_RES_PDF = "LOW_RES_PDF";
		public static final String JOB_TICKET = "JOB_TICKET";
		public static final String BINDERY_FORM = "BINDERY_FORM";
		public static final String INVOICE = "INVOICE";
	}

	private Long attachmentId;
	private Long bookDetailsId;
	private String username;
	private String type;
	private String url;
	private Date date;
	private Integer documentVersion;
	private Long modified;
	private String modifiedBy;
	private Integer version;
	
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "attachment_id", nullable=false)
	public Long getAttachmentId() {
		return attachmentId;
	}
	public void setAttachmentId(Long attachmentId) {
		if (attachmentId!=null && attachmentId==0)
			attachmentId = null;
		this.attachmentId = attachmentId;
	}
	
	@Basic
	@Column(name = "book_details_id")
	public Long getBookDetailsId() {
		return bookDetailsId;
	}
	public void setBookDetailsId(Long bookDetailsId) {
		if (bookDetailsId!=null && bookDetailsId==0)
			bookDetailsId = null;
		this.bookDetailsId = bookDetailsId;
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
	@Column(name = "type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Basic
	@Column(name = "url")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Basic
	@Column(name = "date")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Basic
	@Column(name = "document_version")
	public Integer getDocumentVersion() {
		return documentVersion;
	}
	public void setDocumentVersion(Integer documentVersion) {
		this.documentVersion = documentVersion;
	}
	
	@Basic
	@Column(name = "modified")
	public Long getModified() {
		return modified;
	}
	public void setModified(Long modified) {
		this.modified = modified;
	}
	
	@Basic
	@Column(name = "modified_by")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Basic
	@Column(name = "version")
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Attachment() {
		super();
	}
	
	public Attachment(Attachment a) {
		super();
		this.bookDetailsId = a.bookDetailsId;
		this.username = a.username;
		this.type = a.type;
		this.url = a.url;
		this.documentVersion = a.documentVersion;
		this.date = a.date!=null ? new Date( a.date.getTime() ) : null;
		this.modified = a.modified;
		this.modifiedBy = a.modifiedBy;
		this.version = a.version;
	}
	
	@Transient
	public String getModelType()
	{
		return "Attachment";
	}
}
