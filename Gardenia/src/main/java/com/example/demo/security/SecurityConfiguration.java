package com.example.demo.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{

	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailsService);
		
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests()
		
				.antMatchers("/login/**").permitAll()
				.antMatchers("/country/**","/country/new/**","/country/edit/**").hasRole("MIS")
				.antMatchers("/state/**","/state/new/**","/state/edit/**").hasRole("MIS")
				.antMatchers("/region/**","/region/new/**","/region/edit/**").hasRole("MIS")
				.antMatchers("/district/**","/distrct/new/**","/district/edit/**").hasRole("MIS")
				.antMatchers("/city/**","/city/new/**","/city/edit/**").hasRole("MIS")
				.antMatchers("/area/**","/area/new/**","/area/edit/**").hasRole("MIS")
				.antMatchers("/hqmaster/new/**","/hqmaster/**","/hqmaster/edit/**").hasRole("MIS")
				.antMatchers("/product/new/**","/product/**","/product/edit/**").hasRole("MIS")
				.antMatchers("/distributor/new/**","/distributor/**","/distributor/edit/**").hasRole("MIS")
				.antMatchers("/user/new/**","/user/edit/**").hasRole("USER")
				.antMatchers("/user/**").hasAnyRole("MIS","USER")
				.and().formLogin().loginPage("/login.html")
				.and()
				.sessionManagement()
				.invalidSessionUrl("/login?invalid-session=true");
		
		
		http.cors().and().csrf().disable();
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");      
        web.ignoring().antMatchers("/scripts/**");
        web.ignoring().antMatchers("/images/**");
    }
	
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	@Bean 
	public SessionRegistry sessionResgistry() {
		SessionRegistry sessionRegistry = new SessionRegistryImpl();
		return sessionRegistry;
	}
	
	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

}