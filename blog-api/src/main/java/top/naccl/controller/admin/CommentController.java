package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.entity.Blog;
import top.naccl.model.entity.Comment;
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

	/**
	 * 按页面和博客id分页查询评论List
	 *
	 * @param page     要查询的页面(博客文章or关于我...)
	 * @param blogId   如果是博客文章页面 需要提供博客id
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @return
	 */
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

	/**
	 * 获取所有博客id和title 供评论分类的选择
	 *
	 * @return
	 */
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

	/**
	 * 更新评论公开状态
	 *
	 * @param id        评论id
	 * @param published 是否公开
	 * @return
	 */
	@PutMapping("/comment/published")
	public Result updatePublished(@RequestParam Long id, @RequestParam Boolean published) {
		try {
			int r = commentService.updateCommentPublishedById(id, published);
			if (r == 1) {
				return Result.ok("操作成功");
			} else {
				return Result.error("操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 更新评论接收邮件提醒状态
	 *
	 * @param id     评论id
	 * @param notice 是否接收提醒
	 * @return
	 */
	@PutMapping("/comment/notice")
	public Result updateNotice(@RequestParam Long id, @RequestParam Boolean notice) {
		try {
			int r = commentService.updateCommentNoticeById(id, notice);
			if (r == 1) {
				return Result.ok("操作成功");
			} else {
				return Result.error("操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 按id删除该评论及其所有子评论
	 *
	 * @param id 评论id
	 * @return
	 */
	@DeleteMapping("/comment")
	public Result delete(@RequestParam Long id) {
		try {
			int r = commentService.deleteCommentById(id);
			if (r == 1) {
				return Result.ok("操作成功");
			} else {
				return Result.error("操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
