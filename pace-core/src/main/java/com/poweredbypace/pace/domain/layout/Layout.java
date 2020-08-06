package com.poweredbypace.pace.domain.layout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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

import org.apache.commons.lang.BooleanUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.PrototypeProduct.FirstPageType;
import com.poweredbypace.pace.domain.layouttemplate.LayoutTemplates;
import com.poweredbypace.pace.json.BaseEntitySerializer;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "P_LAYOUT")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Layout extends BaseEntity {

	private static final long serialVersionUID = -211705239380661062L;
	
	private LayoutSize layoutSize;
	private FilmStrip filmStrip;
	private List<Spread> spreads = new ArrayList<Spread>();
	private String recentlyUsedLayoutTemplates;
	private String templatesHistory;
	private FirstPageType firstPageType;
	private Boolean isLayFlat;
	private Integer autoFillVariant;
	private Integer viewMode;
	private Boolean locked;
	private Boolean autoFillEnabled;
	private String viewStateJson;
	private String adminViewStateJson;
	private Integer productVersion;
	private Date dateCreated;
	private Integer revision;
	private Layout mainLayout;
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="LAYOUT_SIZE_ID")
	public LayoutSize getLayoutSize() {
		return layoutSize;
	}
	public void setLayoutSize(LayoutSize layoutSize) {
		this.layoutSize = layoutSize;
	}
	
	@JsonManagedReference
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "layout",
		cascade = CascadeType.ALL,
		orphanRemoval = true)
	public List<Spread> getSpreads() {
		return spreads;
	}
	public void setSpreads(List<Spread> spreads) {
		this.spreads = spreads;
	}
	
	@JsonManagedReference
	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name="FILMSTRIP_ID")
	public FilmStrip getFilmStrip() {
		return filmStrip;
	}
	public void setFilmStrip(FilmStrip filmStrip) {
		this.filmStrip = filmStrip;
	}

	@JsonIgnore
	@Column(name = "RECENTLY_USED_TEMPLATES", columnDefinition = "LONGTEXT")
	public String getRecentlyUsedLayoutTemplatesJson() {
		return recentlyUsedLayoutTemplates;
	}
	public void setRecentlyUsedLayoutTemplatesJson(String recentlyUsedLayoutTemplates) {
		this.recentlyUsedLayoutTemplates = recentlyUsedLayoutTemplates;
	}
	
	@JsonIgnore
	@Column(name = "TEMPLATES_HISTORY", columnDefinition = "LONGTEXT")
	public String getTemplatesHistoryJson() {
		return templatesHistory;
	}
	public void setTemplatesHistoryJson(String templatesHistory) {
		this.templatesHistory = templatesHistory;
	}
	
	@Column(name = "FIRST_PAGE_TYPE")
	@Enumerated(EnumType.STRING)
	public FirstPageType getFirstPageType() {
		return firstPageType;
	}
	public void setFirstPageType(FirstPageType firstPageType) {
		this.firstPageType = firstPageType;
	}
	
	@Column(name = "IS_LAY_FLAT", columnDefinition = "TINYINT(1)")
	public Boolean getIsLayFlat() {
		return isLayFlat;
	}
	public void setIsLayFlat(Boolean isLayFlat) {
		this.isLayFlat = isLayFlat;
	}
	
	@Column(name = "AUTOFILL_VARIANT")
	public Integer getAutoFillVariant() {
		return autoFillVariant;
	}
	public void setAutoFillVariant(Integer autoFillVariant) {
		this.autoFillVariant = autoFillVariant;
	}
	
	@Column(name = "LOCKED", columnDefinition = "TINYINT(1)")
	public Boolean getLocked() { return locked; }
	public void setLocked(Boolean locked) { this.locked = locked; }
	
	@Column(name = "AUTOFILL_ENABLED", columnDefinition = "TINYINT(1)")
	public Boolean getAutoFillEnabled() {
		return autoFillEnabled;
	}
	public void setAutoFillEnabled(Boolean autoFillEnabled) {
		this.autoFillEnabled = autoFillEnabled;
	}
	
	@Column(name = "VIEW_MODE")
	public Integer getViewMode() {
		return viewMode;
	}
	public void setViewMode(Integer viewMode) {
		this.viewMode = viewMode;
	}
	
	@JsonIgnore
	@Column(name = "VIEW_STATE", columnDefinition = "LONGTEXT")
	public String getViewStateJson() {
		return viewStateJson;
	}
	public void setViewStateJson(String viewStateJson) {
		this.viewStateJson = viewStateJson;
	}
	
	@JsonIgnore
	@Column(name = "ADMIN_VIEW_STATE", columnDefinition = "LONGTEXT")
	public String getAdminViewStateJson() {
		return adminViewStateJson;
	}
	public void setAdminViewStateJson(String adminViewStateJson) {
		this.adminViewStateJson = adminViewStateJson;
	}
	
	@JsonIgnore
	@Column(name = "PRODUCT_VERSION")
	public Integer getProductVersion() {
		return productVersion;
	}
	public void setProductVersion(Integer productVersion) {
		this.productVersion = productVersion;
	}
	
	@Transient
	public Object getViewState() {
		final String json = getViewStateJson();
		if (json != null) 
			return JsonUtil.deserialize(json, Object.class);
		 
		return null;
	}
	public void setViewState(Object viewState) {
		setViewStateJson(JsonUtil.serialize(viewState));
	}
	
	@Transient
	public Object getAdminViewState() {
		final String json = getAdminViewStateJson();
		if (json != null) 
			return JsonUtil.deserialize(json, Object.class);
		 
		return null;
	}
	public void setAdminViewState(Object viewState) {
		setAdminViewStateJson(JsonUtil.serialize(viewState));
	}
	
	@Transient
	public LayoutTemplates getRecentlyUsedLayoutTemplates() {
		final String str = getRecentlyUsedLayoutTemplatesJson();
		if (str != null) {
			return JsonUtil.deserialize(str, LayoutTemplates.class);
		} else return new LayoutTemplates();
	}
	public void setRecentlyUsedLayoutTemplates(LayoutTemplates templates) {
		setRecentlyUsedLayoutTemplatesJson(JsonUtil.serialize(templates));
	}
	
	@Transient
	public LayoutTemplates[] getTemplatesHistory() {
		final String str = getTemplatesHistoryJson();
		if (str != null) {
			return JsonUtil.deserialize(str,  LayoutTemplates[].class);
		} else return new LayoutTemplates[0];
	}
	public void setTemplatesHistory(LayoutTemplates[] templates) {
		setTemplatesHistoryJson(JsonUtil.serialize(templates));
	}
	
	@Transient
	@JsonIgnore
	public boolean isLayFlat() {
		return BooleanUtils.isTrue(this.getIsLayFlat());
	}
	
	@Column(name = "DATE_CREATED")
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	@Column(name = "REVISION")
	public Integer getRevision() {
		return revision;
	}
	public void setRevision(Integer revision) {
		this.revision = revision;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="MAIN_LAYOUT_ID")
	@JsonSerialize(using=BaseEntitySerializer.class)
	public Layout getMainLayout() {
		return mainLayout;
	}
	public void setMainLayout(Layout mainLayout) {
		this.mainLayout = mainLayout;
	}
	
}