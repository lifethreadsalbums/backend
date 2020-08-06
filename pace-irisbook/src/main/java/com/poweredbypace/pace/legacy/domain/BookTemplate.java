package com.poweredbypace.pace.legacy.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "book_template")
@SuppressWarnings("serial")
public class BookTemplate implements Serializable, Cloneable {
	
	public static class CoverType {
		public static final String QBIC = "QBIC";
		public static final String FIC = "FIC";	
		public static final String TQIC = "TQIC";
		public static final String PHOTO_STRIP = "PHOTO_STRIP";
	}

	/**
	 * Attribute bookTemplateId.
	 */
	private Long bookTemplateId;

	/**
	 * Attribute width.
	 */
	private Double width;

	/**
	 * Attribute height.
	 */
	private Double height;

	/**
	 * Attribute bleedTop.
	 */
	private Double bleedTop;

	/**
	 * Attribute bleedBottom.
	 */
	private Double bleedBottom;

	/**
	 * Attribute bleedInside.
	 */
	private Double bleedInside;

	/**
	 * Attribute bleedOutside.
	 */
	private Double bleedOutside;

	/**
	 * Attribute marginTop.
	 */
	private Double marginTop;

	/**
	 * Attribute marginBottom.
	 */
	private Double marginBottom;

	/**
	 * Attribute marginInside.
	 */
	private Double marginInside;

	/**
	 * Attribute marginOutside.
	 */
	private Double marginOutside;

	/**
	 * Attribute spineWidth.
	 */
	private Double spineWidth;

	private String name;
	
	private Boolean isProofBookTemplate;

	private Integer paperType;
	
	private Double gapBetweenPages;
	
	private String coverType;
	
	private String shape;
	
	private String grain;
	
	private Double slugTop;

	private Double slugBottom;

	private Double slugInside;

	private Double slugOutside;
	
	private String grainLf;
	
	private Integer imposition;
	
	private Integer impositionLf;
	
	private Double hingeGap;
	
	private String pageOrientation;
	
	private Integer jpegSpreadRotation;
	
	private String masterShape;
	
	/**
	 * <p> 
	 * </p>
	 * @return bookTemplateId
	 */
	@Basic
	@Id
	@GeneratedValue
	@Column(name = "book_template_id")
	public Long getBookTemplateId() {
		return bookTemplateId;
	}

	/**
	 * @param bookTemplateId new value for bookTemplateId 
	 */
	public void setBookTemplateId(Long bookTemplateId) {
		if (bookTemplateId!=null && bookTemplateId==0)
			bookTemplateId = null;
		this.bookTemplateId = bookTemplateId;
	}

	/**
	 * <p> 
	 * </p>
	 * @return width
	 */
	@Basic
	@Column(name = "width")
	public Double getWidth() {
		return width;
	}

	/**
	 * @param width new value for width 
	 */
	public void setWidth(Double width) {
		this.width = width;
	}

	/**
	 * <p> 
	 * </p>
	 * @return height
	 */
	@Basic
	@Column(name = "height")
	public Double getHeight() {
		return height;
	}

	/**
	 * @param height new value for height 
	 */
	public void setHeight(Double height) {
		this.height = height;
	}

	/**
	 * <p> 
	 * </p>
	 * @return bleedTop
	 */
	@Basic
	@Column(name = "bleed_top")
	public Double getBleedTop() {
		return bleedTop;
	}

	/**
	 * @param bleedTop new value for bleedTop 
	 */
	public void setBleedTop(Double bleedTop) {
		this.bleedTop = bleedTop;
	}

	/**
	 * <p> 
	 * </p>
	 * @return bleedBottom
	 */
	@Basic
	@Column(name = "bleed_bottom")
	public Double getBleedBottom() {
		return bleedBottom;
	}

	/**
	 * @param bleedBottom new value for bleedBottom 
	 */
	public void setBleedBottom(Double bleedBottom) {
		this.bleedBottom = bleedBottom;
	}

	/**
	 * <p> 
	 * </p>
	 * @return bleedInside
	 */
	@Basic
	@Column(name = "bleed_inside")
	public Double getBleedInside() {
		return bleedInside;
	}

	/**
	 * @param bleedInside new value for bleedInside 
	 */
	public void setBleedInside(Double bleedInside) {
		this.bleedInside = bleedInside;
	}

	/**
	 * <p> 
	 * </p>
	 * @return bleedOutside
	 */
	@Basic
	@Column(name = "bleed_outside")
	public Double getBleedOutside() {
		return bleedOutside;
	}

