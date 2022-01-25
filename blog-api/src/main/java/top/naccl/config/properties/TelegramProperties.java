package top.naccl.config.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Telegram配置
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
@Configuration
@ConfigurationProperties(prefix = "tg.bot")
public class TelegramProperties {
	/**
	 * Telegram bot的api，默认是https://api.telegram.org/bot
	 * 如果使用自己的反代，可以修改它
	 */
	private String api;
	/**
	 * bot的token，可以从 @BotFather 处获取
	 */
	private String token;
	/**
	 * 自己账号和bot的聊天会话id
	 */
	private String chatId;
	/**
	 * 是否使用代理
	 */
	private Boolean useProxy;
	/**
	 * 是否使用反向代理
	 */
	private Boolean useReverseProxy;
	/**
	 * 反向代理URL
	 */
	private String reverseProxyUrl;
}
