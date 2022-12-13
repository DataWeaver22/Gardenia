package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.jwt.JwtAuthenticationEntryPoint;
import com.example.demo.jwt.JwtAuthenticationFilter;
import com.example.demo.myUserDetailsService.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(myUserDetailsService);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.authorizeRequests().antMatchers("/signin", "/error").permitAll().antMatchers(HttpMethod.OPTIONS, "/**")
				.permitAll()
//				.antMatchers("/country/**", "/state/**", "/region/**", "/district/**", "/city/**", "/area/**",
//						"/hqmaster/**", "/brand/**", "/category/**", "/family/**", "/product/**", "/distributor/**","/user/")
//				.hasRole("MIS")
//				.antMatchers("/user/**").hasAnyRole("MIS","USER")
//				.antMatchers("/country/**","/country/new/**","/country/edit/**").hasRole("MIS")
				// .antMatchers("/state/**").hasRole("MIS");
//				.antMatchers("/region/**","/region/new/**","/region/edit/**").hasRole("MIS")
//				.antMatchers("/district/**","/distrct/new/**","/district/edit/**").hasRole("MIS")
//				.antMatchers("/city/**","/city/new/**","/city/edit/**").hasRole("MIS")
//				.antMatchers("/area/**","/area/new/**","/area/edit/**").hasRole("MIS")
//				.antMatchers("/hqmaster/new/**","/hqmaster/**","/hqmaster/edit/**").hasRole("MIS")
//				.antMatchers("/product/new/**","/product/**","/product/edit/**").hasRole("MIS")
//				.antMatchers("/distributor/new/**","/distributor/**","/distributor/edit/**").hasRole("MIS")
				.anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(jwtAuthenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().cors().disable().csrf().disable();

//		.and()
//		.sessionManagement()
//		.invalidSessionUrl("/login?invalid-session=true")

		http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

//	@Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/css/**");      
//        web.ignoring().antMatchers("/scripts/**");
//        web.ignoring().antMatchers("/images/**");
//    }

	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}

//	@Bean 
//	public SessionRegistry sessionResgistry() {
//		SessionRegistry sessionRegistry = new SessionRegistryImpl();
//		return sessionRegistry;
//	}
//	
//	@Bean
//	public HttpSessionEventPublisher httpSessionEventPublisher() {
//		return new HttpSessionEventPublisher();
//	}

}