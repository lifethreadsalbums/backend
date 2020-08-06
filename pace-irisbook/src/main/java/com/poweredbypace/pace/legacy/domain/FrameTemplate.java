package com.poweredbypace.pace.legacy.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "frame_template")
@SuppressWarnings("serial")
public class FrameTemplate implements Serializable {

	private Long frameTemplateId;
	private String username;
	private Boolean isPublic;
	private Boolean isDefault;
	private String pattern;
	private String shapeCode;
	private Double width;
	private Double height;
	private Double fixedSpacing;
	private String align;
	private Boolean isSpreadTemplate;
	private String pageOrientation;
	
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "frame_template_id")
	public Long getFrameTemplateId() {
		return frameTemplateId;
	}
	public void setFrameTemplateId(Long frameTemplateId) {
		this.frameTemplateId = frameTemplateId;
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
	@Column(name = "is_public")
	public Boolean getIsPublic() {
		return isPublic;
	}
	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	
	@Basic
	@Column(name = "is_default")
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	@Basic
	@Column(name = "pattern")
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	
	@Basic
	@Column(name = "shape_code")
	public String getShapeCode() {
		return shapeCode;
	}
	public void setShapeCode(String shapeCode) {
		this.shapeCode = shapeCode;
	}
	
	@Basic
	@Column(name = "width")
	public Double getWidth() {
		return width;
	}
	public void setWidth(Double width) {
		this.width = width;
	}
	
	@Basic
	@Column(name = "height")
	public Double getHeight() {
		return height;
	}
	public void setHeight(Double height) {
		this.height = height;
	}
	
	@Basic
	@Column(name = "fixed_spacing")
	public Double getFixedSpacing() {
		return fixedSpacing;
	}
	public void setFixedSpacing(Double fixedSpacing) {
		this.fixedSpacing = fixedSpacing;
	}
	
	@Basic
	@Column(name = "align")
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	
	@Basic
	@Column(name = "is_spread_template")
	public Boolean getIsSpreadTemplate() {
		return isSpreadTemplate;
	}
	public void setIsSpreadTemplate(Boolean isSpreadTemplate) {
		this.isSpreadTemplate = isSpreadTemplate;
	}
	
	@Basic
	@Column(name = "page_orientation")
	public String getPageOrientation() {
		return pageOrientation;
	}
	public void setPageOrientation(String pageOrientation) {
		this.pageOrientation = pageOrientation;
	}
	
	
	
}
