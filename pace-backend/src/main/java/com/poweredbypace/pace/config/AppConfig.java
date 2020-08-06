package com.poweredbypace.pace.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import com.poweredbypace.pace.mailchimp.MailChimpService;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.integration.aop.PublisherAnnotationBeanPostProcessor;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.LocaleResolver;

import com.poweredbypace.pace.batch.BatchNamingStrategy;
import com.poweredbypace.pace.batch.DefaultBatchNamingStrategy;
import com.poweredbypace.pace.currency.CurrencyRateProvider;
import com.poweredbypace.pace.currency.UserLocaleResolver;
import com.poweredbypace.pace.currency.WebservicexCurrencyRateProvider;
import com.poweredbypace.pace.domain.BaseEntity;
import com.poweredbypace.pace.job.JobScheduler;
import com.poweredbypace.pace.jobserver.SqsJobScheduler;
import com.poweredbypace.pace.jobserver.config.TaskConfig;
import com.poweredbypace.pace.legacy.config.LegacyIrisConfig;
import com.poweredbypace.pace.mail.EmailAspect;
import com.poweredbypace.pace.mail.EmailTemplateLoader;
import com.poweredbypace.pace.mail.impl.JavaMailSenderConfigurer;
import com.poweredbypace.pace.patch.PartialUpdate;
import com.poweredbypace.pace.payment.PaymentGateway;
import com.poweredbypace.pace.payment.payeezy.PayeezyPaymentGateway;
import com.poweredbypace.pace.payment.psi.PsiGate;

@Configuration

