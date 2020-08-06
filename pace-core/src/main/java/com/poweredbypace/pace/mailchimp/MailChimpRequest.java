package com.poweredbypace.pace.mailchimp;

import java.io.Serializable;

public class MailChimpRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String email_address;
	private String status;
	MergeFields merge_fields;

	// Getter Methods

	public String getEmail_address() {
		return email_address;
	}

	public String getStatus() {
		return status;
	}

	public MergeFields getMerge_fields() {
		if (merge_fields == null) {
			merge_fields = new MergeFields();
		}
		return merge_fields;
	}

	public void setEmail_address(String email_address) {
		this.email_address = email_address;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setMerge_fields(MergeFields merge_fields) {
		this.merge_fields = merge_fields;
	}
}
