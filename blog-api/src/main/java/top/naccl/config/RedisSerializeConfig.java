package top.naccl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * @Description: Redis序列化配置
 * @Author: Naccl
 * @Date: 2020-09-27
 */
@Configuration
public class RedisSerializeConfig {

	/**
	 * 使用JSON序列化方式
	 *
	 * @param redisConnectionFactory
	 * @return
	 */
	@Bean
	public RedisTemplate<Object, Object> jsonRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
		template.setDefaultSerializer(serializer);
		return template;
	}
}
