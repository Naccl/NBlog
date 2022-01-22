package top.naccl.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import top.naccl.config.properties.ProxyProperties;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * RestTemplate相关的Bean配置
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
@Configuration
public class RestTemplateConfig {
	@Autowired
	private ProxyProperties proxyProperties;

	/**
	 * 默认的RestTemplate
	 *
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	/**
	 * 配置了代理和超时时间的RestTemplate
	 *
	 * @return
	 */
	@Bean
	public RestTemplate restTemplateByProxy() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyProperties.getHost(), proxyProperties.getPort()));
		requestFactory.setProxy(proxy);
		requestFactory.setConnectTimeout(proxyProperties.getTimeout());
		return new RestTemplate(requestFactory);
	}
}