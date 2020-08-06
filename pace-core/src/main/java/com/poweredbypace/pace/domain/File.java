package com.poweredbypace.pace.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.SimpleUserSerializer;

@Entity
@Table(name="P_FILE")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("REGULAR")

@JsonIgnoreProperties(ignoreUnknown=true) 
@JsonTypeInfo(  
    use = JsonTypeInfo.Id.NAME,  
    include = JsonTypeInfo.As.PROPERTY,  
    property = "type"
)  
@JsonSubTypes({  
    @Type(value = File.class, name = "File"),  
    @Type(value = ImageFile.class, name = "ImageFile"),
    @Type(value = LogoFile.class, name = "LogoFile"),
    @Type(value = ProoferLogoFile.class, name = "ProoferLogoFile"),
    @Type(value = TextureFile.class, name = "TextureFile"),
    @Type(value = DieFile.class, name = "DieFile")
}) 
public class File extends BaseEntity {
	private static final long serialVersionUID = -5500872370604523099L;
	
	private String filename;
	private String url;
	private User user;
	
	@Column(name="FILENAME")
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	@Column(name="URL")
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@JsonSerialize(using=SimpleUserSerializer.class)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	
}
