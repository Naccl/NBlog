package top.naccl.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 博客配置(目前用于评论提醒模板中的超链接)
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "blog")
public class BlogProperties {
	/**
	 * 博客名称
	 */
	private String name;
	/**
	 * 博客后端接口URL
	 */
	private String api;
	/**
	 * 博客前端后台管理URL
	 */
	private String cms;
	/**
	 * 博客前端前台页面URL
	 */
	private String view;
}
