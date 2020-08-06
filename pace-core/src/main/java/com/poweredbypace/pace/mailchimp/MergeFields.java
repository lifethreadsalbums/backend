package com.poweredbypace.pace.mailchimp;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MergeFields {

	 private String FNAME;
	 private String LNAME;
	 private String PHONE;
	 private String MMERGE5;
	 private String MMERGE11;
	 private String MMERGE6;
	 private String MMERGE7;
	 private String MMERGE8;
	 private String MMERGE10;
	 private String MMERGE9;

	 @JsonProperty("FNAME")
	 public String getFNAME() {
	  return StringUtils.defaultIfBlank(FNAME, "");
	 }
	 @JsonProperty("LNAME")
	 public String getLNAME() {
	  return StringUtils.defaultIfBlank(LNAME, "");
	 }
	 @JsonProperty("PHONE")
	 public String getPHONE() {
	  return StringUtils.defaultIfBlank(PHONE, "");
	 }
	 @JsonProperty("MMERGE5")
	 public String getMMERGE5() {
	  return StringUtils.defaultIfBlank(MMERGE5, "");
	 }
	 @JsonProperty("MMERGE11")
	 public String getMMERGE11() {
	  return StringUtils.defaultIfBlank(MMERGE11, "");
	 }
	 @JsonProperty("MMERGE6")
	 public String getMMERGE6() {
	  return StringUtils.defaultIfBlank(MMERGE6, "");
	 }
	 @JsonProperty("MMERGE7")
	 public String getMMERGE7() {
	  return StringUtils.defaultIfBlank(MMERGE7, "");
	 }
	 @JsonProperty("MMERGE8")
	 public String getMMERGE8() {
	  return StringUtils.defaultIfBlank(MMERGE8, "");
	 }
	 @JsonProperty("MMERGE10")
	 public String getMMERGE10() {
	  return StringUtils.defaultIfBlank(MMERGE10, "");
	 }
	 @JsonProperty("MMERGE9")
	 public String getMMERGE9() {
	  return StringUtils.defaultIfBlank(MMERGE9, "");
	 }

	 // Setter Methods 

	 public void setFNAME(String FNAME) {
	  this.FNAME = FNAME;
	 }

	 public void setLNAME(String LNAME) {
	  this.LNAME = LNAME;
	 }

	 public void setPHONE(String PHONE) {
	  this.PHONE = PHONE;
	 }

	 public void setMMERGE5(String MMERGE5) {
	  this.MMERGE5 = MMERGE5;
	 }

	 public void setMMERGE11(String MMERGE11) {
	  this.MMERGE11 = MMERGE11;
	 }

	 public void setMMERGE6(String MMERGE6) {
	  this.MMERGE6 = MMERGE6;
	 }

	 public void setMMERGE7(String MMERGE7) {
	  this.MMERGE7 = MMERGE7;
	 }

	 public void setMMERGE8(String MMERGE8) {
	  this.MMERGE8 = MMERGE8;
	 }

	 public void setMMERGE10(String MMERGE10) {
	  this.MMERGE10 = MMERGE10;
	 }

	 public void setMMERGE9(String MMERGE9) {
	  this.MMERGE9 = MMERGE9;
	 }


}
