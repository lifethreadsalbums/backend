package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name="P_ELEMENT")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@JsonIgnoreProperties(ignoreUnknown=true) 
@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "type"
)  
@JsonSubTypes({
	@Type(value = ImageStampElement.class, name = "ImageStampElement"),
	@Type(value = TextStampElement.class, name = "TextStampElement"),  
    @Type(value = TextElement.class, name = "TextElement"),  
    @Type(value = SpineTextElement.class, name = "SpineTextElement"),  
    @Type(value = MaterialElement.class, name = "MaterialElement"),
    @Type(value = ImageElement.class, name = "ImageElement"),
    @Type(value = ElementGroup.class, name = "ElementGroup"),
    @Type(value = BackgroundFrameElement.class, name = "BackgroundFrameElement"),
    @Type(value = CameoElement.class, name = "CameoElement"),
    @Type(value = CameoSetElement.class, name = "CameoSetElement"),
}) 
public class Element extends BaseEntity {

	private static final long serialVersionUID = -467886731249730264L;
	
	private Float x;
	private Float y;
	private Float width;
	private Float height;
	private Float rotation;
	private Float opacity;
	private Float strokeWidth;
	private String strokeColor;
	private String backgroundColor;
	private Spread spread;
	private ElementGroup group;
	private String internalId;
	private Boolean locked;
	private Integer zorder;
	
	@Column(name="X")
	public Float getX() {
		return x;
	}
	public void setX(Float x) {
		this.x = x;
	}
	
	@Column(name="Y")
	public Float getY() {
		return y;
	}
	public void setY(Float y) {
		this.y = y;
	}
	
	@Column(name="WIDTH")
	public Float getWidth() {
		return width;
	}
	public void setWidth(Float width) {
		this.width = width;
	}
	
	@Column(name="HEIGHT")
	public Float getHeight() {
		return height;
	}
	public void setHeight(Float height) {
		this.height = height;
	}
	
	@Column(name="ROTATION")
	public Float getRotation() {
		return rotation;
	}
	public void setRotation(Float rotation) {
		this.rotation = rotation;
	}
	
	@Column(name="OPACITY")
	public Float getOpacity() {
		return opacity;
	}
	public void setOpacity(Float opacity) {
		this.opacity = opacity;
	}
	
	@Transient
	public Float getStrokeOpacity() {
		return 1.0f;
	}
	
	@Column(name="STROKE_WIDTH")
	public Float getStrokeWidth() {
		return strokeWidth;
	}
	public void setStrokeWidth(Float strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	
	@Column(name="STROKE_COLOR")
	public String getStrokeColor() {
		return strokeColor;
	}
	public void setStrokeColor(String strokeColor) {
		this.strokeColor = strokeColor;
	}
	
	@Column(name="BACKGROUND_COLOR")
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	@Column(name="LOCKED", columnDefinition = "TINYINT(1)")
	public Boolean getLocked() {
		return locked;
	}
	public void setLocked(Boolean locked) {
		this.locked = locked;
	}
	
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="SPREAD_ID")
	public Spread getSpread() {
		return spread;
	}
	public void setSpread(Spread spread) {
		this.spread = spread;
	}
	
	@JsonBackReference("groupElements")
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="ELEMENT_GROUP_ID")
	public ElementGroup getGroup() {
		return group;
	}
	public void setGroup(ElementGroup group) {
		this.group = group;
	}
	
	@Column(name="INTERNAL_ID")
	@JsonProperty("_id")
	public String getInternalId() {
		return internalId;
	}
	public void setInternalId(String internalId) {
		this.internalId = internalId;
	}
	
	@Column(name="Z_ORDER")
	public Integer getZorder() {
		return zorder;
	}
	public void setZorder(Integer zorder) {
		this.zorder = zorder;
	}
	
	public <E extends Element> void copy(E dst) {
		dst.setX(x);
		dst.setY(y);
		dst.setWidth(width);
		dst.setHeight(height);
		dst.setRotation(rotation);
		dst.setGroup(group);
		dst.setOpacity(opacity);
		dst.setStrokeColor(strokeColor);
		dst.setStrokeWidth(strokeWidth);
		dst.setBackgroundColor(backgroundColor);
		dst.setZorder(zorder);
		dst.setLocked(locked);
	}
	
}
