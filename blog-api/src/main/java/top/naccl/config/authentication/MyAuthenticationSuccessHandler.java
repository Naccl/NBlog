package top.naccl.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import top.naccl.bean.Result;
import top.naccl.bean.User;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: 登录成功的回调
 * @Author: Naccl
 * @Date: 2020-07-21
 */

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		response.setContentType("application/json;charset=utf-8");
		User user = (User) authentication.getPrincipal();
		Result result = Result.ok("登录成功", user);
		PrintWriter out = response.getWriter();
		out.write(new ObjectMapper().writeValueAsString(result));
		out.flush();
		out.close();
	}
}
