package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.ImageFile;

@Entity
@DiscriminatorValue("ImageStampElement")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class ImageStampElement extends ImageElement {

	private static final long serialVersionUID = -6148653631483055194L;

	private Boolean enabled;
	private Boolean firstUse;
	private Float cropWidth;
	private Float cropHeight;
	private Boolean metalPlaque;
	private String positionCode;
	private String foilCode;
	
	@Column(name="FOIL_CODE")
	public String getFoilCode() {
		return foilCode;
	}

	public void setFoilCode(String foilCode) {
		this.foilCode = foilCode;
	}
	
	@Column(name="POSITION_CODE")
	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	
	
	@Column(name = "ENABLED", columnDefinition = "TINYINT(1)")
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	@Column(name = "FIRST_USE", columnDefinition = "TINYINT(1)")
	public Boolean getFirstUse() {
		return firstUse;
	}

	public void setFirstUse(Boolean firstUse) {
		this.firstUse = firstUse;
	}
	
	@Column(name="CROP_WIDTH")
	public Float getCropWidth() {
		return cropWidth;
	}

	public void setCropWidth(Float cropWidth) {
		this.cropWidth = cropWidth;
	}
	
	@Column(name="CROP_HEIGHT")
	public Float getCropHeight() {
		return cropHeight;
	}

	public void setCropHeight(Float cropHeight) {
		this.cropHeight = cropHeight;
	}
	
	@Column(name="METAL_PLAQUE")
	public Boolean getMetalPlaque() {
		return metalPlaque;
	}

	public void setMetalPlaque(Boolean metalPlaque) {
		this.metalPlaque = metalPlaque;
	}
	
	@Transient
	public String getStampUrl() {
		ImageFile imageFile = getImageFile();
		return imageFile!=null ? imageFile.getUrl() : null;
	}
	
	@Override
	public <E extends Element> void copy(E dst) {
		super.copy(dst);
		if (dst instanceof ImageStampElement) {
			ImageStampElement element = (ImageStampElement)dst;
			element.setPositionCode(positionCode);
			element.setFoilCode(foilCode);
			element.setEnabled(enabled);
			element.setFirstUse(firstUse);
			element.setCropWidth(cropWidth);
			element.setCropHeight(cropHeight);
			element.setMetalPlaque(metalPlaque);
		}
	}
	
}
