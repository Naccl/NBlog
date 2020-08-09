package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Blog;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;
import top.naccl.service.SiteSettingService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 站点相关
 * @Author: Naccl
 * @Date: 2020-08-09
 */

@RestController
@RequestMapping("/")
public class IndexController {
	@Autowired
	SiteSettingService siteSettingService;
	@Autowired
	BlogService blogService;

	@GetMapping("/site")
	public Result site() {
		try {
			Map<String, Object> map = siteSettingService.getSiteInfo();
			PageHelper.startPage(1,3);
			List<Blog> newBLogList = blogService.getIdAndTitleListByIsPublishedAndIsRecommend();
			map.put("newBlogList", newBLogList);
			return Result.ok("请求成功", map);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
