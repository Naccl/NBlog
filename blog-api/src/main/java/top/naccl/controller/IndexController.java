package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.NewBlog;
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
@RequestMapping
public class IndexController {
	@Autowired
	SiteSettingService siteSettingService;
	@Autowired
	BlogService blogService;

	/**
	 * 获取站点配置信息 和 最新推荐博客
	 *
	 * @return
	 */
	@GetMapping("/site")
	public Result site() {
		Map<String, Object> map = siteSettingService.getSiteInfo();
		PageHelper.startPage(1, 3);
		List<NewBlog> newBlogList = blogService.getNewBlogListByIsPublishedAndIsRecommend();
		map.put("newBlogList", newBlogList);
		return Result.ok("请求成功", map);
	}
}
