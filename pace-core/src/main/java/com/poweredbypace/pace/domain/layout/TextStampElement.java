package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("TextStampElement")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class TextStampElement extends TextElement {

	private static final long serialVersionUID = 8312007503917326154L;

	//private ElementPosition elementPosition;
	//private Foil foil;
	private Boolean metalPlaque;
	private String positionCode;
	private String foilCode;
	private Boolean autoSize;
	
	@Column(name="POSITION_CODE")
	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	
	

//	@ManyToOne
//	@JoinColumn(name = "ELEMENT_POSITION_ID", nullable=true)
//	public ElementPosition getElementPosition() {
//		return elementPosition;
//	}
//
//	public void setElementPosition(ElementPosition elementPosition) {
//		this.elementPosition = elementPosition;
//	}
//	
//	@ManyToOne
//	@JoinColumn(name = "FOIL_ID", nullable=true)
//	public Foil getFoil() {
//		return foil;
//	}
//
//	public void setFoil(Foil foil) {
//		this.foil = foil;
//	}
	
	@Column(name="FOIL_CODE")
	public String getFoilCode() {
		return foilCode;
	}

	public void setFoilCode(String foilCode) {
		this.foilCode = foilCode;
	}

	@Column(name="METAL_PLAQUE")
	public Boolean getMetalPlaque() {
		return metalPlaque;
	}

	public void setMetalPlaque(Boolean metalPlaque) {
		this.metalPlaque = metalPlaque;
	}
	
	@Column(name="AUTO_SIZE")
	public Boolean getAutoSize() {
		return autoSize;
	}

	public void setAutoSize(Boolean autoSize) {
		this.autoSize = autoSize;
	}

	@Override
	public <E extends Element> void copy(E dst) {
		super.copy(dst);
		if (dst instanceof TextStampElement) {
			TextStampElement val = (TextStampElement)dst;
			val.setPositionCode(positionCode);
			val.setMetalPlaque(metalPlaque);
			val.setFoilCode(foilCode);
			val.setAutoSize(autoSize);
		}
	}
	
}
