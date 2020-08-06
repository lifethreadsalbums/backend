package com.poweredbypace.pace.legacy.domain;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.itextpdf.text.Rectangle;


@Entity
@Table(name = "placed_element")
@SuppressWarnings("serial")
public class PlacedElement implements Serializable {

	private Long placedElementId;
	private Image image;	
	private Page page;	
	private Double imageBoxX;
	private Double imageBoxY;
	private Double imageBoxWidth;
	private Double imageBoxHeight;
	private Double pictureBoxX;
	private Double pictureBoxY;
	private Double pictureBoxWidth;
	private Double pictureBoxHeight;
	private Integer indexZ;
	private String text;
	private Double opacity;
	private Double stroke;
	private Double rotation;
	private Double imageRotation;
	private String group;
	private Long fillColor;
	private Boolean isLocked;
	private String backgroundFrameType;
	private Double scale;
	private Boolean maintainAspectRatio;
	private Long strokeColor;
	private Double strokeOpacity;


	@Basic
	@Id
	@GeneratedValue
	@Column(name = "placed_element_id")
	public Long getPlacedElementId() {
		return placedElementId;
	}

	public void setPlacedElementId(Long value) {
		if (value!=null && value==0)
			value = null;
		this.placedElementId = value;
	}

	@ManyToOne
	@JoinColumn(name = "image_id")
	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	@ManyToOne
	@JoinColumn(name = "page_id")
	@JsonIgnore
	public Page getPage() {
		return this.page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@Basic
	@Column(name = "image_box_x")
	public Double getImageBoxX() {
		return imageBoxX;
	}

	public void setImageBoxX(Double imageBoxX) {
		this.imageBoxX = imageBoxX;
	}

	@Basic
	@Column(name = "image_box_y")
	public Double getImageBoxY() {
		return imageBoxY;
	}

	public void setImageBoxY(Double imageBoxY) {
		this.imageBoxY = imageBoxY;
	}

	@Basic
	@Column(name = "image_box_width")
	public Double getImageBoxWidth() {
		return imageBoxWidth;
	}

	public void setImageBoxWidth(Double imageBoxWidth) {
		this.imageBoxWidth = imageBoxWidth;
	}

	@Basic
	@Column(name = "image_box_height")
	public Double getImageBoxHeight() {
		return imageBoxHeight;
	}

	public void setImageBoxHeight(Double imageBoxHeight) {
		this.imageBoxHeight = imageBoxHeight;
	}

	@Basic
	@Column(name = "picture_box_x")
	public Double getPictureBoxX() {
		return pictureBoxX;
	}

	public void setPictureBoxX(Double pictureBoxX) {
		this.pictureBoxX = pictureBoxX;
	}

	@Basic
	@Column(name = "picture_box_y")
	public Double getPictureBoxY() {
		return pictureBoxY;
	}

	public void setPictureBoxY(Double pictureBoxY) {
		this.pictureBoxY = pictureBoxY;
	}

	@Basic
	@Column(name = "picture_box_width")
	public Double getPictureBoxWidth() {
		return pictureBoxWidth;
	}

	public void setPictureBoxWidth(Double pictureBoxWidth) {
		this.pictureBoxWidth = pictureBoxWidth;
	}

	@Basic
	@Column(name = "picture_box_height")
	public Double getPictureBoxHeight() {
		return pictureBoxHeight;
	}

	public void setPictureBoxHeight(Double pictureBoxHeight) {
		this.pictureBoxHeight = pictureBoxHeight;
	}

	@Basic
	@Column(name = "index_z")
	public Integer getIndexZ() {
		return indexZ;
	}

	public void setIndexZ(Integer indexZ) {
		this.indexZ = indexZ;
	}

	
	@Basic
	@Column(name = "text")
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
	@Basic
	@Column(name = "opacity")
	public Double getOpacity() {
		return opacity;
	}

	public void setOpacity(Double opacity) {
		this.opacity = opacity;
	}

	@Basic
	@Column(name = "stroke")
	public Double getStroke() {
		return stroke;
	}

	public void setStroke(Double stroke) {
		this.stroke = stroke;
	}

	@Basic
	@Column(name = "rotation")
	public Double getRotation() {
		return rotation;
	}

	public void setRotation(Double rotation) {
		this.rotation = rotation;
	}

	@Basic
	@Column(name = "image_rotation")
	public Double getImageRotation() {
		return imageRotation;
	}

	public void setImageRotation(Double imageRotation) {
		this.imageRotation = imageRotation;
	}

	@Basic
	@Column(name = "`group`")
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	@Basic
	@Column(name = "fill_color")
	public Long getFillColor() {
		return fillColor;
	}

	public void setFillColor(Long fillColor) {
		this.fillColor = fillColor;
	}

	@Basic
	@Column(name = "is_locked")
	public Boolean getIsLocked() {
		return isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	@Basic
	@Column(name = "background_frame_type")
	public String getBackgroundFrameType() {
		return backgroundFrameType;
	}

	public void setBackgroundFrameType(String backgroundFrameType) {
		this.backgroundFrameType = backgroundFrameType;
	}

	@Transient
	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}

	@Basic
	@Column(name = "maintain_aspect_ratio")
	public Boolean getMaintainAspectRatio() {
		return maintainAspectRatio;
	}

	public void setMaintainAspectRatio(Boolean maintainAspectRatio) {
		this.maintainAspectRatio = maintainAspectRatio;
	}
	
	@Basic
	@Column(name = "stroke_color")
	public Long getStrokeColor() {
		return strokeColor;
	}

	public void setStrokeColor(Long strokeColor) {
		this.strokeColor = strokeColor;
	}
	
	@Basic
	@Column(name = "stroke_opacity")
	public Double getStrokeOpacity() {
		return strokeOpacity;
	}

	public void setStrokeOpacity(Double strokeOpacity) {
		this.strokeOpacity = strokeOpacity;
	}

	@Transient
	public Rectangle getBounds()
	{
		float x = this.pictureBoxX.floatValue();
		float y = this.pictureBoxY.floatValue();
		float width = this.pictureBoxWidth.floatValue();
		float height = this.pictureBoxHeight.floatValue();
		
		double angleRad = Math.toRadians(rotation.doubleValue());
		AffineTransform af = new AffineTransform();
		af.translate(x, y);
		af.rotate(angleRad);
		
		Point2D p1 = af.transform(new Point2D.Double(0, 0), null);
		Point2D p2 = af.transform(new Point2D.Double(width, 0), null);
		Point2D p3 = af.transform(new Point2D.Double(width, height), null);
		Point2D p4 = af.transform(new Point2D.Double(0, height), null);

		double left = Math.min(Math.min(p1.getX(), p2.getX()), Math.min(p3.getX(), p4.getX()));
		double top = Math.min(Math.min(p1.getY(), p2.getY()), Math.min(p3.getY(), p4.getY()));
		double right = Math.max(Math.max(p1.getX(), p2.getX()), Math.max(p3.getX() ,p4.getX()));
		double bottom = Math.max(Math.max(p1.getY(), p2.getY()), Math.max(p3.getY(), p4.getY()));
		
		Rectangle bounds = new Rectangle((float)left, (float)top, (float)right, (float)bottom);
		bounds.normalize();
		
		return bounds;
	}
	
	public PlacedElement()
	{
		super();
	}

	public PlacedElement(PlacedElement el, Page page)
	{
		super();
		this.image = el.image;
		this.page = page;	
		this.imageBoxX = el.imageBoxX;
		this.imageBoxY = el.imageBoxY;
		this.imageBoxWidth = el.imageBoxWidth;
		this.imageBoxHeight = el.imageBoxHeight;
		this.pictureBoxX = el.pictureBoxX;
		this.pictureBoxY = el.pictureBoxY;
		this.pictureBoxWidth = el.pictureBoxWidth;
		this.pictureBoxHeight = el.pictureBoxHeight;
		this.indexZ = el.indexZ;
		this.text = el.text;
		this.opacity = el.opacity;
		this.stroke = el.stroke;
		this.rotation = el.rotation;
		this.imageRotation = el.imageRotation;
		this.group = el.group;
		this.fillColor = el.fillColor;
		this.isLocked = el.isLocked;
		this.backgroundFrameType = el.backgroundFrameType;
		this.scale = el.scale;
		this.maintainAspectRatio = el.maintainAspectRatio;
		this.strokeColor = el.strokeColor;
		this.strokeOpacity = el.strokeOpacity;
	}
	
}