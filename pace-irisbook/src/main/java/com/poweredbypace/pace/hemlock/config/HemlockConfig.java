package com.poweredbypace.pace.hemlock.config;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.common.LiteralExpression;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.file.remote.handler.FileTransferringMessageHandler;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;


@Configuration
@ComponentScan({
	"com.poweredbypace.pace.hemlock.service", 
	"com.poweredbypace.pace.hemlock.domain",
	"com.poweredbypace.pace.hemlock.task"
})
public class HemlockConfig {
	
	@Value("${ftp.host}") private String ftpHost;
	@Value("${ftp.port}") private int ftpPort;
	@Value("${ftp.username}") private String ftpUsername;
	@Value("${ftp.password}") private String ftpPassword;
	@Value("${ftp.clientMode}") private int ftpClientMode;
	@Value("${ftp.remoteDir}") private String ftpRemoteDir;


	@Bean
	public MessageChannel hemlockFtp() {
		return new DirectChannel();
	}
	
	@Bean
	public MessageChannel outputChannel() {
		return new DirectChannel();
	}
	
	public DefaultFtpSessionFactory ftpSessionFactory() {
		DefaultFtpSessionFactory ftp = new DefaultFtpSessionFactory();
		ftp.setHost(ftpHost);
		ftp.setPort(ftpPort);
		ftp.setUsername(ftpUsername);
		ftp.setPassword(ftpPassword);
		ftp.setClientMode(ftpClientMode);
		return ftp;
	}
	
	@Bean
	@ServiceActivator(inputChannel = "hemlockFtp")
	public MessageHandler fileOutBoundGateway() {
		FileTransferringMessageHandler<FTPFile> ftp = 
				new FileTransferringMessageHandler<FTPFile>(ftpSessionFactory());
		ftp.setRemoteDirectoryExpression(new LiteralExpression(ftpRemoteDir));
		ftp.setCharset("UTF-8");
		return ftp;
	}
	
	@Bean
	@ServiceActivator(inputChannel = "outputChannel")
	public MessageHandler loggingHandler() {
		return new LoggingHandler("info");
	}

}
