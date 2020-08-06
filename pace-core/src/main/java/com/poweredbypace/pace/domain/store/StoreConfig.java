package com.poweredbypace.pace.domain.store;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class StoreConfig {
	
	private String appTitle;
	private String supportEmail;
	private String prooferUrl;
	private String notificationServerUrl;
	private String tourUrl;
	private String eulaUrl;
	private String termsUrl;
	private String privacyUrl;
	private String helpDeskUrl;
	private Map<String, Object> loginPage; 
	private Map<String, Object> logo; 
	private String urlPrefix;
	private String imageUrlPrefix;
	private String defaultMaterialUrl;
	private String defaultDarkMaterialUrl;
	private Map<String, Object> studioSampleDie;
	private Map<String, Object> prints;
	private Map<String, Object> welcomePage; 
	private Map<String, Object> adminOrders;
	private Map<String, Object> coverBuilder; 
	private Map<String, Object> dashboard;
	
	public String getAppTitle() {
		return appTitle;
	}
	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}
	public String getSupportEmail() {
		return supportEmail;
	}
	public void setSupportEmail(String supportEmail) {
		this.supportEmail = supportEmail;
	}
	public String getProoferUrl() {
		return prooferUrl;
	}
	public void setProoferUrl(String prooferUrl) {
		this.prooferUrl = prooferUrl;
	}
	public String getNotificationServerUrl() {
		return notificationServerUrl;
	}
	public void setNotificationServerUrl(String notificationServerUrl) {
		this.notificationServerUrl = notificationServerUrl;
	}
	public String getTourUrl() {
		return tourUrl;
	}
	public void setTourUrl(String tourUrl) {
		this.tourUrl = tourUrl;
	}
	public String getEulaUrl() {
		return eulaUrl;
	}
	public void setEulaUrl(String eulaUrl) {
		this.eulaUrl = eulaUrl;
	}
	public String getTermsUrl() {
		return termsUrl;
	}
	public void setTermsUrl(String termsUrl) {
		this.termsUrl = termsUrl;
	}
	public String getPrivacyUrl() {
		return privacyUrl;
	}
	public void setPrivacyUrl(String privacyUrl) {
		this.privacyUrl = privacyUrl;
	}
	public String getHelpDeskUrl() {
		return helpDeskUrl;
	}
	public void setHelpDeskUrl(String helpDeskUrl) {
		this.helpDeskUrl = helpDeskUrl;
	}
	public Map<String, Object> getLoginPage() {
		return loginPage;
	}
	public void setLoginPage(Map<String, Object> loginPage) {
		this.loginPage = loginPage;
	}
	public Map<String, Object> getLogo() {
		return logo;
	}
	public void setLogo(Map<String, Object> logo) {
		this.logo = logo;
	}
	public String getUrlPrefix() {
		return urlPrefix;
	}
	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
	public String getImageUrlPrefix() {
		return imageUrlPrefix;
	}
	public void setImageUrlPrefix(String imageUrlPrefix) {
		this.imageUrlPrefix = imageUrlPrefix;
	}
	public String getDefaultMaterialUrl() {
		return defaultMaterialUrl;
	}
	public void setDefaultMaterialUrl(String defaultMaterialUrl) {
		this.defaultMaterialUrl = defaultMaterialUrl;
	}
	public String getDefaultDarkMaterialUrl() {
		return defaultDarkMaterialUrl;
	}
	public void setDefaultDarkMaterialUrl(String defaultDarkMaterialUrl) {
		this.defaultDarkMaterialUrl = defaultDarkMaterialUrl;
	}
	public Map<String, Object> getStudioSampleDie() {
		return studioSampleDie;
	}
	public void setStudioSampleDie(Map<String, Object> studioSampleDie) {
		this.studioSampleDie = studioSampleDie;
	}
	public Map<String, Object> getPrints() {
		return prints;
	}
	public void setPrints(Map<String, Object> prints) {
		this.prints = prints;
	}
	public Map<String, Object> getWelcomePage() {
		return welcomePage;
	}
	public void setWelcomePage(Map<String, Object> welcomePage) {
		this.welcomePage = welcomePage;
	}
	public Map<String, Object> getAdminOrders() {
		return adminOrders;
	}
	public void setAdminOrders(Map<String, Object> adminOrders) {
		this.adminOrders = adminOrders;
	}
	public Map<String, Object> getCoverBuilder() {
		return coverBuilder;
	}
	public void setCoverBuilder(Map<String, Object> coverBuilder) {
		this.coverBuilder = coverBuilder;
	}
	public Map<String, Object> getDashboard() {
		return dashboard;
	}
	public void setDashboard(Map<String, Object> dashboard) {
		this.dashboard = dashboard;
	}
	
}
