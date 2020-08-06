package com.poweredbypace.pace.domain.layouttemplate;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Objects;
import com.poweredbypace.pace.util.JsonUtil;

@Entity
@DiscriminatorValue("GridLayoutTemplate")
@JsonIgnoreProperties(ignoreUnknown=true) 
public class GridLayoutTemplate extends LayoutTemplate {

	private static final long serialVersionUID = 5405640257872910151L;
	
	private Scheme scheme;
	private Integer numEffectiveCells;
	private Float desiredProportion;
	private String span;
	private String align;
	private Integer ord;
	
	private ArrayList<Row> rows;

	@Transient
	public ArrayList<Row> getRows() { return rows; }
	public void setRows(ArrayList<Row> rows) { this.rows = rows; }
	
	@Transient
	public Scheme getScheme() { return scheme; }
	public void setScheme(Scheme scheme) { this.scheme = scheme; }
	
	@Column(name = "NUM_EFFECTIVE_CELLS")
	public Integer getNumEffectiveCells() { return numEffectiveCells; }
	public void setNumEffectiveCells(Integer numEffectiveCells) { this.numEffectiveCells = numEffectiveCells; }
	
	@JsonIgnore
	@Column(name = "SCHEME", columnDefinition = "TEXT")
	public String getSchemeJson() throws JsonProcessingException {
		return JsonUtil.serialize(scheme);
	}
	public void setSchemeJson(String scheme) throws JsonProcessingException, IOException {
		this.scheme = JsonUtil.deserialize(scheme, Scheme.class);
	}
	
	@JsonIgnore
	@Column(name = "ROWS", columnDefinition = "TEXT")
	public String getRowsJson() throws JsonProcessingException {
		return JsonUtil.serialize(rows);
	}
	public void setRowsJson(String rows) throws JsonProcessingException, IOException {
		this.rows = JsonUtil.deserialize(rows, new TypeReference<ArrayList<Row>>(){});
	}
	
	@Column(name = "PROPORTION")
	public Float getDesiredProportion() { return desiredProportion; }
	public void setDesiredProportion(Float desiredProportion) { this.desiredProportion = desiredProportion; }
	
	@Column(name = "SPAN")
	public String getSpan() { return span; }
	public void setSpan(String span) { this.span = span; }
	
	@Column(name = "ALIGN")
	public String getAlign() { return align; }
	public void setAlign(String align) { this.align = align; }
	
	@Column(name = "ORD")
	public Integer getOrd() { return ord; }
	public void setOrd(Integer ord) { this.ord = ord; }
	
	@Override
	public String toString() {
		return Objects.toStringHelper(GridLayoutTemplate.class.getSimpleName())
				.add("id", getId())
				.add("publicTemplate", publicTemplate)
				.add("scheme", scheme)
				.add("numEffectiveCells", numEffectiveCells)
				.add("rows", rows)
				.add("desiredProportions", desiredProportion)
				.add("span", span)
				.add("ord", ord)
				.add("align", align).toString();
	}
	
	public static class Scheme implements Serializable {
		private static final long serialVersionUID = 8465083444974260598L;
		
		private Integer nRows;
		private Integer nCols;
		
		private ArrayList<Float> rowHeights;
		private ArrayList<Float> colWidths;
		
		@JsonProperty("nRows")
		public Integer getNRows() { return nRows; }
		public void setNRows(Integer nRows) { this.nRows = nRows; }
		
		@JsonProperty("nCols")
		public Integer getNCols() { return nCols; }
		public void setNCols(Integer nCols) { this.nCols = nCols; }
		
		public ArrayList<Float> getRowHeights() { return rowHeights; }
		public void setRowHeights(ArrayList<Float> rowHeights) { this.rowHeights = rowHeights; }
		
		public ArrayList<Float> getColWidths() { return colWidths; }
		public void setColWidths(ArrayList<Float> colWidhts) { this.colWidths = colWidhts; }
		
		@Override
		public String toString() {
			return Objects.toStringHelper(Scheme.class.getSimpleName())
					.add("nRows", nRows)
					.add("nCols", nCols)
					.add("rowHeights", rowHeights)
					.add("colWidths", colWidths).toString();
		}
	}
	
	public static class Row implements Serializable {
		private static final long serialVersionUID = 282720991397421318L;
		
		private ArrayList<Cell> cells;
		
		public ArrayList<Cell> getCells() { return cells; }
		public void setCells(ArrayList<Cell> cells) { this.cells = cells; }
		
		@Override
		public String toString() {
			return Objects.toStringHelper(Row.class.getSimpleName())
					.add("cells", cells).toString();
		}
	}
	
	public static class Cell implements Serializable {
		private static final long serialVersionUID = -8187681197571225472L;
		
		private Integer rowSpan = 0;
		private Integer colSpan = 0;
		private Integer order = 0;
		
		public Integer getRowSpan() { return rowSpan; }
		public void setRowSpan(Integer rowSpan) { this.rowSpan = rowSpan; }
		
		public Integer getColSpan() { return colSpan; }
		public void setColSpan(Integer colSpan) { this.colSpan = colSpan; }
		
		public Integer getOrder() { return order; }
		public void setOrder(Integer order) { this.order = order; }
		
		@Override
		public String toString() {
			return Objects.toStringHelper(Cell.class.getSimpleName())
					.add("rowSpan", rowSpan)
					.add("colSpan", colSpan)
					.add("order", order).toString();
		}		
	}

}