package com.poweredbypace.pace.jobserver.config;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.integration.aop.PublisherAnnotationBeanPostProcessor;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import com.poweredbypace.pace.currency.CurrencyRateProvider;
import com.poweredbypace.pace.currency.WebservicexCurrencyRateProvider;
import com.poweredbypace.pace.env.Env;
import com.poweredbypace.pace.job.JobScheduler;
import com.poweredbypace.pace.jobserver.QueueProcessor;
import com.poweredbypace.pace.jobserver.SqsJobScheduler;
import com.poweredbypace.pace.mail.EmailAspect;
import com.poweredbypace.pace.mail.EmailTemplateLoader;
import com.poweredbypace.pace.mail.impl.JavaMailSenderConfigurer;
import com.poweredbypace.pace.push.RedisConfig;
import com.poweredbypace.pace.service.ScreenshotService;
import com.poweredbypace.pace.service.ViewService;
import com.poweredbypace.pace.service.impl.CronService;
import com.poweredbypace.pace.service.impl.ScreenshotServiceImpl;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModel;

@Configuration
@ComponentScan(basePackages = { 
		"com.poweredbypace.pace.service", 
		"com.poweredbypace.pace.repository",
		"com.poweredbypace.pace.util",
		"com.poweredbypace.pace.mail",
		"com.poweredbypace.pace.expression",
	})
@EnableScheduling
@EnableAspectJAutoProxy
@EnableCaching
@EnableAsync
@EnableJpaRepositories(basePackages = { "com.poweredbypace.pace.repository" })
@EnableTransactionManagement
@EnableIntegration
@Import({
	PdfConfig.class, 
	ShippingConfig.class,
	TaskConfig.class})
public class AppConfig {
	
	@Value("${jdbc.driverClassName}")				private String dbDriverClassName;
	@Value("${jdbc.url}") 							private String dbUrl;
	@Value("${jdbc.username}")						private String dbUsername;
	@Value("${jdbc.password}")						private String dbPassword;
	@Value("${jdbc.maxIdle}")						private int dbMaxIdle;
	@Value("${jdbc.maxActive}")						private int dbMaxActive;
	@Value("${jdbc.timeBetweenEvictionRunsMillis}") private long dbTimeBetweenEvictionRunsMillis;
	@Value("${jdbc.minEvictableIdleTimeMillis}")	private long dbMinEvictableIdleTimeMillis;
	@Value("${jdbc.defaultAutoCommit}")				private boolean dbDefaultAutoCommit;
	
	@Bean
	public QueueProcessor queueProcessor() {
		QueueProcessor bean = new QueueProcessor();
		return bean;
	}
	
	@Bean
	public JobScheduler jobScheduler() {
		SqsJobScheduler bean = new SqsJobScheduler();
		return bean;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() throws IOException {
	    PropertySourcesPlaceholderConfigurer pc = new PropertySourcesPlaceholderConfigurer();
	    pc.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:jobserver.properties"));
	    pc.setIgnoreUnresolvablePlaceholders(true);
	    pc.setIgnoreResourceNotFound(true);
	    pc.setOrder(0);
	    return pc;
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer productionPlaceHolderConfigurer() throws IOException {
	    PropertySourcesPlaceholderConfigurer pc = new PropertySourcesPlaceholderConfigurer();
	    pc.setLocations(new PathMatchingResourcePatternResolver().getResources("file:/pace/conf/jobserver.properties"));
	    pc.setIgnoreUnresolvablePlaceholders(true);
	    pc.setIgnoreResourceNotFound(true);
	    pc.setOrder(-1);
	    return pc;
	}
	
	@Bean
	public DataSource dataSource() {
		BasicDataSource bean = new BasicDataSource();
		bean.setDriverClassName(dbDriverClassName);
		bean.setUrl(dbUrl);
		bean.setUsername(dbUsername);
		bean.setPassword(dbPassword);
		bean.setMaxIdle(dbMaxIdle);
		bean.setMaxActive(dbMaxActive);
		bean.setTimeBetweenEvictionRunsMillis(dbTimeBetweenEvictionRunsMillis);
		bean.setMinEvictableIdleTimeMillis(dbMinEvictableIdleTimeMillis);
		bean.setDefaultAutoCommit(dbDefaultAutoCommit);
		return bean;
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
		factory.setJpaProperties(jpaProperties);

		factory.setDataSource(dataSource());
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("com.poweredbypace.pace.domain");
		factory.setPersistenceUnitName("paceUnit");
		
		//factory.afterPropertiesSet();
		//factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
		return factory;
	}
	
	@Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
		JpaTransactionManager transactionManager = new JpaTransactionManager();
	    transactionManager.setEntityManagerFactory(emf);
	 
	    return transactionManager;
	}
	
	@Bean
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager();
	}
	
