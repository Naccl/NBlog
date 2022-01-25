package top.naccl.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * GitHub配置(目前用于评论中QQ头像的图床)
 *
 * @author: Naccl
 * @date: 2022-01-23
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "upload.github")
public class GithubProperties {
	/**
	 * GitHub token
	 */
	private String token;
	/**
	 * GitHub username
	 */
	private String username;
	/**
	 * GitHub 仓库名
	 */
	private String repos;
	/**
	 * GitHub 仓库路径
	 */
	private String reposPath;
}
