package com.poweredbypace.pace.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.proxy.HibernateProxyHelper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.poweredbypace.pace.exception.BeanCloneException;
import com.poweredbypace.pace.json.View;

@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 8189011176340081024L;
	
	private Long id;
	private Integer version;
	private Date modified;
	private Date created;
	
	
	@Column(name = "CREATED")
	@JsonIgnore
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@JsonIgnore
	@Column(name = "MODIFIED")
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	@Version
	@Column(name = "VERSION")
	@JsonView(View.BaseEntity.class)
	public Integer getVersion() {
		return version;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID")
	@JsonView(View.BaseEntity.class)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder b = new HashCodeBuilder();
		b.append(getId());
		b.append(getVersion());
		return b.hashCode();
	}

	@Override
	public boolean equals(Object o){
		if (o == null)
		    return false;
		
		Class<?> objClass = HibernateProxyHelper.getClassWithoutInitializingProxy(o);
	    if (this.getClass() != objClass) {
	        return false;
	    }
		  
		BaseEntity that = (BaseEntity)o;
		EqualsBuilder b = new EqualsBuilder();
		b.append(getId(), that.getId());
		b.append(getVersion(), that.getVersion());
		return b.isEquals();
	}

	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T copy() {
		try {
			return (T) BeanUtils.cloneBean(this);
		} catch (Exception e) {
			throw new BeanCloneException(e);
		} 
	}
	
	@PrePersist
	void onCreate() {
		this.setCreated(new Date());
	}
	
	@PreUpdate
	void onPersist() {
		this.setModified(new Date());
	}

	
}
