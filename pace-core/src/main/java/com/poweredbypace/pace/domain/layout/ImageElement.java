package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.ImageFile;

@Entity
@DiscriminatorValue("ImageElement")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class ImageElement extends Element {
	
	public static final String SEPIA_FILTER = "sepia";
	public static final String BW_FILTER = "bw";

	private static final long serialVersionUID = 6239106511909208899L;
	
	private Float imageX;
	private Float imageY;
	private Float imageWidth;
	private Float imageHeight;
	private Float imageRotation;
	private Integer quantity;
	private ImageFile imageFile;
	private Boolean flipX;
	private Boolean flipY;
	private String filter;
	private String placeholder;
	
	
	@Column(name="PLACEHOLDER") 
	public String getPlaceholder() {
		return placeholder;
	}
	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	
	@Column(name="FILTER") 
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	@Column(name="FLIP_X", columnDefinition = "TINYINT(1)")
	public Boolean getFlipX() {
		return flipX;
	}
	public void setFlipX(Boolean flipX) {
		this.flipX = flipX;
	}
	
	@Column(name="FLIP_Y", columnDefinition = "TINYINT(1)")
	public Boolean getFlipY() {
		return flipY;
	}
	public void setFlipY(Boolean flipY) {
		this.flipY = flipY;
	}
	@Column(name="IMAGE_X")
	public Float getImageX() {
		return imageX;
	}
	public void setImageX(Float imageX) {
		this.imageX = imageX;
	}
	
	@Column(name="IMAGE_Y")
	public Float getImageY() {
		return imageY;
	}
	public void setImageY(Float imageY) {
		this.imageY = imageY;
	}
	
	@Column(name="IMAGE_WIDTH")
	public Float getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(Float imageWidth) {
		this.imageWidth = imageWidth;
	}
	
	@Column(name="IMAGE_HEIGHT")
	public Float getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(Float imageHeight) {
		this.imageHeight = imageHeight;
	}
	
	@Column(name="IMAGE_ROTATION")
	public Float getImageRotation() {
		return imageRotation;
	}
	public void setImageRotation(Float imageRotation) {
		this.imageRotation = imageRotation;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IMAGE_FILE_ID")
	public ImageFile getImageFile() {
		return imageFile;
	}
	public void setImageFile(ImageFile imageFile) {
		this.imageFile = imageFile;
	}
	
	@Column(name="QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public <E extends Element> void copy(E dst) {
		super.copy(dst);
		if (dst instanceof ImageElement) {
			ImageElement imageElement = (ImageElement)dst;
			imageElement.setImageFile(imageFile);
			imageElement.setImageWidth(imageWidth);
			imageElement.setImageHeight(imageHeight);
			imageElement.setImageX(imageX);
			imageElement.setImageY(imageY);
			imageElement.setImageRotation(imageRotation);
		}
	}

}
