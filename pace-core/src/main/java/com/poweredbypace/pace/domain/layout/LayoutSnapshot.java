package com.poweredbypace.pace.domain.layout;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.util.JsonUtil;


@Entity
@Table(name = "P_LAYOUT_SNAPSHOT")
@JsonIgnoreProperties(ignoreUnknown=true)
public class LayoutSnapshot extends BaseEntity {
	
	private static final long serialVersionUID = -3419168953016703374L;
	private Long layoutId;
	private Date date = new Date();
	private String layoutJson;
	
	public LayoutSnapshot() { }

	@Column(name = "LAYOUT_ID")
	//@JsonView(SummaryView.class)
	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}
	
	@Column(name = "DATE")
	//@JsonView(SummaryView.class)
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "LAYOUT_JSON", columnDefinition = "LONGTEXT")
	public String getLayoutJson() {
		return layoutJson;
	}

	public void setLayoutJson(String layoutJson) {
		this.layoutJson = layoutJson;
	}

	@Transient
	@JsonIgnore
	public Layout getLayout() {
		final String str = getLayoutJson();
		if (str != null) {
			return (Layout)JsonUtil.deserialize(str, Layout.class);
		} else return null;
	}
	
	@JsonSetter
	public void setLayout(Layout layout) {
		setLayoutJson(JsonUtil.serialize(layout));
	}

}
