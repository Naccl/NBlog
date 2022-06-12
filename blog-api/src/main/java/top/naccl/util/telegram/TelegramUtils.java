package top.naccl.util.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import top.naccl.config.properties.BlogProperties;
import top.naccl.config.properties.TelegramProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Telegram消息工具类
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
@Slf4j
@EnableRetry
@EnableAsync
@Lazy
@Component
public class TelegramUtils {
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RestTemplate restTemplateByProxy;
	@Autowired
	private TelegramProperties telegramProperties;
	@Autowired
	private BlogProperties blogProperties;

	public static final String SET_WEBHOOK = "/setWebhook";
	public static final String SEND_MESSAGE = "/sendMessage";

	private static final String PARSE_MODE = "HTML";

	/**
	 * 设置Webhook监听地址
	 */
	public void setWebhook() {
		log.info("start setWebhook");

		String url = telegramProperties.getApi() + telegramProperties.getToken() + SET_WEBHOOK;

		//与 TelegramBotController#getUpdate 请求地址一致
		String webhook = blogProperties.getApi() + "/tg/" + telegramProperties.getToken();

		Map<String, Object> data = new HashMap<>(2);
		data.put("url", webhook);

		sendByAutoCheckReverseProxy(url, data);
	}

	/**
	 * 构建POST消息的body
	 *
	 * @param content 消息内容
	 * @return
	 */
	public Map<String, Object> getMessageBody(String content) {
		Map<String, Object> body = new HashMap<>(4);
		body.put("chat_id", telegramProperties.getChatId());
		body.put("parse_mode", PARSE_MODE);
		body.put("text", content);
		return body;
	}

	/**
	 * 根据配置检查是否通过反代发送请求
	 * 发生异常时重试
	 *
	 * @param url  the URL
	 * @param data body of post
	 */
	@Retryable(
			include = {RestClientException.class},
			maxAttempts = 5,
			backoff = @Backoff(delay = 5000L, multiplier = 2)
	)
	@Async
	public void sendByAutoCheckReverseProxy(String url, Map<String, Object> data) {
		if (telegramProperties.getUseReverseProxy()) {
			send2ReverseProxy(url, data);
		} else {
			send2TelegramApi(url, data);
		}
	}

	/**
	 * 使用TelegramAPI发送消息
	 *
	 * @param data body of post
	 */
	private void send2TelegramApi(String url, Map<String, Object> data) {
		HttpEntity httpEntity = new HttpEntity(data);
		ResponseEntity<String> response = sendPostRequest(url, httpEntity, String.class);
		log.info("send2TelegramApi => url: {}", url);
		log.info("send2TelegramApi => response: {}", response);
	}

	/**
	 * 通过反向代理发送消息
	 * 这是我自己Cloudflare Worker的例子
	 *
	 * @param data body of post
	 */
	private void send2ReverseProxy(String url, Map<String, Object> data) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("content-type", MediaType.APPLICATION_JSON_VALUE);
		headers.add("User-Agent", "");

		Map<String, Object> body = new HashMap<>(4);
		body.put("to", url);
		body.put("data", data);
		HttpEntity httpEntity = new HttpEntity(body, headers);

		ResponseEntity<String> response = sendPostRequest(telegramProperties.getReverseProxyUrl(), httpEntity, String.class);
		log.info("send2ReverseProxy => reverseProxyUrl: {}", telegramProperties.getReverseProxyUrl());
		log.info("send2ReverseProxy => destUrl: {}", url);
		log.info("send2ReverseProxy => response: {}", response);
	}

	/**
	 * 根据配置是否启用代理发送请求
	 *
	 * @param url          the URL
	 * @param httpEntity   the Object to be POSTed (perhaps null)
	 * @param responseType responseType
	 * @param <T>          responseType
	 * @return the converted object
	 */
	private <T> ResponseEntity<T> sendPostRequest(String url, HttpEntity httpEntity, Class<T> responseType) {
		if (telegramProperties.getUseProxy()) {
			log.info("sendPostRequest => use proxy");
			return restTemplateByProxy.postForEntity(url, httpEntity, responseType);
		}
		log.info("sendPostRequest => unused proxy");
		return restTemplate.postForEntity(url, httpEntity, responseType);
	}
}
