package top.naccl.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description: JWT工具类
 * @Author: Naccl
 * @Date: 2020-09-02
 */
public class JwtUtils {
	private static int expireTime = 3600 * 6;
	public static String secretKey = "abcdefghijklmnopqrstuvwxyz";

	/**
	 * 生成token
	 *
	 * @param subject
	 * @return
	 */
	public static String generateToken(String subject) {
		String jwt = Jwts.builder()
				.setSubject(subject)
				.setExpiration(new Date(System.currentTimeMillis() + expireTime * 1000))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
		return jwt;
	}

	/**
	 * 校验token
	 *
	 * @param request
	 * @return
	 */
	public static Boolean validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader("Authorization");
		if (jwtToken != null && !"".equals(jwtToken) && !"null".equals(jwtToken)) {
			try {
				Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken.replace("Bearer", "")).getBody();
				String username = claims.getSubject();//获取当前登录用户名
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}
}
