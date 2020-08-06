package com.poweredbypace.pace.patch;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Patch implements Serializable {
	
	public static enum Type {
		REPLACE,	// replace the value (primitives)
		SET_ID,		// retrieve item of given id from repository and set
		ADD_SET,	// create new item from JSON, add to repository and finally set
		APPEND,		// create new item from JSON, add to repository and finally append to collection
		APPEND_ID	// retrieve item of given id from repository and set
	}
	
	private static final long serialVersionUID = 3379453754825802513L;

	private String path;
	private String val;
	private Type type;
	
	public Patch() {}
	public Patch(final String json) {
		Preconditions.checkNotNull(json);
		
		ObjectMapper oMapper = new ObjectMapper();
		try {
			Patch patch = oMapper.readValue(json, Patch.class);
			this.path = patch.getPath();
			this.val = patch.getVal();
			this.type = patch.getType();
		} catch (Exception e) {
			throw new IllegalArgumentException("Not a JSON format.");
		}
	}
	public Patch(final String path, final String val, final Type type) {
		this.path = path;
		this.val = val;
		this.type = type;
	}
	
	public String getPath() { return path; }
	public void setPath(final String path) { this.path = path; }
	
	public String getVal() { return val; }
	public void setVal(final String val) { this.val = val; }
	
	public Type getType() { return type; }
	public void setType(final Type type) { this.type = type; }
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("path", path)
				.add("val", val)
				.add("type", type).toString();
	}

}