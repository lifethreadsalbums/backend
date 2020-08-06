package com.poweredbypace.pace.domain.layout;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.PrototypeProductOptionValue;

@Entity
@Table(name="APP_LAYOUT_SIZE")
@JsonIgnoreProperties(ignoreUnknown=true)
public class LayoutSize extends BaseEntity {

	public static enum PageOrientation {
		Horizontal,
		Vertical
	}
	
	private static final long serialVersionUID = -5110197909088497427L;
	private String code;
	private Float width;
	private Float height;
	private String displayWidth;
	private String displayHeight;
	private Float bleedTop;
	private Float bleedBottom;
	private Float bleedInside;
	private Float bleedOutside;
	private Float marginTop;
	private Float marginBottom;
	private Float marginInside;
	private Float marginOutside;
	private Float slugTop;
	private Float slugBottom;
	private Float slugInside;
	private Float slugOutside;
	private Float gapBetweenPages;
	private Float spineWidth;
	private Float hingeGap;
	private Float boardWidthBuffer;
	private Float boardHeightBuffer;
	private Float spineBuffer;
	private Float borderWidth;
	private Boolean dynamicSpineWidth;
	private Boolean singlePrint;
	private PageOrientation pageOrientation;
	private List<PrototypeProductOptionValue> prototypeProductOptionValues;
	private CoverType coverType;
	private Integer gridX;
	private Integer gridY;
	private Spread templateSpread;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TEMPLATE_SPREAD_ID")
	public Spread getTemplateSpread() {
		return templateSpread;
	}
	public void setTemplateSpread(Spread templateSpread) {
		this.templateSpread = templateSpread;
	}
	
	
	@Column(name="GRID_X")
	public Integer getGridX() {
		return gridX;
	}
	public void setGridX(Integer gridX) {
		this.gridX = gridX;
	}
	
	@Column(name="GRID_Y")
	public Integer getGridY() {
		return gridY;
	}
	public void setGridY(Integer gridY) {
		this.gridY = gridY;
	}
	
