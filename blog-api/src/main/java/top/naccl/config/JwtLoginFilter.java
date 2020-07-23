package top.naccl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import top.naccl.bean.Result;
import top.naccl.bean.User;
import top.naccl.exception.BadRequestException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: JWT登录过滤器
 * @Author: Naccl
 * @Date: 2020-07-21
 */
public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
	private int expireTime = 3600;

	private String secretKey = "abcdefghijklmnopqrstuvwxyz";

	protected JwtLoginFilter(String defaultFilterProcessesUrl, AuthenticationManager authenticationManager) {
		super(new AntPathRequestMatcher(defaultFilterProcessesUrl));
		setAuthenticationManager(authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		try {
			if (!"POST".equals(request.getMethod())) {
				throw new BadRequestException("请求方法错误");
			}
			User user = new ObjectMapper().readValue(request.getInputStream(), User.class);
			return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		} catch (BadRequestException | MismatchedInputException exception) {
			response.setContentType("application/json;charset=utf-8");
			Result result = Result.create(400, "非法请求");
			PrintWriter out = response.getWriter();
			out.write(new ObjectMapper().writeValueAsString(result));
			out.flush();
			out.close();
		}
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
			throws IOException, ServletException {
		String jwt = Jwts.builder()
				.setSubject(authResult.getName())
				.setExpiration(new Date(System.currentTimeMillis() + expireTime * 1000))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();

		response.setContentType("application/json;charset=utf-8");
		User user = (User) authResult.getPrincipal();
		user.setPassword(null);
		Map<String, Object> map = new HashMap<>();
		map.put("user", user);
		map.put("token", jwt);
		Result result = Result.ok("登录成功", map);
		PrintWriter out = response.getWriter();
		out.write(new ObjectMapper().writeValueAsString(result));
		out.flush();
		out.close();
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		response.setContentType("application/json;charset=utf-8");
		Result result = Result.create(401, exception.getMessage());
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