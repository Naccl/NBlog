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
import top.naccl.entity.User;
import top.naccl.model.dto.Comment;
import top.naccl.model.vo.PageComment;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.AboutService;
import top.naccl.service.BlogService;
import top.naccl.service.CommentService;
import top.naccl.service.impl.UserServiceImpl;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.JwtUtils;
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
	@Autowired
	AboutService aboutService;
	@Autowired
	UserServiceImpl userService;

	/**
	 * 根据页面分页查询评论列表
	 *
	 * @param page     页面分类（0普通文章，1关于我...）
	 * @param blogId   如果page==0，需要博客id参数
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @param request  若文章受密码保护，需要获取访问Token
	 * @return
	 */
	@GetMapping("/comments")
	public Result comments(@RequestParam Integer page,
	                       @RequestParam(defaultValue = "") Long blogId,
	                       @RequestParam(defaultValue = "1") Integer pageNum,
	                       @RequestParam(defaultValue = "10") Integer pageSize,
	                       HttpServletRequest request) {
		int judgeResult = judgeCommentEnabled(page, blogId);
		if (judgeResult == 2) {
			return Result.create(404, "该博客不存在");
		} else if (judgeResult == 1) {
			return Result.create(403, "评论已关闭");
		} else if (judgeResult == 3) {//文章受密码保护，需要验证Token
			String jwtToken = request.getHeader("Authorization");
			if (jwtToken != null && !"".equals(jwtToken) && !"null".equals(jwtToken)) {
				try {
					//获取Token中博客id
					String tokenBlogIdString = JwtUtils.validateToken(jwtToken);
					Long tokenBlogId = Long.parseLong(tokenBlogIdString);
					//博客id不匹配，验证不通过，可能博客id改变或客户端传递了其它密码保护文章的Token
					if (tokenBlogId != blogId) {
						return Result.create(403, "Token不匹配，请重新验证密码！");
					}
				} catch (Exception e) {
					e.printStackTrace();
					return Result.create(403, "Token已失效，请重新验证密码！");
				}
			} else {
				return Result.create(403, "此文章受密码保护，请验证密码！");
			}
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
	 * @param blogId 如果page==0，需要博客id参数，校验文章是否公开状态
	 * @return 0:公开可查询状态 1:评论关闭 2:该博客不存在 3:文章受密码保护
	 */
	private int judgeCommentEnabled(Integer page, Long blogId) {
		if (page == 0) {//普通博客
			Boolean commentEnabled = blogService.getCommentEnabledByBlogId(blogId);
			Boolean published = blogService.getPublishedByBlogId(blogId);
			if (commentEnabled == null || published == null) {//未查询到此博客
				return 2;
			} else if (!published) {//博客未公开
				return 2;
			} else if (!commentEnabled) {//博客评论已关闭
				return 1;
			}
			//判断文章是否存在密码
			String password = blogService.getBlogPassword(blogId);
			if (!"".equals(password)) {
				return 3;
			}
		} else if (page == 1) {//关于我页面
			if (!aboutService.getAboutCommentEnabled()) {//页面评论已关闭
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 提交评论
	 *
	 * @param comment 评论DTO
	 * @param request 用于获取ip和博主身份Token
	 * @return
	 */
	@PostMapping("/comment")
	public Result postComment(@RequestBody Comment comment, HttpServletRequest request) {
		//评论内容合法性校验
		if (StringUtils.isEmpty(comment.getContent()) || comment.getContent().length() > 250) {
			return Result.error("参数有误");
		}

		String jwtToken = request.getHeader("Authorization");
		//检测Token是否存在，存在则为博主评论
		if (jwtToken != null && !"".equals(jwtToken) && !"null".equals(jwtToken)) {
			try {
				setAdminComment(comment, request, jwtToken);
			} catch (Exception e) {
				e.printStackTrace();
				return Result.create(403, "博主身份Token已失效，请重新登录！");
			}
		} else {//不存在则为游客评论
			//评论昵称、邮箱合法性校验
			if (StringUtils.isEmpty(comment.getNickname(), comment.getEmail()) || comment.getNickname().length() > 15) {
				return Result.error("参数有误");
			}
			setVisitorComment(comment, request);
		}
		commentService.saveComment(comment);
		return Result.ok("评论成功");
	}

	/**
	 * 设置博主评论属性
	 *
	 * @param comment  评论DTO
	 * @param request  用于获取ip和博主身份Token
	 * @param jwtToken 请求头中的Token
	 */
	private void setAdminComment(Comment comment, HttpServletRequest request, String jwtToken) {
		String username = JwtUtils.validateToken(jwtToken);
		//Token验证通过，获取Token中用户名
		User admin = (User) userService.loadUserByUsername(username);
		comment.setAdminComment(true);
		comment.setCreateTime(new Date());
		comment.setPublished(true);
		comment.setAvatar(admin.getAvatar());
		comment.setWebsite("/");
		comment.setNickname(admin.getNickname());
		comment.setEmail(admin.getEmail());
		comment.setIp(IpAddressUtils.getIpAddress(request));
		comment.setNotice(false);
	}

	/**
	 * 设置游客评论属性
	 *
	 * @param comment 评论DTO
	 * @param request 用于获取ip
	 */
	private void setVisitorComment(Comment comment, HttpServletRequest request) {
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
		comment.setAdminComment(false);
		comment.setCreateTime(new Date());
		comment.setPublished(true);//默认不需要审核
		comment.setAvatar(avatar);
		comment.setWebsite(website);
		comment.setNickname(comment.getNickname().trim());
		comment.setEmail(comment.getEmail().trim());
		comment.setIp(IpAddressUtils.getIpAddress(request));
	}
}