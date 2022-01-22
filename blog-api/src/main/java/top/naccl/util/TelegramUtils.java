package top.naccl.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.naccl.config.properties.TelegramProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Telegram消息工具类
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
@EnableAsync
@Component
public class TelegramUtils {
	@Lazy
	@Autowired
	private RestTemplate restTemplate;
	@Lazy
	@Autowired
	private RestTemplate restTemplateByProxy;
	@Autowired
	private TelegramProperties telegramProperties;

	private static final String SEND_MESSAGE = "/sendMessage";
	private static final String PARSE_MODE = "HTML";

	@Async
	public void send2TelegramApi(String content) {
		String url = telegramProperties.getApi() + telegramProperties.getToken() + SEND_MESSAGE;

		Map<String, Object> body = new HashMap<>(3);
		body.put("chat_id", telegramProperties.getChatId());
		body.put("parse_mode", PARSE_MODE);
		body.put("text", content);
		HttpEntity httpEntity = new HttpEntity(body);

		if (telegramProperties.getUseProxy()) {
			restTemplateByProxy.postForEntity(url, httpEntity, String.class);
		} else {
			restTemplate.postForEntity(url, httpEntity, String.class);
		}
	}
}