	@Column(name="CODE")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@OneToOne
	@JoinColumn(name="COVER_TYPE")
	public CoverType getCoverType() {
		return coverType;
	}
	public void setCoverType(CoverType coverType) {
		this.coverType = coverType;
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
	
	@Column(name="DISPLAY_WIDTH")
	public String getDisplayWidth() {
		return displayWidth;
	}
	public void setDisplayWidth(String displayWidth) {
		this.displayWidth = displayWidth;
	}
	
	@Column(name="DISPLAY_HEIGHT")
	public String getDisplayHeight() {
		return displayHeight;
	}
	public void setDisplayHeight(String displayHeight) {
		this.displayHeight = displayHeight;
	}
	
	@Column(name="BLEED_TOP")
	public Float getBleedTop() {
		return bleedTop;
	}
	public void setBleedTop(Float bleedTop) {
		this.bleedTop = bleedTop;
	}
	
	@Column(name="BLEED_BOTTOM")
	public Float getBleedBottom() {
		return bleedBottom;
	}
	public void setBleedBottom(Float bleedBottom) {
		this.bleedBottom = bleedBottom;
	}
	
	@Column(name="BLEED_INSIDE")
	public Float getBleedInside() {
		return bleedInside;
	}
	public void setBleedInside(Float bleedInside) {
		this.bleedInside = bleedInside;
	}
	
	@Column(name="BLEED_OUTSIDE")
	public Float getBleedOutside() {
		return bleedOutside;
	}
	public void setBleedOutside(Float bleedOutside) {
		this.bleedOutside = bleedOutside;
	}
	
	@Column(name="MARGIN_TOP")
	public Float getMarginTop() {
		return marginTop;
	}
	public void setMarginTop(Float marginTop) {
		this.marginTop = marginTop;
	}
	
	@Column(name="MARGIN_BOTTOM")
	public Float getMarginBottom() {
		return marginBottom;
	}
	public void setMarginBottom(Float marginBottom) {
		this.marginBottom = marginBottom;
	}
	
	@Column(name="MARGIN_INSIDE")
	public Float getMarginInside() {
		return marginInside;
	}
	public void setMarginInside(Float marginInside) {
		this.marginInside = marginInside;
	}
	
	@Column(name="MARGIN_OUTSIDE")
	public Float getMarginOutside() {
		return marginOutside;
	}
	public void setMarginOutside(Float marginOutside) {
		this.marginOutside = marginOutside;
	}
	
	@Column(name="SLUG_TOP")
	public Float getSlugTop() {
		return slugTop;
	}
	public void setSlugTop(Float slugTop) {
		this.slugTop = slugTop;
	}
	
	@Column(name="SLUG_BOTTOM")
	public Float getSlugBottom() {
		return slugBottom;
	}
	public void setSlugBottom(Float slugBottom) {
		this.slugBottom = slugBottom;
	}
	
	@Column(name="SLUG_INSIDE")
	public Float getSlugInside() {
		return slugInside;
	}
	public void setSlugInside(Float slugInside) {
		this.slugInside = slugInside;
	}
	
	@Column(name="SLUG_OUTSIDE")
	public Float getSlugOutside() {
		return slugOutside;
	}
	public void setSlugOutside(Float slugOutside) {
		this.slugOutside = slugOutside;
	}
	
	@Column(name="BORDER_WIDTH")
	public Float getBorderWidth() {
		return borderWidth;
	}
	public void setBorderWidth(Float borderWidth) {
		this.borderWidth = borderWidth;
	}
	
	@Column(name="PAGE_ORIENTATION")
	@Enumerated(EnumType.STRING)
	public PageOrientation getPageOrientation() {
		return pageOrientation;
	}
	public void setPageOrientation(PageOrientation pageOrientation) {
		this.pageOrientation = pageOrientation;
	}
	
	@Column(name="GAP_BETWEEN_PAGES")
	public Float getGapBetweenPages() {
		return gapBetweenPages;
	}
	public void setGapBetweenPages(Float gapBetweenPages) {
		this.gapBetweenPages = gapBetweenPages;
	}
	
	@Transient
	public Float getHingeGap() {
		return hingeGap!=null ? hingeGap : 0f;
	}
	public void setHingeGap(Float hingeGap) {
		this.hingeGap = hingeGap;
	}
	
	@Transient
	public Float getSpineWidth() {
		return spineWidth!=null ? spineWidth : 0f;
	}
	
	public void setSpineWidth(Float spineWidth) {
		this.spineWidth = spineWidth;
	}
	
	@Column(name="BOARD_WIDTH_BUFFER")
	public Float getBoardWidthBuffer() {
		return boardWidthBuffer;
	}
	public void setBoardWidthBuffer(Float boardWidthBuffer) {
		this.boardWidthBuffer = boardWidthBuffer;
	}
	
	@Column(name="BOARD_HEIGHT_BUFFER")
	public Float getBoardHeightBuffer() {
		return boardHeightBuffer;
	}
	public void setBoardHeightBuffer(Float boardHeightBuffer) {
		this.boardHeightBuffer = boardHeightBuffer;
	}
	
	@Column(name="SPINE_BUFFER")
	public Float getSpineBuffer() {
		return spineBuffer;
	}
	public void setSpineBuffer(Float spineBuffer) {
		this.spineBuffer = spineBuffer;
	}
	
	@Column(name="DYNAMIC_SPINE_WIDTH", columnDefinition = "TINYINT(1)")
	public Boolean getDynamicSpineWidth() {
		return dynamicSpineWidth;
	}
	public void setDynamicSpineWidth(Boolean dynamicSpineWidth) {
		this.dynamicSpineWidth = dynamicSpineWidth;
	}
	
	@Column(name="SINGLE_PRINT", columnDefinition = "TINYINT(1)")
	public Boolean getSinglePrint() {
		return singlePrint;
	}
	public void setSinglePrint(Boolean singlePrint) {
		this.singlePrint = singlePrint;
	}
	
	@JsonIgnore
	@OneToMany(mappedBy="layoutSize", fetch=FetchType.LAZY)
	public List<PrototypeProductOptionValue> getPrototypeProductOptionValues() {
		return prototypeProductOptionValues;
	}
	public void setPrototypeProductOptionValues(
			List<PrototypeProductOptionValue> prototypeProductOptionValues) {
		this.prototypeProductOptionValues = prototypeProductOptionValues;
	}
	
	@Transient
	public void setBleed(float bleed) {
		this.bleedBottom = bleed;
		this.bleedInside = bleed;
		this.bleedOutside = bleed;
		this.bleedTop = bleed;
	}
	
	@Transient
	public void setSlug(float slug) {
		this.slugBottom = slug;
		this.slugInside = slug;
		this.slugOutside = slug;
		this.slugTop = slug;
	}
	
	public LayoutSize() { }
	
	public LayoutSize(LayoutSize ls) {
		this.code = ls.getCode();
		this.width = ls.getWidth();
		this.height = ls.getHeight();
		this.displayWidth = ls.getDisplayWidth();
		this.displayHeight = ls.getDisplayHeight();
		this.bleedTop = ls.getBleedTop();
		this.bleedBottom = ls.getBleedBottom();
		this.bleedInside = ls.getBleedInside();
		this.bleedOutside = ls.getBleedOutside();
		this.marginTop = ls.getMarginTop();
		this.marginBottom = ls.getMarginBottom();
		this.marginInside = ls.getMarginInside();
		this.marginOutside = ls.getMarginOutside();
		this.slugTop = ls.getSlugTop();
		this.slugBottom = ls.getSlugBottom();
		this.slugInside = ls.getSlugInside();
		this.slugOutside = ls.getSlugOutside();
		this.gapBetweenPages = ls.getGapBetweenPages();
		this.pageOrientation = ls.getPageOrientation();
		this.coverType = ls.getCoverType();
		this.spineWidth = ls.getSpineWidth();
		this.hingeGap = ls.getHingeGap();
		this.boardWidthBuffer = ls.getBoardWidthBuffer();
		this.boardHeightBuffer = ls.getBoardHeightBuffer();
		this.spineBuffer = ls.getSpineBuffer();
		this.dynamicSpineWidth = ls.getDynamicSpineWidth();
	}
	
}
