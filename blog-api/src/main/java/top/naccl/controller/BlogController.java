package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.RandomBlog;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;

import java.util.List;

/**
 * @Description: 博客相关
 * @Author: Naccl
 * @Date: 2020-08-12
 */
@RestController
@RequestMapping
public class BlogController {
	@Autowired
	BlogService blogService;

	/**
	 * 按置顶、创建时间排序 分页查询博客简要信息列表
	 *
	 * @param pageNum 页码
	 * @return
	 */
	@GetMapping("/blogs")
	public Result blogs(@RequestParam(defaultValue = "1") Integer pageNum) {
		int pageSize = 5;//每页显示5条
		String orderBy = "is_top desc, create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<BlogInfo> pageInfo = new PageInfo<>(blogService.getBlogInfoListByIsPublished());
		PageResult<BlogInfo> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
		return Result.ok("请求成功", pageResult);
	}

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

	/**
	 * 获取随机公开文章
	 *
	 * @return
	 */
	@GetMapping("/randomBlogs")
	public Result randomBlogs() {
		int limitNum = 5;
		List<RandomBlog> randomBlogs = blogService.getRandomBlogListByLimitNumAndIsPublished(limitNum);
		return Result.ok("获取成功", randomBlogs);
	}
}
