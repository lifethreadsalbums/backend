package com.poweredbypace.pace.domain.layout;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@DiscriminatorValue("CameoSetElement")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class CameoSetElement extends Element {

	private static final long serialVersionUID = -8536964805365467569L;
	
	private List<CameoElement> shapes = new ArrayList<CameoElement>();
	
	private String positionCode;
	
	@Column(name="POSITION_CODE")
	public String getPositionCode() {
		return positionCode;
	}

	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}

	@ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinTable(name = "P_CAMEO_SET_ELEMENT", 
		joinColumns = { @JoinColumn(name = "CAMEO_SET_ID", nullable = false, updatable = false) }, 
		inverseJoinColumns = { @JoinColumn(name = "CAMEO_ELEMENT_ID", nullable = false, updatable = false) }
	)
	public List<CameoElement> getShapes() {
		return shapes;
	}

	public void setShapes(List<CameoElement> shapes) {
		this.shapes = shapes;
	}
	
	@Override
	public <E extends Element> void copy(E dst) {
		super.copy(dst);
		if (dst instanceof CameoSetElement) {
			CameoSetElement val = (CameoSetElement)dst;
			val.setPositionCode(positionCode);
			val.setShapes(shapes);
		}
	}

		
}
