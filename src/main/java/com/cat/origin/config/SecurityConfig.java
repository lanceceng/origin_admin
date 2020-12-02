package com.cat.origin.config;

import com.cat.origin.filter.TokenFilter;
import com.cat.origin.modules.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * spring security配置
 * 
 * @author 小威老师 xiaoweijiagou@163.com
 * 
 *         2017年10月16日
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

//	@Autowired
//	private LogoutSuccessHandler logoutSuccessHandler;

	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

//	@Autowired
//	private UserDetailsService userDetailsService;

	@Autowired
	private TokenFilter tokenFilter;

//	@Bean
//	public UserDetailsService myUserService(){
//		return new UserDetailsServiceImpl();
//	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		// 基于token，所以不需要session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests()
//				.antMatchers(HttpMethod.GET,"/admin/doLogin").permitAll()
//				.antMatchers(HttpMethod.GET,"/admin/login").permitAll()
//				.antMatchers(HttpMethod.GET,"/admin/index").permitAll()
				.antMatchers("/admin/**").permitAll()
				// 允许对于网站静态资源的无授权访问
				.antMatchers("/", "/*.html", "/favicon.ico", "/css/**", "/js/**", "/fonts/**", "/layui/**", "/img/**",
						"/v2/api-docs/**", "/swagger-resources/**", "/webjars/**",
						"/plugins/**",
						"/font-awesome/**",
						"/amazeui/**",
						"/adminlte/**",
						"/common/**",
						"/bootstrap/**",
						"/jquery/**",
						"/sys/**",
						"/templates/admin/**",
						"/static/**/**", "/templates/**")
				.permitAll().anyRequest().authenticated();
		http.formLogin()
				.loginPage("/admin/doLogin")
//				.loginProcessingUrl("/admin/doLogin")
				.successHandler(authenticationSuccessHandler).failureHandler(authenticationFailureHandler).and()
				.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
		http.logout()
				.logoutUrl("/admin/quit")
				// 当登出成功之后，会被重定向到的地址
				.logoutSuccessUrl("/admin/login");
				// 指定登出成功后的处理，如果指定了这个，那么logoutSuccessUrl就不会生效。
//				.logoutSuccessHandler(logoutSuccessHandler);
		// 解决不允许显示在iframe的问题
		http.headers().frameOptions().disable();
		// 禁用缓存
		http.headers().cacheControl();
		// 添加JWT filter
		http.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(tokenFilter.getUserDetailsService()).passwordEncoder(bCryptPasswordEncoder());
	}

}
