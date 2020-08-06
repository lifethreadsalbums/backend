package com.poweredbypace.pace.domain.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.DigestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.poweredbypace.pace.domain.Address;
import com.poweredbypace.pace.domain.Address.AddressType;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.domain.layouttemplate.LayoutTemplate;

@Entity
@Table(name = "APP_USER")
@JsonIgnoreProperties(ignoreUnknown=true)
public class User extends BaseEntity implements UserDetails, Serializable {
	
	public enum UserStatus {
		New,
		Verified,
		Enabled,
		Suspended
	}
	
	private static final long serialVersionUID = 5739674843185312531L;

	private String email;
	private String password;
	private String newPassword;
	private String firstName;
	private String lastName;
	private String companyName;
	private String taxNumber;
	private String phone;
	private String website;
	private boolean enabledOld;
	private Date createDate;
	private Date discontinueDate;
	private boolean shippingAddressSameAsBillingAddress;
	private boolean verifiedOld;
	private boolean changePasswordOnNextLogin;
	private String customLogoUrl;
	private Date lastLoginDate;
	private Date approvalDate;
	private Set<Role> roles = new HashSet<Role>();
	private Group group;
	private List<Address> addresses;
	private List<LayoutTemplate> savedLayoutTemplates;
	private String taxExemptionNumber;
	private String currency;
	private UserStatus status;
	private Boolean isDeleted = false;
	private Boolean systemAccount = false;
	
	@Column(name = "SYSTEM_ACCOUNT", columnDefinition = "TINYINT(1)")
	public Boolean getSystemAccount() {
		return systemAccount;
	}
	public void setSystemAccount(Boolean systemAccount) {
		this.systemAccount = systemAccount;
	}
	
	@Column(name = "IS_DELETED", columnDefinition = "TINYINT(1)")
	public Boolean getIsDeleted() {
		return isDeleted;
	}
	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
	