	@Bean(name="webservicex-currency-rate-provider")
	public CurrencyRateProvider webservicexCurrencyRateProvider() {
		WebservicexCurrencyRateProvider provider = new WebservicexCurrencyRateProvider();
		provider.setCurrencyCodes(Arrays.asList(new String[] {"PLN", "USD", "CAD"} ));
		return provider;
	}
	
	@Bean
	public EmailTemplateLoader emailTemplateLoader() {
		return new EmailTemplateLoader();
	}
	
	@Bean
	public FreeMarkerConfigurationFactoryBean fmConfigFactory() {
		FreeMarkerConfigurationFactoryBean factory = new FreeMarkerConfigurationFactoryBean();
		factory.setPreTemplateLoaders(emailTemplateLoader());
		
		
		BeansWrapper w = new BeansWrapper(); 
		TemplateModel statics = w.getStaticModels(); 
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("statics", statics);
		factory.setFreemarkerVariables(map);
		//Properties props = new Properties();
		//props.put("template_update_delay", 0);
		//factory.setFreemarkerSettings(props);
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
	public PublisherAnnotationBeanPostProcessor pub() {
		return new PublisherAnnotationBeanPostProcessor();
	}
	
	@Bean
	public Env env(ViewService viewService) {
		Env env = viewService.getEnv();
		return env;
	}
	
	
	@Bean
	public ScreenshotService screenshotService() {
		ScreenshotService svc = new ScreenshotServiceImpl();
		return svc;
	}
	
	@Bean
	public CronService cronService() {
		return new CronService();
	}
	
	
	@Value("${redis.url}") private String redisUrl;

//	@Bean
//	public AtmosphereFramework atmosphereFramework() throws ServletException, InstantiationException, IllegalAccessException {
//	   AtmosphereFramework atmosphereFramework = new AtmosphereFramework(false, false);
//	   // atmosphereFramework.setBroadcasterCacheClassName(UUIDBroadcasterCache.class.getName());
//	   return atmosphereFramework;
//	}
	
//	
//	@Bean
//	public BroadcasterFactory broadcasterFactory() throws ServletException, InstantiationException, IllegalAccessException {
//	   return atmosphereFramework().getAtmosphereConfig().getBroadcasterFactory();
//	}
//	
	@Bean
	public RedisConfig cfg() {
		RedisConfig cfg = new RedisConfig();
		cfg.setRedisUrl(redisUrl);
		return cfg;
	}
//	
//	@Bean
//	public AtmosphereSpringContext atmosphereSpringContext() {
//	   AtmosphereSpringContext atmosphereSpringContext = new AtmosphereSpringContext();
//	   Map<String, String> map = new HashMap<String, String>();
//	   //map.put(ApplicationConfig.ANNOTATION_PACKAGE, PushService.class.getPackage().getName());
//	   
//	   map.put("org.atmosphere.cpr.broadcasterClass", RedisBroadcaster.class.getName());
//	   map.put(AtmosphereInterceptor.class.getName(), TrackMessageSizeInterceptor.class.getName());
//	   //map.put(AnnotationProcessor.class.getName(), VoidAnnotationProcessor.class.getName());
//	   map.put("org.atmosphere.plugin.redis.RedisBroadcaster.server", redisUrl);
//	   map.put("org.atmosphere.cpr.broadcasterLifeCyclePolicy", ATMOSPHERE_RESOURCE_POLICY.IDLE_DESTROY.toString());
//	   atmosphereSpringContext.setConfig(map);
//	   return atmosphereSpringContext;
//	}
	
}