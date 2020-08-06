package com.poweredbypace.pace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.SimpleUserSerializer;

@Entity
@Table(name = "P_ATTACHMENT")
public class Attachment extends BaseEntity {
	
	private static final long serialVersionUID = 6188238761317986808L;
	
	public enum AttachmentType {
		CoverJpeg,
		CoverTiff,
		CoverPdf,
		
		HiResJpeg,
		HiResTiff,
		HiResPdf,
		
		CameoJpeg,
		CameoTiff,
		CameoPdf,
		
		DiePng,
		DieBmp,
		DieZip,
		
		LogoPng,
		LogoBmp,
		LogoZip,
		
		LowResPdf,
		JobTicket,
		BinderyForm,
		Invoice
	}
	
	private AttachmentType type;
	private String url;
	private Date date;
	private Integer documentVersion;
	private Product product;
	private User user;
	private Long checksum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", nullable = true)
	@JsonSerialize(using = SimpleUserSerializer.class)
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID", nullable = false)
	@JsonIgnore
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@Column(name="TYPE", nullable=false)
	@Enumerated(EnumType.STRING)
	public AttachmentType getType() {
		return type;
	}
	public void setType(AttachmentType type) {
		this.type = type;
	}
	
	@Column(name="URL")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@Column(name="DATE")
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Column(name="DOC_VERSION")
	public Integer getDocumentVersion() {
		return documentVersion;
	}
	public void setDocumentVersion(Integer documentVersion) {
		this.documentVersion = documentVersion;
	}
	
	@Column(name="CHECKSUM")
	public Long getChecksum() {
		return checksum;
	}
	public void setChecksum(Long checksum) {
		this.checksum = checksum;
	}
	
	
}
