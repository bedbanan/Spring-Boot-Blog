package com.lalala.config;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/*
 * 安全配置类
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=false) //启动安全设置方法
public class SecurityConfig extends WebSecurityConfigurerAdapter{
    
	private static final String KEY = "lalala.com";

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Bean
	public PasswordEncoder PasswordEncoder() {
		return new BCryptPasswordEncoder(); //使用BCrypt加密
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);  //设置密码的加密方式
		return authenticationProvider;
	}
	
//	@Autowired
//	private  DataSource dataSource;
	
	/**
     * 记住我功能
     * @return
     */
//    @Bean
//    public PersistentTokenRepository persistentTokenRepository(){
//        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
//        jdbcTokenRepository.setDataSource(dataSource);
//        //自动创建数据库表，使用一次后注释掉，不然会报错
////        jdbcTokenRepository.setCreateTableOnStartup(true);
//        return jdbcTokenRepository;
//    }
	
	/*
	 * 自定义配置（非 Javadoc）设置访问权限
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**","/js/**","/fonts/**","/index").permitAll()//这些都可以自由访问
		.antMatchers("/h2-console/**").permitAll() //允许自由访问
		.antMatchers("/admins/**").hasRole("ADMIN")  //只允许ADMIN角色访问
		.and()
		.formLogin()  //基于form表单验证登录
		.loginPage("/login").failureUrl("/login-error")  //自定义登录界面且能重定向至异常界面
		.and().rememberMe().key(KEY) //启动rememberme  就是是否记住自己的账号
//		.and().rememberMe()
//		.tokenRepository(persistentTokenRepository())
//		.tokenValiditySeconds(3600)
		.and().exceptionHandling().accessDeniedPage("/403"); //处理异常，拒接访问就重定向
		http.csrf().ignoringAntMatchers("/h2-console/**");//禁止H2控制台的CSRF防护
		http.headers().frameOptions().sameOrigin();//允许来自同一来源的H2控制台的请求
	}
	/*
	 * 认证信息的管理
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)throws Exception{
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

}
