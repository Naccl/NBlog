package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.config.RedisKeyConfig;
import top.naccl.model.vo.Result;
import top.naccl.service.DashboardService;
import top.naccl.service.RedisService;
import top.naccl.service.VisitLogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 后台管理仪表盘
 * @Author: Naccl
 * @Date: 2020-10-08
 */
@RestController
@RequestMapping("/admin")
public class DashboardAdminController {
	@Autowired
	DashboardService dashboardService;
	@Autowired
	VisitLogService visitLogService;
	@Autowired
	RedisService redisService;

	@GetMapping("/dashboard")
	public Result dashboard() {
		int todayPV = visitLogService.countVisitLogByToday();
		int todayUV = redisService.countBySet(RedisKeyConfig.IDENTIFICATION_SET);
		int blogCount = dashboardService.getBlogCount();
		int commentCount = dashboardService.getCommentCount();
		Map<String, List> categoryBlogCountList = dashboardService.getCategoryBlogCountList();
		Map<String, List> tagBlogCountList = dashboardService.getTagBlogCountList();

		Map<String, Object> map = new HashMap<>();
		map.put("pv", todayPV);
		map.put("uv", todayUV);
		map.put("blogCount", blogCount);
		map.put("commentCount", commentCount);
		map.put("category", categoryBlogCountList);
		map.put("tag", tagBlogCountList);
		return Result.ok("获取成功", map);
	}
}
