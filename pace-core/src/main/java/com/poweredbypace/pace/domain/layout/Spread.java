package com.poweredbypace.pace.domain.layout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.layouttemplate.LayoutTemplate;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name="P_SPREAD")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Spread extends BaseEntity{

	private static final long serialVersionUID = -5968259606600789418L;

	private Integer numPages;
	private Integer pageNumber;
	private List<Element> elements = new ArrayList<Element>();
	private List<GuideLine> guideLines = new ArrayList<GuideLine>();
	private Layout layout;
	private String internalId;
	private LayoutTemplate template;
	private String lastTemplateMode;
	private Boolean locked;
	private Boolean applyAutoFill = true;
	private Boolean autoLayout = true;
	private Boolean hasErrorsLeft = false;
	private Boolean hasErrorsRight = false;
	private Integer errorTop;
	private Integer errorBottom;
	private Integer numLowResErrorsLeft = 0;
	private Integer numLowResErrorsRight = 0;
	private Integer quantity = 0;
	private LayoutSize layoutSize;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAYOUT_SIZE_ID")
	public LayoutSize getLayoutSize() {
		return layoutSize;
	}
	public void setLayoutSize(LayoutSize layoutSize) {
		this.layoutSize = layoutSize;
	}
	
	@Column(name="INTERNAL_ID")
	@JsonProperty("_id")
	public String getInternalId() { return internalId; }
	public void setInternalId(String internalId) { this.internalId = internalId; }
	
	@Column(name="NUM_PAGES")
	public Integer getNumPages() { return numPages; }
	public void setNumPages(Integer numPages) { this.numPages = numPages; }
	
	@Column(name="PAGE_NUMBER")
	public Integer getPageNumber() { return pageNumber;	}
	public void setPageNumber(Integer pageNumber) { this.pageNumber = pageNumber; }
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "spread",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	@OrderBy("zorder")
	public List<Element> getElements() { return elements; }
	public void setElements(List<Element> elements) { this.elements = elements;	}
	
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAYOUT_ID")
	public Layout getLayout() { return layout; }
	public void setLayout(Layout layout) { this.layout = layout; }
	
	@Column(name = "LOCKED", columnDefinition = "TINYINT(1)")
	public Boolean getLocked() { return locked; }
	public void setLocked(Boolean locked) { this.locked = locked; }
	
	@Transient
	public List<GuideLine> getGuideLines() { return guideLines; }
	public void setGuideLines(List<GuideLine> guideLines) { this.guideLines = guideLines; }
	
	@JsonIgnore
	@Column(name = "GUIDE_LINES", columnDefinition = "TEXT")
	public String getGuideLinesJson() throws JsonProcessingException {
		if(guideLines != null) {
			return JsonUtil.serialize(guideLines);
		} else return null;
	}
	public void setGuideLinesJson(String guideLines) throws JsonProcessingException, IOException {
		if(guideLines != null) {
			this.guideLines = JsonUtil.deserialize(guideLines, new TypeReference<List<GuideLine>>() {});
		}
	}
	
	@Transient
	public LayoutTemplate getTemplate() { return template; }
	public void setTemplate(LayoutTemplate template) { this.template = template; }
	
	@JsonIgnore
	@Column(name = "TEMPLATE", columnDefinition = "TEXT")
	public String getTemplateJson() throws JsonProcessingException {
		if (template != null) {
			return JsonUtil.serialize(template);
		} else return null;
	}
	public void setTemplateJson(String template) throws JsonProcessingException, IOException {
		if (template != null) {
			this.template = JsonUtil.deserialize(template, LayoutTemplate.class);
		}
	}
	
	@Column(name = "APPLY_AF", columnDefinition = "TINYINT(1)")
	public Boolean getApplyAutoFill() { return applyAutoFill; }
	public void setApplyAutoFill(Boolean applyAutoFill) { this.applyAutoFill = applyAutoFill; }
	
	
	@Column(name = "AUTO_LAYOUT", columnDefinition = "TINYINT(1)")
	public Boolean getAutoLayout() { return autoLayout; }
	public void setAutoLayout(Boolean autoLayout) { this.autoLayout = autoLayout; }
	
	@Column(name = "LAST_TEMPLATE_MODE")
	public String getLastTemplateMode() { return lastTemplateMode; }
	public void setLastTemplateMode(String lastTemplateMode) { this.lastTemplateMode = lastTemplateMode; }
	
	@Column(name = "HAS_ERRORS_LEFT", columnDefinition = "TINYINT(1)")
	public Boolean getHasErrorsLeft() {
		return hasErrorsLeft;
	}
	public void setHasErrorsLeft(Boolean hasErrorsLeft) {
		this.hasErrorsLeft = hasErrorsLeft;
	}
	
	@Column(name = "HAS_ERRORS_RIGHT", columnDefinition = "TINYINT(1)")
	public Boolean getHasErrorsRight() {
		return hasErrorsRight;
	}
	public void setHasErrorsRight(Boolean hasErrorsRight) {
		this.hasErrorsRight = hasErrorsRight;
	}
	
	@Column(name = "ERROR_TOP")
	public Integer getErrorTop() {
		return errorTop;
	}
	public void setErrorTop(Integer errorTop) {
		this.errorTop = errorTop;
	}
	
	@Column(name = "ERROR_BOTTOM")
	public Integer getErrorBottom() {
		return errorBottom;
	}
	public void setErrorBottom(Integer errorBottom) {
		this.errorBottom = errorBottom;
	}
	
	@Column(name = "NUM_LOWRES_ERRORS_LEFT")
	public Integer getNumLowResErrorsLeft() {
		return numLowResErrorsLeft;
	}
	public void setNumLowResErrorsLeft(Integer numLowResErrorsLeft) {
		this.numLowResErrorsLeft = numLowResErrorsLeft;
	}
	
	@Column(name = "NUM_LOWRES_ERRORS_RIGHT")
	public Integer getNumLowResErrorsRight() {
		return numLowResErrorsRight;
	}
	public void setNumLowResErrorsRight(Integer numLowResErrorsRight) {
		this.numLowResErrorsRight = numLowResErrorsRight;
	}
	
	@Column(name = "QUANTITY")
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
}
