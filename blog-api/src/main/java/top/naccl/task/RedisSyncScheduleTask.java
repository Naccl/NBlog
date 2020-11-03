package top.naccl.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.naccl.config.RedisKeyConfig;
import top.naccl.service.BlogService;
import top.naccl.service.RedisService;

import java.util.Map;
import java.util.Set;

/**
 * @Description: Redis相关定时任务
 * @Author: Naccl
 * @Date: 2020-11-02
 */
@Component
public class RedisSyncScheduleTask {
	@Autowired
	RedisService redisService;
	@Autowired
	BlogService blogService;

	/**
	 * 从Redis同步博客浏览量到数据库
	 */
	public void syncBlogViewsToDatabase() {
		String redisKey = RedisKeyConfig.BLOG_VIEWS_MAP;
		Map map = redisService.getMapByHash(redisKey);
		Set<Integer> keys = map.keySet();
		for (Integer key : keys) {
			Integer views = (Integer) map.get(key);
			blogService.updateViews(key.longValue(), views);
		}
	}
}