@ComponentScan(basePackages = { 
	"com.poweredbypace.pace.service", 
	"com.poweredbypace.pace.repository",
	"com.poweredbypace.pace.env",
	"com.poweredbypace.pace.batch",
	"com.poweredbypace.pace.util",
	"com.poweredbypace.pace.mail",
	"com.poweredbypace.pace.exception.handler",
	"com.poweredbypace.pace.expression",
	"com.poweredbypace.pace.mailchimp"
})
@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy
@EnableAsync
@EnableJpaRepositories(basePackages = { "com.poweredbypace.pace.repository" })
@EnableTransactionManagement
@EnableIntegration
@Import({
	MultiHttpSecurityConfig.class,
	ShippingConfig.class,
	TaskConfig.class,
	LegacyIrisConfig.class,
	AtmosphereConfig.class
})
public class AppConfig {
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() throws IOException {
	    PropertySourcesPlaceholderConfigurer pc = new PropertySourcesPlaceholderConfigurer();
	    pc.setLocations(
	    		new PathMatchingResourcePatternResolver().getResources("classpath:properties/pace-app.properties"));
	    
	    pc.setIgnoreUnresolvablePlaceholders(true);
	    pc.setIgnoreResourceNotFound(true);
	    pc.setOrder(0);
	    return pc;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer productionPlaceHolderConfigurer() throws IOException {
	    PropertySourcesPlaceholderConfigurer pc = new PropertySourcesPlaceholderConfigurer();
	    pc.setLocations(
	    		new PathMatchingResourcePatternResolver().getResources("file:/pace/conf/pace-app.properties"));
	    
	    pc.setIgnoreUnresolvablePlaceholders(true);
	    pc.setIgnoreResourceNotFound(true);
	    pc.setOrder(-1);
	    return pc;
	}
	
	@Bean
    public PlatformTransactionManager transactionManager(){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
	 
	    return transactionManager;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()
	{
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		//vendorAdapter.setGenerateDdl(Boolean.TRUE);
		vendorAdapter.setShowSql(false);
		vendorAdapter.setDatabase(Database.MYSQL);

		Properties jpaProperties = new Properties();
		jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		jpaProperties.put("hibernate.connection.useUnicode", "true");
		jpaProperties.put("hibernate.connection.characterEncoding", "UTF-8");
		jpaProperties.put("hibernate.connection.charSet", "UTF-8");
		jpaProperties.put("hibernate.event.merge.entity_copy_observer", "allow");
		jpaProperties.put("hibernate.hbm2ddl.auto", hibernateHbl2dll);
		jpaProperties.put("hibernate.hbm2ddl.import_files", "/01_import_initial_data.sql,/02_import_store_config.sql,/03_products.sql,/04_import_resources.sql,/05_import_mock_data.sql");
		factory.setJpaProperties(jpaProperties);

		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.poweredbypace.pace.domain");
		factory.setPersistenceUnitName("paceUnit");
		
		//factory.afterPropertiesSet();
		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory;
	}

	@Bean
	public HibernateExceptionTranslator hibernateExceptionTranslator()
	{
		return new HibernateExceptionTranslator();
	}

	@Value("${jdbc.driverClassName}") private String jdbcClassName;
	@Value("${jdbc.url}") private String jdbcUrl;
	@Value("${jdbc.username}") private String jdbcUsername;
	@Value("${jdbc.password}") private String jdbcPassword;
	@Value("${hibernate.hbm2ddl.auto}") private String hibernateHbl2dll;
	
	@Bean
	public DataSource dataSource()
	{
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(jdbcClassName);
		dataSource.setUrl(jdbcUrl);
		dataSource.setUsername(jdbcUsername);
		dataSource.setPassword(jdbcPassword);
		
		dataSource.setMaxIdle(10);
		dataSource.setMaxActive(-1);
		dataSource.setTimeBetweenEvictionRunsMillis(60000);
		dataSource.setMinEvictableIdleTimeMillis(300000);
		dataSource.setDefaultAutoCommit(false);
		
		return dataSource;
	}

	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager();
	}
	
//	@Bean
//    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() throws MalformedURLException {
//        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
//        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("ehcache.xml"));
//        return ehCacheManagerFactoryBean;
//    }
//
//    @Bean
//    @Autowired
//    public EhCacheCacheManager cacheManager(EhCacheManagerFactoryBean ehcache) {
//        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();
//        ehCacheCacheManager.setCacheManager(ehcache.getObject());
//        return ehCacheCacheManager;
//    }
	
	
	@Bean
	public LocaleResolver localeResolver() {
		UserLocaleResolver resolver = new UserLocaleResolver();
		resolver.setDefaultLanguage("en");
		resolver.setDefaultLocale(Locale.CANADA);
		return resolver;
	}

	@Bean(name="webservicex-currency-rate-provider")
	public CurrencyRateProvider webservicexCurrencyRateProvider() {
		WebservicexCurrencyRateProvider provider = new WebservicexCurrencyRateProvider();
		provider.setCurrencyCodes(Arrays.asList(new String[] {"PLN", "USD", "CAD"} ));
		return provider;
	}
	
	@Value("${psigate.storeKeyUSD}") private String psiStoreKeyUSD;
	@Value("${psigate.storeKeyCAD}") private String psiStoreKeyCAD;
	@Value("${psigate.thanksUrl}") private String psiThanksUrl;
	@Value("${psigate.noThanksUrl}") private String psiNoThanksUrl;
	@Value("${psigate.gatewayUrl}") private String gatewayUrl;
	@Value("${psigate.verifyApiUrl}") private String verifyApiUrl;
	@Value("${psigate.passphrase}") private String passphrase;
	@Value("${psigate.storeID}") private String storeID;
	@Value("${psigate.useVerifyApi}") private boolean useVerifyApi;
	
	@Bean(name="psigate")
	public PaymentGateway psiGate() {
		PsiGate psiGate = new PsiGate();
		
		Map<String, String> storeKeys = new HashMap<String, String>();
		storeKeys.put("CAD", psiStoreKeyCAD);
		storeKeys.put("USD", psiStoreKeyUSD);
		
		psiGate.setStoreKeys(storeKeys);
		psiGate.setThanksUrl(psiThanksUrl);
		psiGate.setNoThanksUrl(psiNoThanksUrl);
		psiGate.setGatewayUrl(gatewayUrl);
		psiGate.setVerifyApiUrl(verifyApiUrl);
		psiGate.setPassphrase(passphrase);
		psiGate.setStoreID(storeID);
		psiGate.setUseVerifyApi(useVerifyApi);
		
		return psiGate;
	}
	
	@Bean(name="payeezy")
	public PaymentGateway payeezyGate() {
		PayeezyPaymentGateway gate = new PayeezyPaymentGateway();
		return gate;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
		ms.setDefaultEncoding("UTF-8");
		ms.setBasename("classpath:messages/messages");
		return ms;
	}
	
	@Bean
	public EmailTemplateLoader emailTemplateLoader() {
		return new EmailTemplateLoader();
	}

	@Bean
	public MailChimpService mailChimpService() { return new MailChimpService();}
	
	@Bean
	public FreeMarkerConfigurationFactoryBean fmConfigFactory() {
		FreeMarkerConfigurationFactoryBean factory = new FreeMarkerConfigurationFactoryBean();
		factory.setPreTemplateLoaders(emailTemplateLoader());
		return factory;
	}
	
	@Bean
	public JavaMailSenderConfigurer mailSenderConfigurer() {
		return new JavaMailSenderConfigurer();
	}
	
	@Bean
	public EmailAspect emailAspect() {
		return new EmailAspect();
	}
	
	@Bean
	public <Entity extends BaseEntity> PartialUpdate<Entity> partialUpdate() {
		return new PartialUpdate<Entity>();
	}
	
	@Bean
	public BatchNamingStrategy defaultBatchNamingStrategy() {
		return new DefaultBatchNamingStrategy();
	}
	
	@Bean
	public JobScheduler jobScheduler() {
		SqsJobScheduler bean = new SqsJobScheduler();
		return bean;
	}
	
	@Bean
	public PublisherAnnotationBeanPostProcessor pub() {
		return new PublisherAnnotationBeanPostProcessor();
	}	
	
}
