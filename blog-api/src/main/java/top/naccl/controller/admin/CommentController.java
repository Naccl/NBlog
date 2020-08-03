package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Blog;
import top.naccl.entity.Comment;
import top.naccl.service.BlogService;
import top.naccl.service.CommentService;
import top.naccl.util.Result;

import java.util.List;

/**
 * @Description: 博客评论后台管理
 * @Author: Naccl
 * @Date: 2020-08-03
 */
@RestController
@RequestMapping("/admin")
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	BlogService blogService;

	@GetMapping("/comments")
	public Result comments(@RequestParam(defaultValue = "0") Integer page,
	                       @RequestParam(defaultValue = "") Long blogId,
	                       @RequestParam(defaultValue = "1") Integer pageNum,
	                       @RequestParam(defaultValue = "10") Integer pageSize) {
		try {
			String orderBy = "create_time desc";
			PageHelper.startPage(pageNum, pageSize, orderBy);
			List<Comment> comments = commentService.getListByPageAndParentCommentId(page, blogId, (long) -1);
			PageInfo<Comment> pageInfo = new PageInfo<>(comments);
			return Result.ok("请求成功", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	@GetMapping("/blogIdAndTitle")
	public Result blogIdAndTitle() {
		try {
			List<Blog> blogs = blogService.getIdAndTitleList();
			return Result.ok("请求成功", blogs);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