	/**
	 * @param bleedOutside new value for bleedOutside 
	 */
	public void setBleedOutside(Double bleedOutside) {
		this.bleedOutside = bleedOutside;
	}

	/**
	 * <p> 
	 * </p>
	 * @return marginTop
	 */
	@Basic
	@Column(name = "margin_top")
	public Double getMarginTop() {
		return marginTop;
	}

	/**
	 * @param marginTop new value for marginTop 
	 */
	public void setMarginTop(Double marginTop) {
		this.marginTop = marginTop;
	}

	/**
	 * <p> 
	 * </p>
	 * @return marginBottom
	 */
	@Basic
	@Column(name = "margin_bottom")
	public Double getMarginBottom() {
		return marginBottom;
	}

	/**
	 * @param marginBottom new value for marginBottom 
	 */
	public void setMarginBottom(Double marginBottom) {
		this.marginBottom = marginBottom;
	}

	/**
	 * <p> 
	 * </p>
	 * @return marginInside
	 */
	@Basic
	@Column(name = "margin_inside")
	public Double getMarginInside() {
		return marginInside;
	}

	/**
	 * @param marginInside new value for marginInside 
	 */
	public void setMarginInside(Double marginInside) {
		this.marginInside = marginInside;
	}

	/**
	 * <p> 
	 * </p>
	 * @return marginOutside
	 */
	@Basic
	@Column(name = "margin_outside")
	public Double getMarginOutside() {
		return marginOutside;
	}

	/**
	 * @param marginOutside new value for marginOutside 
	 */
	public void setMarginOutside(Double marginOutside) {
		this.marginOutside = marginOutside;
	}

	/**
	 * <p> 
	 * </p>
	 * @return spineWidth
	 */
	@Basic
	@Column(name = "spine_width")
	public Double getSpineWidth() {
		return spineWidth;
	}

	/**
	 * @param spineWidth new value for spineWidth 
	 */
	public void setSpineWidth(Double spineWidth) {
		this.spineWidth = spineWidth;
	}

	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Basic
	@Column(name = "is_proof_book_template")
	public Boolean getIsProofBookTemplate() {
		return isProofBookTemplate;
	}

	public void setIsProofBookTemplate(Boolean isProofBookTemplate) {
		this.isProofBookTemplate = isProofBookTemplate;
	}
	
	
	@Basic
	@Column(name = "paper_type")
	public Integer getPaperType() {
		return paperType;
	}

	public void setPaperType(Integer paperType) {
		this.paperType = paperType;
	}
	
	
	@Basic
	@Column(name = "gap_between_pages")
	public Double getGapBetweenPages() {
		return gapBetweenPages;
	}

