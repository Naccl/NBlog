package top.naccl.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import top.naccl.bean.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 登录失败的回调
 * @Author: Naccl
 * @Date: 2020-07-21
 */

@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		response.setContentType("application/json;charset=utf-8");
		Result result = Result.create(500, exception.getMessage());
		//登录不成功时，会抛出对应的异常
		//可以查看 AuthenticationException 异常的继承关系，源码中有每种异常的解释
		if (exception instanceof LockedException) {
			result.setMsg("账号被锁定，请联系管理员！");
		} else if (exception instanceof CredentialsExpiredException) {
			result.setMsg("密码过期，请联系管理员！");
		} else if (exception instanceof AccountExpiredException) {
			result.setMsg("账号过期，请联系管理员！");
		} else if (exception instanceof DisabledException) {
			result.setMsg("账号被禁用，请联系管理员！");
		} else if (exception instanceof BadCredentialsException) {
			result.setMsg("用户名或密码错误！");
		}
		PrintWriter out = response.getWriter();
		out.write(new ObjectMapper().writeValueAsString(result));
		out.flush();
		out.close();
	}
}
