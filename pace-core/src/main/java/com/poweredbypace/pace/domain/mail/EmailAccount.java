package com.poweredbypace.pace.domain.mail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name="APP_EMAIL_ACCOUNT")
public class EmailAccount extends BaseEntity {
	
	private static final long serialVersionUID = 417929995745495480L;
	
	private String from;
	private String fromName;
	private String to;
	private String cc;
	private String bcc;
	private SmtpServer smtpServer;
	
	@Column(name="EMAIL_FROM")
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	@Column(name="EMAIL_FROM_NAME")
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	@Column(name="EMAIL_TO")
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	@Column(name="EMAIL_CC")
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	@Column(name="EMAIL_BCC")
	public String getBcc() {
		return bcc;
	}
	public void setBcc(String bcc) {
		this.bcc = bcc;
	}
	
	@ManyToOne
	@JoinColumn(name = "SMTP_SERVER_ID")
	public SmtpServer getSmtpServer() {
		return smtpServer;
	}
	public void setSmtpServer(SmtpServer smtpServer) {
		this.smtpServer = smtpServer;
	}
	
}