	public void setGapBetweenPages(Double gapBetweenPages) {
		this.gapBetweenPages = gapBetweenPages;
	}
	
	
	@Basic
	@Column(name = "cover_type")
	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}

	@Basic
	@Column(name = "shape")
	public String getShape() {
		return shape;
	}

	public void setShape(String shape) {
		this.shape = shape;
	}
	
	
	@Basic
	@Column(name = "grain")
	public String getGrain() {
		return grain;
	}

	public void setGrain(String grain) {
		this.grain = grain;
	}
	
	
	@Basic
	@Column(name = "slug_top")
	public Double getSlugTop() {
		return slugTop;
	}

	public void setSlugTop(Double slugTop) {
		this.slugTop = slugTop;
	}

	@Basic
	@Column(name = "slug_bottom")
	public Double getSlugBottom() {
		return slugBottom;
	}

	public void setSlugBottom(Double slugBottom) {
		this.slugBottom = slugBottom;
	}

	@Basic
	@Column(name = "slug_inside")
	public Double getSlugInside() {
		return slugInside;
	}

	public void setSlugInside(Double slugInside) {
		this.slugInside = slugInside;
	}

	@Basic
	@Column(name = "slug_outside")
	public Double getSlugOutside() {
		return slugOutside;
	}

	public void setSlugOutside(Double slugOutside) {
		this.slugOutside = slugOutside;
	}
	
	@Basic
	@Column(name = "grain_lf")
	public String getGrainLf() {
		return grainLf;
	}

	public void setGrainLf(String grainLf) {
		this.grainLf = grainLf;
	}

	@Basic
	@Column(name = "imposition")
	public Integer getImposition() {
		return imposition;
	}

	public void setImposition(Integer imposition) {
		this.imposition = imposition;
	}

	@Basic
	@Column(name = "imposition_lf")
	public Integer getImpositionLf() {
		return impositionLf;
	}

	public void setImpositionLf(Integer impositionLf) {
		this.impositionLf = impositionLf;
	}
	
	
	@Basic
	@Column(name = "page_orientation")
	public String getPageOrientation() {
		return pageOrientation;
	}

	public void setPageOrientation(String pageOrientation) {
		this.pageOrientation = pageOrientation;
	}
	
	@Basic
	@Column(name = "jpeg_spread_rotation")
	public Integer getJpegSpreadRotation() {
		return jpegSpreadRotation;
	}

	public void setJpegSpreadRotation(Integer jpegSpreadRotation) {
		this.jpegSpreadRotation = jpegSpreadRotation;
	}
	
	
	@Basic
	@Column(name = "master_shape")
	public String getMasterShape() {
		return masterShape;
	}

	public void setMasterShape(String masterShape) {
		this.masterShape = masterShape;
	}

	@Transient
	public Double getHingeGap() {
		return hingeGap;
	}

	public void setHingeGap(Double hingeGap) {
		this.hingeGap = hingeGap;
	}
	
	@Transient
	public boolean isFullImageCover()
	{
		return getCoverType()!=null && getCoverType().equals(CoverType.FIC);
	}
	
	@Transient
	public boolean isQuarterBoundImageCover()
	{
		return getCoverType()!=null && getCoverType().equals(CoverType.QBIC);
	}

	@Transient
	public String getShapeCode()
	{
		return getShape().replaceAll("PRF BK", "").trim();
	}
	
	@Transient
	public boolean isPortfolioTemplate()
	{
		return getShape().startsWith("P-");
	}
	
	
	@Transient
	public void setBleed(double bleed) {
		setBleedBottom(bleed);
		setBleedInside(bleed);
		setBleedOutside(bleed);
		setBleedTop(bleed);
	}
	
	@Transient
	public void setBleed(float bleed) {
		setBleed((double)bleed);
	}
	
	
	@Transient
	public void setSlug(double slug) {
		setSlugBottom(slug);
		setSlugInside(slug);
		setSlugOutside(slug);
		setSlugTop(slug);
	}
	
	@Transient
	public void setSlug(float slug) {
		setSlug((double)slug);
	}
	
	@Transient
	public void setMargin(double margin) {
		setMarginBottom(margin);
		setMarginInside(margin);
		setMarginOutside(margin);
		setMarginTop(margin);
	}
	
	@Transient
	public void setMargin(float margin) {
		setMargin((double)margin);
	}

	public BookTemplate() {
		super();
	}
	
	
	public BookTemplate(BookTemplate bookTemplate)
	{
		this.setBookTemplateId(bookTemplate.bookTemplateId);
		this.setBleedBottom(bookTemplate.bleedBottom);
		this.setBleedInside(bookTemplate.bleedInside);
		this.setBleedOutside(bookTemplate.bleedOutside);
		this.setBleedTop(bookTemplate.bleedTop);
		this.setHeight(bookTemplate.height);
		this.setIsProofBookTemplate(bookTemplate.isProofBookTemplate);
		this.setMarginBottom(bookTemplate.marginBottom);
		this.setMarginInside(bookTemplate.marginInside);
		this.setMarginOutside(bookTemplate.marginOutside);
		this.setMarginTop(bookTemplate.marginTop);
		this.setName(bookTemplate.name);
		this.setSpineWidth(bookTemplate.spineWidth);
		this.setWidth(bookTemplate.width);
		this.setPaperType(bookTemplate.paperType);
		this.setGapBetweenPages(bookTemplate.gapBetweenPages);
		this.setCoverType(bookTemplate.coverType);
		this.setShape(bookTemplate.shape);
		this.setGrain(bookTemplate.grain);
		this.setSlugBottom(bookTemplate.slugBottom);
		this.setSlugInside(bookTemplate.slugInside);
		this.setSlugOutside(bookTemplate.slugOutside);
		this.setSlugTop(bookTemplate.slugTop);
		this.setGrainLf(bookTemplate.grainLf);
		this.setImposition(bookTemplate.imposition);
		this.setImpositionLf(bookTemplate.impositionLf);
		this.setHingeGap(bookTemplate.hingeGap);
		this.setPageOrientation(bookTemplate.pageOrientation);
		this.setJpegSpreadRotation(bookTemplate.jpegSpreadRotation);
		this.setMasterShape(bookTemplate.masterShape);
	}
	
	
}