package top.naccl.task;

import org.springframework.stereotype.Component;

/**
 * @Description: Redis相关定时任务
 * @Author: Naccl
 * @Date: 2020-11-02
 */
@Component
public class RedisSyncScheduleTask {
	/**
	 * 从Redis同步博客浏览量到数据库
	 */
	public void syncBlogViewsToDatabase() {
		System.out.println("test");
	}
}
