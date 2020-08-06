package com.poweredbypace.pace.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@Table(name = "APP_REPORT")
public class Report extends BaseEntity {
	
	private static final long serialVersionUID = -5424936085204381104L;
	
	public enum ReportEngine {
		JasperReports
	}
	
	private String code;
	private String name;
	private String source;
	private String engine;
	private Report parent;
	private List<Report> subreports = new ArrayList<Report>();
	private String sheetNames;
	private String paramsJson;
	private String filename;
	
	@Column(name = "CODE", nullable = false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@JsonIgnore
	@Column(name = "SOURCE", nullable = false, columnDefinition = "TEXT")
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	@Column(name = "ENGINE", nullable = false)
	public String getEngine() {
		return engine;
	}
	public void setEngine(String engine) {
		this.engine = engine;
	}
	
	@JsonIgnore
	@Column(name = "SHEET_NAMES", nullable = true)
	public String getSheetNames() {
		return sheetNames;
	}
	public void setSheetNames(String sheetNames) {
		this.sheetNames = sheetNames;
	}
	
	@JsonIgnore
	@JoinColumn(name = "PARENT_ID")
	@ManyToOne(fetch=FetchType.LAZY)
	public Report getParent() {
		return parent;
	}
	public void setParent(Report parent) {
		this.parent = parent;
	}
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent",
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	public List<Report> getSubreports() {
		return subreports;
	}
	public void setSubreports(List<Report> subreports) {
		this.subreports = subreports;
	}
	
	@JsonIgnore
	@Column(name = "PARAMS", nullable = false, columnDefinition = "TEXT")
	public String getParamsJson() {
		return paramsJson;
	}
	public void setParamsJson(String paramsJson) {
		this.paramsJson = paramsJson;
	}
	
	
	@JsonIgnore
	@Column(name = "FILENAME", nullable = false)
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Transient
	public List<ReportParameter> getParams() {
		if (StringUtils.isNotEmpty(getParamsJson())) {
			return JsonUtil.deserialize(getParamsJson(), new TypeReference<List<ReportParameter>>() {});
		} 
		return new ArrayList<Report.ReportParameter>();
	}
	
	@Transient
	public void setParams(List<ReportParameter> params) {
		setParamsJson( JsonUtil.serialize(params) );
	}
	
	public static class ReportParameter {
		private String name;
		private String description;
		private Class<?> type;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Class<?> getType() {
			return type;
		}
		public void setType(Class<?> type) {
			this.type = type;
		}
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		
		public Object valueOf(String value) {
			if (type.isAssignableFrom(java.sql.Date.class)) {
				return java.sql.Date.valueOf(value);
			} 
			return value;
		}
	}
	
}
