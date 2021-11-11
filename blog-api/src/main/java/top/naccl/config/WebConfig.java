package top.naccl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.naccl.interceptor.AccessLimitInterceptor;

/**
 * @Description: 配置CORS跨域支持、拦截器
 * @Author: Naccl
 * @Date: 2020-07-22
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
	@Autowired
	AccessLimitInterceptor accessLimitInterceptor;
	private String accessPath;
	private String resourcesLocations;

	/**
	 * @param accessPath 请求地址映射
	 */
	@Value("${upload.access.path}")
	public void setAccessPath(String accessPath) {
		this.accessPath = accessPath;
	}

	/**
	 * @param resourcesLocations 本地文件路径映射
	 */
	@Value("${upload.resources.locations}")
	public void setResourcesLocations(String resourcesLocations) {
		this.resourcesLocations = resourcesLocations;
	}

	/**
	 * 跨域请求
	 *
	 * @param registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins("*")
				.allowedHeaders("*")
				.allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
				.maxAge(3600);
	}

	/**
	 * 请求拦截器
	 *
	 * @param registry
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(accessLimitInterceptor);
	}

	/**
	 * 本地静态资源路径映射
	 *
	 * @param registry
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(accessPath).addResourceLocations(resourcesLocations);
	}
}
