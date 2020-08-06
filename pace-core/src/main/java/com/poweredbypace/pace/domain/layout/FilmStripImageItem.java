package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.poweredbypace.pace.domain.ImageFile;

@Entity
@DiscriminatorValue("image")
public class FilmStripImageItem extends FilmStripItem {

	private static final long serialVersionUID = 5161249699362071771L;

	private ImageFile image;
	private Boolean inCoverZone;
	private Boolean isDoubleSpread;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="IMAGE_FILE_ID")
	public ImageFile getImage() {
		return image;
	}

	public void setImage(ImageFile image) {
		this.image = image;
	}

	@Column(name = "IN_COVER_ZONE", columnDefinition = "TINYINT(1)")
	public Boolean getInCoverZone() {
		return inCoverZone;
	}

	public void setInCoverZone(Boolean inCoverZone) {
		this.inCoverZone = inCoverZone;
	}

	@Column(name = "IS_DOUBLE_SPREAD", columnDefinition = "TINYINT(1)")
	public Boolean getIsDoubleSpread() {
		return isDoubleSpread;
	}

	public void setIsDoubleSpread(Boolean isDoubleSpread) {
		this.isDoubleSpread = isDoubleSpread;
	}
	
}
