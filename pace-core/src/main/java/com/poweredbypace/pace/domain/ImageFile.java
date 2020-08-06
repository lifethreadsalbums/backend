package com.poweredbypace.pace.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.ApplicationConstants;
import com.poweredbypace.pace.json.SimpleIccProfileSerializer;

@Entity
@DiscriminatorValue("IMAGE")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ImageFile extends File {

	private static final long serialVersionUID = -2470070623133898917L;

	public static final String GENERIC_UPLOAD_ERROR_MESSAGE = "An error occurred during file upload. Please re-upload this image.";
	
	public enum ImageFileStatus {
		New,
		Preflighted,
		UploadInProgress,
		Uploaded,
		Rejected,
		Cancelled
	}
	
	private ImageFileStatus status;
	private Integer dpiX;
	private Integer dpiY;
	private Integer width;
	private Integer height;
	private Long size;
	private String colorSpace;
	private String iccProfile;
	private String errorMessage;
	private Date creationDate;
	private Integer rating;
	private Integer orientation;
	private Boolean isBlackAndWhite;
	private IccProfile targetIccProfile;
	private IccProfile customIccProfile;
	private String internalId;
	
	@Column(name="INTERNAL_ID")
	@JsonProperty("_id")
	public String getInternalId() { return internalId; }
	public void setInternalId(String internalId) { this.internalId = internalId; }
	
	@Enumerated(EnumType.STRING)
	@Column(name = "IMG_STATUS")
	public ImageFileStatus getStatus() {
		return status;
	}

	public void setStatus(ImageFileStatus status) {
		this.status = status;
	}

	@Column(name = "IMG_DPI_X")
	public Integer getDpiX() {
		return dpiX;
	}

	public void setDpiX(Integer dpiX) {
		this.dpiX = dpiX;
	}

	@Column(name = "IMG_DPI_Y")
	public Integer getDpiY() {
		return dpiY;
	}

	public void setDpiY(Integer dpiY) {
		this.dpiY = dpiY;
	}

	@Column(name = "IMG_WIDTH")
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	@Column(name = "IMG_HEIGHT")
	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Column(name = "IMG_SIZE")
	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	@Column(name = "IMG_COLOR_SPACE")
	public String getColorSpace() {
		return colorSpace;
	}

	public void setColorSpace(String colorSpace) {
		this.colorSpace = colorSpace;
	}

	@Column(name = "IMG_ICC_PROFILE")
	public String getIccProfile() {
		return iccProfile;
	}

	public void setIccProfile(String iccProfile) {
		this.iccProfile = iccProfile;
	}

	@Column(name = "IMG_ERROR_MESSAGE")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Column(name = "IMG_CREATE_DATE")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "IMG_RATING")
	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	@Column(name = "IMG_ORIENTATION")
	public Integer getOrientation() {
		return orientation;
	}

	public void setOrientation(Integer orientation) {
		this.orientation = orientation;
	}

	@Column(name = "IS_BW", columnDefinition = "TINYINT(1)")
	public Boolean getIsBlackAndWhite() {
		return isBlackAndWhite;
	}

	public void setIsBlackAndWhite(Boolean isBlackAndWhite) {
		this.isBlackAndWhite = isBlackAndWhite;
	}

	@JoinColumn(name = "TARGET_ICC_PROFILE_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonSerialize(using = SimpleIccProfileSerializer.class)
	public IccProfile getTargetIccProfile() {
		return targetIccProfile;
	}

	public void setTargetIccProfile(IccProfile targetIccProfile) {
		this.targetIccProfile = targetIccProfile;
	}

	@JoinColumn(name = "CUSTOM_ICC_PROFILE_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	public IccProfile getCustomIccProfile() {
		return customIccProfile;
	}

	public void setCustomIccProfile(IccProfile customIccProfile) {
		this.customIccProfile = customIccProfile;
	}
	
	@Transient
	public String getOriginalImageUrl() {
		return ApplicationConstants.ORIGINAL_IMAGE_PATH + getUrl();
	}
	
	@Transient
	public String getLowResImageUrl() {
		return ApplicationConstants.LOW_RES_IMAGE_PATH + getUrl();
	}
	
	@Transient
	public String getThumbImageUrl() {
		return ApplicationConstants.THUMB_IMAGE_PATH + getUrl();
	}
	
}
