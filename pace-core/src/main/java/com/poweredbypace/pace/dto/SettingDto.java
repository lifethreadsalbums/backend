package com.poweredbypace.pace.dto;

import java.io.Serializable;

import com.poweredbypace.pace.util.JsonUtil;

public class SettingDto implements Serializable {

	private static final long serialVersionUID = -1111019511255541614L;

	private String name;
	private String value;
	private String type;
	
	public String getName() {
		return name;
	}
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(final String value) {
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	public void setType(final String type) {
		this.type = type;
	}
	
	public static SettingDto[] getFromString(final String settingStr) {
		return JsonUtil.deserialize(settingStr, SettingDto[].class);
	}
	
}