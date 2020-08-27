package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.dto.Comment;
import top.naccl.model.vo.PageComment;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;
import top.naccl.service.CommentService;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.MD5Utils;
import top.naccl.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 评论
 * @Author: Naccl
 * @Date: 2020-08-15
 */
@RestController
@RequestMapping
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	BlogService blogService;

	/**
	 * 根据页面分页查询评论列表
	 *
	 * @param page     页面分类（0普通文章，1关于我...）
	 * @param blogId   如果page==0，需要博客id参数
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @return
	 */
	@GetMapping("/comments")
	public Result comments(@RequestParam Integer page,
	                       @RequestParam(defaultValue = "") Long blogId,
	                       @RequestParam(defaultValue = "1") Integer pageNum,
	                       @RequestParam(defaultValue = "10") Integer pageSize) {
		if (!judgeCommentEnabled(page, blogId)) {
			return Result.create(403, "评论已关闭");
		}
		Integer count = commentService.countByPageAndIsPublished(page, blogId);
		PageHelper.startPage(pageNum, pageSize);
		PageInfo<PageComment> pageInfo = new PageInfo<>(commentService.getPageCommentList(page, blogId, (long) -1));
		PageResult<PageComment> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
		Map<String, Object> map = new HashMap<>();
		map.put("count", count);
		map.put("comments", pageResult);
		return Result.ok("获取成功", map);
	}

	/**
	 * 查询对应页面评论是否开启
	 *
	 * @param page   页面分类（0普通文章，1关于我...）
	 * @param blogId 如果page==0，需要博客id参数
	 * @return
	 */
	private boolean judgeCommentEnabled(Integer page, Long blogId) {
		if (page == 0) {//普通博客
			return blogService.getCommentEnabledByBlogId(blogId);
		}
		return false;
	}


	/**
	 * 提交评论
	 *
	 * @param comment 评论DTO
	 * @param request 请求，用于获取ip和管理员身份（待添加）
	 * @return
	 */
	@PostMapping("/comment")
	public Result postComment(@RequestBody Comment comment, HttpServletRequest request) {
		if (StringUtils.isEmpty(comment.getNickname(), comment.getEmail(), comment.getContent())
				|| comment.getNickname().length() > 15 || comment.getContent().length() > 250) {
			return Result.error("参数有误");
		}

		//set 随机头像（获取QQ昵称头像功能待添加）
		String nicknameMD5 = MD5Utils.getMD5(comment.getNickname());//根据评论昵称取MD5，保证每一个昵称对应一个头像
		char m = nicknameMD5.charAt(nicknameMD5.length() - 1);//取MD5最后一位
		int num = m % 6 + 1;//计算对应的头像
		String avatar = num + ".jpg";

		//set website
		String website = comment.getWebsite().trim();
		if (!"".equals(website) && !website.startsWith("http://") && !website.startsWith("https://")) {
			website = "http://" + website;
		}

		comment.setAvatar(avatar);
		comment.setWebsite(website);
		comment.setNickname(comment.getNickname().trim());
		comment.setEmail(comment.getEmail().trim());
		comment.setPublished(true);//默认不需要审核
		comment.setAdminComment(false);//管理员登录功能待添加
		comment.setCreateTime(new Date());
		comment.setIp(IpAddressUtils.getIpAddress(request));

		commentService.saveComment(comment);
		return Result.ok("评论成功");
	}
}