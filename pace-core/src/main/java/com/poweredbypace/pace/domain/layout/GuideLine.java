package com.poweredbypace.pace.domain.layout;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown=true)
public class GuideLine implements Serializable {
	private static final long serialVersionUID = -3602463938488015647L;
	
	private Integer x1;
	private Integer y1;
	private Integer x2;
	private Integer y2;
	private String type;
	private Boolean fitPage;
	
	@JsonProperty("x1")
	public Integer getX1() { return x1; }
	public void setX1(Integer x1) { this.x1 = x1; }
	
	@JsonProperty("x2")
	public Integer getX2() { return x2; }
	public void setX2(Integer x2) { this.x2 = x2; }
	
	@JsonProperty("y1")
	public Integer getY1() { return y1; }
	public void setY1(Integer y1) { this.y1 = y1; }
	
	@JsonProperty("y2")
	public Integer getY2() { return y2; }
	public void setY2(Integer y2) { this.y2 = y2; }
	
	@JsonProperty("__type")
	public String getType() { return type; }
	public void setType(String type) { this.type = type; }
	
	@JsonProperty("fitPage")
	public Boolean getFitPage() { return fitPage; }
	public void setFitPage(Boolean fitPage) { this.fitPage = fitPage; }

	@Override
	public String toString() {
		return Objects.toStringHelper(GuideLine.class.getSimpleName())
				.add("type", type)
				.add("fitPage", fitPage)
				.add("x1", x1)
				.add("x2", x2)
				.add("y1", y1)
				.add("y2", y2).toString();
	}
}