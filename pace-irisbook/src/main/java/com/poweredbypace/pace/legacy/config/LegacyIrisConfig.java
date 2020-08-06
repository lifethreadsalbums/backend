package com.poweredbypace.pace.legacy.config;

import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.poweredbypace.pace.legacy.IrisbookImportService;

@Configuration
public class LegacyIrisConfig {

	@Value("${jdbc.driverClassName}") private String jdbcClassName;
	@Value("${jdbc.url}") private String jdbcUrl;
	@Value("${jdbc.username}") private String jdbcUsername;
	@Value("${jdbc.password}") private String jdbcPassword;
	
	
	@Bean
	public LocalContainerEntityManagerFactoryBean irisEmf()
	{
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(jdbcClassName);
		ds.setUrl(jdbcUrl.replace("pace_iris", "irisbook"));
		ds.setUsername(jdbcUsername);
		ds.setPassword(jdbcPassword);
		
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setShowSql(false);
		vendorAdapter.setDatabase(Database.MYSQL);

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		jpaProperties.put("hibernate.connection.useUnicode", "true");
		jpaProperties.put("hibernate.connection.characterEncoding", "UTF-8");
		jpaProperties.put("hibernate.connection.charSet", "UTF-8");
		factory.setJpaProperties(jpaProperties);
		
		factory.setDataSource(ds);
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.poweredbypace.pace.legacy.domain");
		factory.setPersistenceUnitName("irisbookUnit");
		
		//factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory;
	}
	
	@Bean
	public IrisbookImportService irisbookImportService() {
		return new IrisbookImportService();
	}
	

}
