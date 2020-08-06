package com.poweredbypace.pace.domain.layout;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.ImageFile;
import com.poweredbypace.pace.domain.Product;
import com.poweredbypace.pace.domain.user.User;
import com.poweredbypace.pace.json.BaseEntitySerializer;
import com.poweredbypace.pace.json.SimpleUserSerializer;

@Entity
@Table(name="P_PROOFER_SETTINGS")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProoferSettings extends BaseEntity {
	
	public static enum ProofStatus {
		WaitingOnDesigner,
		WaitingOnClient,
		Approved,
		NoComments
	}
	
	private static final long serialVersionUID = -7849757503706107693L;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private ImageFile logo;
	private Product product;
	private User user;
	private Boolean published;
	private Boolean approved;
	
	
	@Column(name = "FIRST_NAME")
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "LAST_NAME")
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "PUBLISHED", columnDefinition = "TINYINT(1)")
	public Boolean getPublished() {
		return published;
	}
	public void setPublished(Boolean published) {
		this.published = published;
	}
	
	@Column(name = "APPROVED", columnDefinition = "TINYINT(1)")
	public Boolean getApproved() {
		return approved;
	}
	public void setApproved(Boolean approved) {
		this.approved = approved;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PRODUCT_ID")
	@JsonSerialize(using=BaseEntitySerializer.class)
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LOGO_ID")
	public ImageFile getLogo() {
		return logo;
	}
	public void setLogo(ImageFile logo) {
		this.logo = logo;
	}
	
	@JsonSerialize(using=SimpleUserSerializer.class)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
