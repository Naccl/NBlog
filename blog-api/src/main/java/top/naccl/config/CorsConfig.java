package top.naccl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Description: CORS跨域支持
 * @Author: Naccl
 * @Date: 2020-07-22
 */

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedHeaders("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
				.maxAge(3600);
	}
}
