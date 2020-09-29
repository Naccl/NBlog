package top.naccl.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;
import top.naccl.service.RedisService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 读写Redis相关操作
 * @Author: Naccl
 * @Date: 2020-09-27
 */
@Service
public class RedisServiceImpl implements RedisService {
	@Autowired
	RedisTemplate jsonRedisTemplate;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public PageResult<BlogInfo> getBlogInfoPageResultByHash(String hash, Integer pageNum) {
		if (jsonRedisTemplate.opsForHash().hasKey(hash, pageNum)) {
			Object redisResult = jsonRedisTemplate.opsForHash().get(hash, pageNum);
			PageResult<BlogInfo> pageResult = objectMapper.convertValue(redisResult, PageResult.class);
			return pageResult;
		} else {
			return null;
		}
	}

	@Override
	public void saveBlogInfoPageResultToHash(String hash, Integer pageNum, Object object) {
		jsonRedisTemplate.opsForHash().put(hash, pageNum, object);
	}

	@Override
	public <T> List<T> getListByValue(String key) {
		List<T> redisResult = (List<T>) jsonRedisTemplate.opsForValue().get(key);
		return redisResult;
	}

	@Override
	public <T> void saveListToValue(String key, List<T> list) {
		jsonRedisTemplate.opsForValue().set(key, list);
	}

	@Override
	public <T> Map<String, T> getMapByValue(String key) {
		Map<String, T> redisResult = (Map<String, T>) jsonRedisTemplate.opsForValue().get(key);
		return redisResult;
	}

	@Override
	public <T> void saveMapToValue(String key, Map<String, T> map) {
		jsonRedisTemplate.opsForValue().set(key, map);
	}
}
