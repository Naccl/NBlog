package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;

/**
 * @Description: 博客相关
 * @Author: Naccl
 * @Date: 2020-08-12
 */
@RestController
@RequestMapping("/")
public class BlogController {
	@Autowired
	BlogService blogService;

	/**
	 * 按id获取公开博客详情
	 *
	 * @param id 博客id
	 * @return
	 */
	@GetMapping("/blog")
	public Result getBlog(@RequestParam Long id) {
		BlogDetail blog = blogService.getBlogByIdAndIsPublished(id);
		return Result.ok("获取成功", blog);
	}
}
