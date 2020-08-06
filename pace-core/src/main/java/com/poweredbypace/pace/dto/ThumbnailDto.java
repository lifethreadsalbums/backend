package com.poweredbypace.pace.dto;

import com.poweredbypace.pace.domain.ImageFile;

public class ThumbnailDto {
	private Long id;
	private ImageFile imageFile;
	private String thumbnailAsBase64;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getThumbnailAsBase64() {
		return thumbnailAsBase64;
	}
	public void setThumbnailAsBase64(String thumbnailAsBase64) {
		this.thumbnailAsBase64 = thumbnailAsBase64;
	}
	public ImageFile getImageFile() {
		return imageFile;
	}
	public void setImageFile(ImageFile imageFile) {
		this.imageFile = imageFile;
	}
	
}
