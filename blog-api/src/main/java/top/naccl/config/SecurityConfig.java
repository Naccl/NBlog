package top.naccl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import top.naccl.config.authentication.MyAuthenticationEntryPoint;
import top.naccl.config.authentication.MyAuthenticationFailureHandler;
import top.naccl.config.authentication.MyAuthenticationSuccessHandler;
import top.naccl.service.UserServiceImpl;


/**
 * @Description: Spring Security配置类
 * @Author: Naccl
 * @Date: 2020-07-19
 */

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserServiceImpl userService;
	@Autowired
	MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
	@Autowired
	MyAuthenticationFailureHandler myAuthenticationFailureHandler;
	@Autowired
	MyAuthenticationEntryPoint myAuthenticationEntryPoint;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(new MyPasswordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.anyRequest().authenticated()//拦截所有请求
				.and()
				.formLogin()
//				.loginPage("/admin/login")//未登录时，访问需要登录才能访问的接口，会自动重定向到该登录页面
				.loginProcessingUrl("/admin/login")//处理登录请求的接口URI
				.usernameParameter("username")//默认为 username
				.passwordParameter("password")//自定义账号密码的 key，默认为 password
				//登录成功的回调函数
				.successHandler(myAuthenticationSuccessHandler)
				//登录失败的回调函数
				.failureHandler(myAuthenticationFailureHandler)
				.permitAll()//和表单登录相关的接口直接通过
				.and()
				.logout()
				//注销时，直接在前端清除Token，不用发请求了
//				.logoutUrl("/admin/logout")
//				.logoutSuccessHandler((request, response, authentication) -> {
//					response.setContentType("application/json;charset=utf-8");
//					PrintWriter out = response.getWriter();
//					out.write("注销成功");
//					out.flush();
//					out.close();
//				})
				.permitAll()
				.and()
				.csrf().disable()//禁用 csrf 防御
				.exceptionHandling()
				//未登录时，返回json，而不重定向
				.authenticationEntryPoint(myAuthenticationEntryPoint);
	}
}