	@Column(name = "EMAIL", nullable = false)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "PASSWORD", nullable = false)
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	@JsonSetter
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Transient
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	@Column(name = "FIRST_NAME", nullable = false)
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	@Column(name = "LAST_NAME", nullable = false)
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Column(name = "COMPANY_NAME")
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	@Column(name = "TAX_NUMBER")
	public String getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	
	@Column(name = "PHONE")
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Column(name = "WEBSITE")
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	
	@Column(name = "ENABLED", nullable = false, columnDefinition = "TINYINT(1)")
	public boolean getEnabledOld() {
		return enabledOld;
	}
	public void setEnabledOld(boolean enabledOld) {
		this.enabledOld = enabledOld;
	}
	
	@Column(name = "CREATE_DATE", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name = "DISCONTINUE_DATE")
	public Date getDiscontinueDate() {
		return discontinueDate;
	}
	public void setDiscontinueDate(Date discontinueDate) {
		this.discontinueDate = discontinueDate;
	}
	
	@Column(name = "SHIPPING_ADDR_SAME_AS_BILLING_ADDR", nullable = false, columnDefinition = "TINYINT(1)")
	public boolean isShippingAddressSameAsBillingAddress() {
		return shippingAddressSameAsBillingAddress;
	}
	public void setShippingAddressSameAsBillingAddress(
			boolean shippingAddressSameAsBillingAddress) {
		this.shippingAddressSameAsBillingAddress = shippingAddressSameAsBillingAddress;
	}
	
	@Column(name = "VERIFIED", nullable = false, columnDefinition = "TINYINT(1)")
	public boolean isVerifiedOld() {
		return verifiedOld;
	}
	public void setVerifiedOld(boolean verified) {
		this.verifiedOld = verified;
	}
	
	@Column(name = "FORCE_CHANGE_PASSWORD", nullable = false, columnDefinition = "TINYINT(1)")
	public boolean isChangePasswordOnNextLogin() {
		return changePasswordOnNextLogin;
	}
	public void setChangePasswordOnNextLogin(boolean changePasswordOnNextLogin) {
		this.changePasswordOnNextLogin = changePasswordOnNextLogin;
	}
	
	@Column(name = "CUSTOM_LOGO_URL")
	public String getCustomLogoUrl() {
		return customLogoUrl;
	}
	public void setCustomLogoUrl(String customLogoUrl) {
		this.customLogoUrl = customLogoUrl;
	}
	
	@Column(name = "LAST_LOGIN_DATE")
	public Date getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	
	@Column(name = "APPROVAL_DATE")
	public Date getApprovalDate() {
		return approvalDate;
	}
	public void setApprovalDate(Date approvalDate) {
		this.approvalDate = approvalDate;
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "APP_USER_ROLE", joinColumns = { 
			@JoinColumn(name = "USER_ID", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", 
					nullable = false, updatable = false) })
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "GROUP_ID")
	//@JsonView(SummaryView.class)
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade=CascadeType.ALL)
	public List<Address> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.REMOVE)
	@JoinTable(
		name = "APP_USER_SAVED_LAYOUT_TEMPLATE",
		joinColumns = { @JoinColumn(name = "USER_ID", nullable = false, updatable = false) }, 
		inverseJoinColumns = { @JoinColumn(name = "LAYOUT_TEMPLATE_ID", nullable = false, updatable = false) }
	)
	public List<LayoutTemplate> getSavedLayoutTemplates() {
		return savedLayoutTemplates;
	}
	public void setSavedLayoutTemplates(List<LayoutTemplate> savedLayoutTemplate) {
		this.savedLayoutTemplates = savedLayoutTemplate;
	}
	
	@Column(name = "TAX_EXEMPTION_NUMBER")
	public String getTaxExemptionNumber() {
		return taxExemptionNumber;
	}
	public void setTaxExemptionNumber(String taxExemptionNumber) {
		this.taxExemptionNumber = taxExemptionNumber;
	}
	
	@Column(name = "CURRENCY")
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	
	@Column(name="STATUS")
	@Enumerated(EnumType.STRING)
	public UserStatus getStatus() {
		return status;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}
	
	@Override
	@Transient
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return getRoles();
	}
	
	@Override
	@Transient
	@JsonIgnore
	public String getUsername() {
		return getEmail();
	}
	
	@Override
	@Transient
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	@Transient
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return status==UserStatus.Enabled;
	}
	
	@Override
	@Transient
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Transient
	@JsonIgnore
	public String getPasswordResetHash() {
		return getPasswordResetHash(new Date().getTime());
	}
	
	@Override
	@Transient
	public boolean isEnabled() {
		return status==UserStatus.Enabled;
	}
	
	@Transient
	public boolean isSuspended() {
		return status==UserStatus.Suspended;
	}
	
	@Transient
	public boolean isVerified() {
		return status!=UserStatus.New;
	}
	
	@Transient
	@JsonIgnore
	public String getPasswordResetHash(long timestamp) {
		String hexTimestamp = Long.toHexString(timestamp);
		String userInfo = this.getUsername() + this.getPassword() + hexTimestamp + "-" + this.getVersion();
		String hash = DigestUtils.md5DigestAsHex(userInfo.getBytes()) + "x" + hexTimestamp;
		return hash;
	}

	@Transient
	@JsonIgnore
	public String getVerificationHash() {
		String userInfo = this.getUsername() + this.getPassword() + this.getFirstName() + this.getLastName();
		String hash = DigestUtils.md5DigestAsHex(userInfo.getBytes());
		return hash;
	}
	
	@Transient
	@JsonIgnore
	public Address getAddress(AddressType type)
	{
		final List<Address> addresses = getAddresses();
		if(addresses != null) {
			for(Address address: addresses)
			{
				if (address.getAddressType()==type)
					return address;
			}
		}
		return null;
	}
	
	@Transient
	public void setAddress(Address address, AddressType type) {
		if(addresses == null)
			addresses = new ArrayList<Address>();
		
		// Remove address of given type if exists.
		final Address addr = getAddress(type);
		if(addr != null) {
			addresses.remove(addr);
		}
		
		// If address is not null - set the address type
		// and put into the set.
		if(address != null) {
			address.setAddressType(type);
			address.setUser(this);
			addresses.add(address);
		}
	}
	
	@Transient
	public Address getBillingAddress() {
		return getAddress(AddressType.BillingAddress);
	}
	public void setBillingAddress(Address address) {
		setAddress(address, AddressType.BillingAddress);
	}
	
	@Transient
	public Address getShippingAddress() {
		return getAddress(AddressType.ShippingAddress);
	}
	public void setShippingAddress(Address address) {
		setAddress(address, AddressType.ShippingAddress);
	}
	
	@Transient
	public boolean hasRole(String roleName) {
		for(Role role:getRoles()) {
			if (role.getName().equals(roleName))
				return true;
		}
		return false;
	}
	
	@Transient
	public boolean isAdmin() {
		return hasRole(Role.ROLE_ADMIN);
	}
	
	public void setAdmin(boolean admin) {
		// Do nothing.
	}
	
	@Transient
	public boolean isSuperAdmin() {
		return hasRole(Role.ROLE_SUPER_ADMIN);
	}
	
	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
}
