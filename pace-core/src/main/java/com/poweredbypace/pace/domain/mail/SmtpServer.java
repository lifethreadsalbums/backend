package com.poweredbypace.pace.domain.mail;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.poweredbypace.pace.domain.BaseEntity;

@Entity
@Table(name="APP_SMTP_SERVER")
public class SmtpServer extends BaseEntity {
	
	private final Log log = LogFactory.getLog(SmtpServer.class);
	private static final long serialVersionUID = -1087523073261749793L;
	
	private String host;
	private Integer port;
	private String username;
	private String password;
	private String protocol;
	private String properties;
	
	@Column(name="HOST")
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	@Column(name="PORT")
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	@Column(name="USERNAME")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Column(name="PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="PROTOCOL")
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	@Column(name="PROPERTIES", columnDefinition="TEXT")
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	
	@Transient
	public Properties getJavaMailProperties()
	{
		if (properties==null)
			return null;
		Properties props = new Properties();
		try {
			props.load(new StringReader(properties));
			return props;
		} catch (IOException e) {
			log.error("Cannot instantiate properties", e);
		}
		return null;
	}
	

}
