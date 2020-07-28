package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Blog;
import top.naccl.service.BlogService;
import top.naccl.util.Result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 博客文章后台管理
 * @Author: Naccl
 * @Date: 2020-07-29
 */
@RestController
@RequestMapping("/admin")
public class BlogController {
	@Autowired
	BlogService blogService;

	@GetMapping("/blogs")
	public Result blogs() {
		try {
			List<Blog> blogs = blogService.getBlogList();
			Map<String, Object> map = new HashMap<>();
			map.put("blogs", blogs);
			return Result.ok("请求成功", map);
		} catch (Exception e) {
			return Result.create(500, "异常错误");
		}
	}
}
