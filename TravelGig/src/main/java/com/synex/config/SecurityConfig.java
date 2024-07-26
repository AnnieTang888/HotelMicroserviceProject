package com.synex.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	UserDetailsService userDetailsService;
	

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user =
//			 User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}
	


/*	@Bean 
	public SecurityFilterChain apiFilterChain2(HttpSecurity http) throws Exception {
		http
			.apply(MyCustomDsl.customDsl())
			.flag(true).and()
			.authorizeRequests()
				.requestMatchers("/", "/home").permitAll().and()
			      .exceptionHandling().accessDeniedPage("/accessDeniedPage").and()
			.authorizeRequests()
				.requestMatchers("/bookHotel", "/userProfile", "/test","/welcome").hasAnyAuthority("ADMIN").and()
		.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/home").permitAll().and()
		.logout()
		.logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
        .permitAll();
		
		return http.build();
	}*/
	
//May-15th	
	@Bean 
	public SecurityFilterChain apiFilterChain2(HttpSecurity http) throws Exception {
	    http
	        .apply(MyCustomDsl.customDsl())
	        .flag(true).and()
	        .authorizeRequests()
	            .requestMatchers("/", "/home").permitAll()  // Public access
	        .and()
	        .authorizeRequests()
	            .requestMatchers("/bookHotel", "/test", "/welcome").hasAnyAuthority("ADMIN")  // Admin only
	            .requestMatchers("/userProfile").authenticated()  // Accessible to any logged-in user
	        .and()
	        .exceptionHandling().accessDeniedPage("/accessDeniedPage")  // Custom access denied page
	        .and()
	        .formLogin()
	            .loginPage("/login")
	            .defaultSuccessUrl("/home")
	            .permitAll()
	        .and()
	        .logout()
	            .logoutSuccessUrl("/")
	            .invalidateHttpSession(true)
	            .deleteCookies("JSESSIONID")
	            .permitAll();
	    
	    return http.build();
	}


	

}