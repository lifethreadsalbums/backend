package com.poweredbypace.pace.dto;

import com.poweredbypace.pace.domain.BaseEntity;

public abstract class BaseDto<T extends BaseEntity> {
	private Long id;
	private Integer version;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	public BaseDto(T entity) {
		this.id = entity.getId();
		this.version = entity.getVersion();
	}
	
	public BaseDto() {

	}
	
}
