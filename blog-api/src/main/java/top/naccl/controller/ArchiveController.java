package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.ArchiveBlog;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 归档页面
 * @Author: Naccl
 * @Date: 2020-08-12
 */
@RestController
@RequestMapping("/")
public class ArchiveController {
	@Autowired
	BlogService blogService;

	/**
	 * 按年月分组归档博客 统计博客总数
	 *
	 * @return
	 */
	@GetMapping("/archives")
	public Result archives() {
		try {
			Map<String, List<ArchiveBlog>> archiveBlogMap = blogService.getArchiveBlogList();
			Integer count = blogService.countBlog();
			Map<String, Object> map = new HashMap<>();
			map.put("blogMap", archiveBlogMap);
			map.put("count", count);
			return Result.ok("请求成功", map);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
