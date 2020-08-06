package com.poweredbypace.pace.domain.layouttemplate;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name="P_LAYOUT_TEMPLATE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@JsonIgnoreProperties(ignoreUnknown=true) 
@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "type"
)
@JsonSubTypes({
		@Type(value = GridLayoutTemplate.class, name = "GridLayoutTemplate"),
		@Type(value = CustomLayoutTemplate.class, name = "CustomLayoutTemplate"),
		@Type(value = TwoPageLayoutTemplate.class, name = "TwoPageLayoutTemplate")
})
public abstract class LayoutTemplate extends BaseEntity {
	
	private static final long serialVersionUID = -9123286375119623550L;
	
	protected Boolean publicTemplate = false;
	protected String target;
	protected Long oldId;
	protected String historyId;
	
	@Column(name="IS_PUBLIC", columnDefinition = "TINYINT(1)")
	public Boolean isPublicTemplate() { return publicTemplate; }
	public void setPublicTemplate(Boolean publicTemplate) { this.publicTemplate = publicTemplate; }
	
	@Column(name = "TARGET")
	public String getTarget() { return target; }
	public void setTarget(String target) { this.target = target; }
	
	@Column(name="OLD_DB_ID")
	public Long getOldId() { return oldId; }
	public void setOldId(Long oldId) { this.oldId = oldId; }
	
	@Column(name="HIST_ID")
	public String getHistoryId() { return historyId; }
	public void setHistoryId(String historyId) { this.historyId = historyId; }
}