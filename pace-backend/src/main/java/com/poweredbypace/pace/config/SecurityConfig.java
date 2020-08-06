package com.poweredbypace.pace.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import com.poweredbypace.pace.security.Http403ForbiddenEntryPoint;
import com.poweredbypace.pace.security.ImpersonationAuthProvider;
import com.poweredbypace.pace.security.PaceLogoutSuccessHandler;
import com.poweredbypace.pace.security.ProoferAuthProvider;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
	private static final String REMEMBER_ME_KEY="PACE-APP";
	
	@Resource(name="userService")
	private UserDetailsService userDetailsService;
	
	@Bean
	public DefaultAuthenticationEventPublisher authEventPublisher() {
		return new DefaultAuthenticationEventPublisher();
	}
	
	@Bean
	public AuthenticationProvider authProvider() {
		return new ImpersonationAuthProvider();
	}
	
	@Bean
	public AuthenticationProvider prooferAuthProvider() {
		return new ProoferAuthProvider();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new ShaPasswordEncoder();
	}
	
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder())
		.and()
			.authenticationProvider(authProvider())
			.authenticationProvider(prooferAuthProvider())
			.authenticationEventPublisher(authEventPublisher());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.sessionManagement()
        		.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        	.and()
        		.rememberMe().key(REMEMBER_ME_KEY).rememberMeServices(rememberMeServices()).and()
        	.securityContext()
            	.securityContextRepository(securityContextRepository())
            .and()
				.csrf().disable()
			
				.authorizeRequests()
				.antMatchers("/api/login", 
						"/index.html",
						"/health-check.html",
						"/crossdomain.xml",
						"/app/emailVerified.html",
						"/styles/**",
						"/scripts/**",
						"/bower_components/**",
						"/i18n/**",
						"/views/**",
						"/fonts/**",
						"/images/**",
						"/api/country/**",
						"/api/store/current",
						"/api/user/register",
						"/api/user/password/reset",
						"/api/userGroup",
						"/api/isEmailRegistered",
						"/api/user/resendVerificationEmail",
						"/api/currency/available",
						"/api/payment/payeezy/paymentComplete",
						"/verifyEmail",
						"/resetPassword",
						"/export_shipments/**",
						"/api/export/**",
						"/api/html-to-pdf").permitAll()
				.antMatchers("/report/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/api/report/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")
				.antMatchers("/api/sa/**").hasAuthority("ROLE_SUPER_ADMIN")
				
				.anyRequest().authenticated()
			.and()				
				.logout().logoutUrl("/api/logout").logoutSuccessHandler(logoutHandler())
			.and()
				.httpBasic().authenticationEntryPoint(entryPoint())
			.and()
				.headers().frameOptions().sameOrigin();
  
	}
	@Bean
    public SecurityContextRepository securityContextRepository() {
        HttpSessionSecurityContextRepository repo = new HttpSessionSecurityContextRepository();
        return repo;
    }
	
	@Bean
    public RememberMeServices rememberMeServices() {
        TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService);
        rememberMeServices.setTokenValiditySeconds(2678400); // 1month
        return rememberMeServices;
    }
	
	@Bean(name="authenticationManager")
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Bean
	public Http403ForbiddenEntryPoint entryPoint() {
		return new Http403ForbiddenEntryPoint();
	}
	
	@Bean
	public PaceLogoutSuccessHandler logoutHandler() {
		return new PaceLogoutSuccessHandler();
	}
	
	
}