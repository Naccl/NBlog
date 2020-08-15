package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.PageComment;
import top.naccl.model.vo.Result;
import top.naccl.service.CommentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 评论
 * @Author: Naccl
 * @Date: 2020-08-15
 */
@RestController
@RequestMapping("/")
public class CommentController {
	@Autowired
	CommentService commentService;

	@GetMapping("/comments")
	public Result comments(@RequestParam Integer page,
	                       @RequestParam(defaultValue = "") Long blogId,
	                       @RequestParam(defaultValue = "1") Integer pageNum,
	                       @RequestParam(defaultValue = "10") Integer pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		Integer count = commentService.countByPageAndIsPublished(page, blogId);
		List<PageComment> comments = commentService.getPageCommentList(page, blogId, (long) -1);
		Map<String, Object> map = new HashMap<>();
		map.put("count", count);
		map.put("comments", comments);
		return Result.ok("获取成功", map);
	}
}