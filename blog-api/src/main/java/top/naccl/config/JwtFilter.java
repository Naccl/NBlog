package top.naccl.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import top.naccl.bean.Result;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Description: JWT请求过滤器
 * @Author: Naccl
 * @Date: 2020-07-21
 */
public class JwtFilter extends GenericFilterBean {
	private String secretKey = "abcdefghijklmnopqrstuvwxyz";

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String jwtToken = request.getHeader("Authorization");
		if (jwtToken != null && !"".equals(jwtToken) && !"null".equals(jwtToken)) {
			try {
				Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken.replace("Bearer", "")).getBody();
				String username = claims.getSubject();//获取当前登录用户名
//		        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList((String) claims.get("authorities"));
				UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, null, null);
				SecurityContextHolder.getContext().setAuthentication(token);
			} catch (Exception e) {
				response.setContentType("application/json;charset=utf-8");
				Result result = Result.create(403, "凭证已失效，请重新登录！");
				PrintWriter out = response.getWriter();
				out.write(new ObjectMapper().writeValueAsString(result));
				out.flush();
				out.close();
				return;
			}
		}
		filterChain.doFilter(request, servletResponse);
	}
}