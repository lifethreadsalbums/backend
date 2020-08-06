package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name="P_FILM_STRIP_ITEM")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("dummy")
@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "type"
)  
@JsonSubTypes({  
    @Type(value = FilmStripImageItem.class, name = "FilmStripImageItem"),  
    @Type(value = FilmStripSpreadCut.class, name = "FilmStripSpreadCut"),
    @Type(value = FilmStripPageCut.class, name = "FilmStripPageCut")
}) 
@JsonIgnoreProperties(ignoreUnknown=true)
public class FilmStripItem extends BaseEntity {

	private static final long serialVersionUID = 4971192118410659658L;
	private FilmStrip filmStrip;
	private Integer currentOrder;
	private String internalId;
	private Boolean active;
	private Boolean stackCollapsed;
	private String stackId;
	private Integer stackItemCount;
	private Integer stackItemNumber;
	
	@JsonBackReference
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="FILMSTRIP_ID")
	public FilmStrip getFilmStrip() { return filmStrip; }
	public void setFilmStrip(FilmStrip filmStrip) { this.filmStrip = filmStrip; }
	
	@Column(name = "CURRENT_ORDER")
	public Integer getCurrentOrder() { return currentOrder; }
	public void setCurrentOrder(Integer currentOrder) { this.currentOrder = currentOrder; }
	
	@Column(name="INTERNAL_ID")
	@JsonProperty("_id")
	public String getInternalId() { return internalId; }
	public void setInternalId(String internalId) { this.internalId = internalId; }
	
	@Column(name="ACTIVE", columnDefinition = "TINYINT(1)")
	public Boolean getActive() { return active; }
	public void setActive(Boolean active) { this.active = active; }
	
	@Column(name="STACK_COLLAPSED", columnDefinition = "TINYINT(1)")
	public Boolean getStackCollapsed() { return stackCollapsed; }
	public void setStackCollapsed(Boolean stackCollapsed) { this.stackCollapsed = stackCollapsed; }
	
	@Column(name="STACK_ID")
	public String getStackId() { return stackId; }
	public void setStackId(String stackId) { this.stackId = stackId; }
	
	@Column(name="STACK_ITEM_COUNT")
	public Integer getStackItemCount() { return stackItemCount; }
	public void setStackItemCount(Integer stackItemCount) { this.stackItemCount = stackItemCount; }
	
	@Column(name="STACK_ITEM_NUMBER")
	public Integer getStackItemNumber() { return stackItemNumber; }
	public void setStackItemNumber(Integer stackItemNumber) { this.stackItemNumber = stackItemNumber; }
}