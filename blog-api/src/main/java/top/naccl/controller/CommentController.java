package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.annotation.AccessLimit;
import top.naccl.constant.JwtConstants;
import top.naccl.entity.User;
import top.naccl.model.dto.Comment;
import top.naccl.model.vo.PageComment;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.CommentService;
import top.naccl.service.impl.UserServiceImpl;
import top.naccl.util.JwtUtils;
import top.naccl.util.StringUtils;
import top.naccl.util.comment.CommentUtils;
import top.naccl.enums.CommentOpenStateEnum;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 评论
 * @Author: Naccl
 * @Date: 2020-08-15
 */
@RestController
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	CommentUtils commentUtils;

	/**
	 * 根据页面分页查询评论列表
	 *
	 * @param page     页面分类（0普通文章，1关于我...）
	 * @param blogId   如果page==0，需要博客id参数
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @param jwt      若文章受密码保护，需要获取访问Token
	 * @return
	 */
	@GetMapping("/comments")
	public Result comments(@RequestParam Integer page,
	                       @RequestParam(defaultValue = "") Long blogId,
	                       @RequestParam(defaultValue = "1") Integer pageNum,
	                       @RequestParam(defaultValue = "10") Integer pageSize,
	                       @RequestHeader(value = "Authorization", defaultValue = "") String jwt) {
		CommentOpenStateEnum openState = commentUtils.judgeCommentState(page, blogId);
		switch (openState) {
			case NOT_FOUND:
				return Result.create(404, "该博客不存在");
			case CLOSE:
				return Result.create(403, "评论已关闭");
			case PASSWORD:
				//文章受密码保护，需要验证Token
				if (JwtUtils.judgeTokenIsExist(jwt)) {
					try {
						String subject = JwtUtils.getTokenBody(jwt).getSubject();
						if (subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
							//博主身份Token
							String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
							User admin = (User) userService.loadUserByUsername(username);
							if (admin == null) {
								return Result.create(403, "博主身份Token已失效，请重新登录！");
							}
						} else {
							//经密码验证后的Token
							Long tokenBlogId = Long.parseLong(subject);
							//博客id不匹配，验证不通过，可能博客id改变或客户端传递了其它密码保护文章的Token
							if (!tokenBlogId.equals(blogId)) {
								return Result.create(403, "Token不匹配，请重新验证密码！");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						return Result.create(403, "Token已失效，请重新验证密码！");
					}
				} else {
					return Result.create(403, "此文章受密码保护，请验证密码！");
				}
				break;
			default:
				break;
		}
		//查询该页面所有评论的数量
		Integer allComment = commentService.countByPageAndIsPublished(page, blogId, null);
		//查询该页面公开评论的数量
		Integer openComment = commentService.countByPageAndIsPublished(page, blogId, true);
		PageHelper.startPage(pageNum, pageSize);
		PageInfo<PageComment> pageInfo = new PageInfo<>(commentService.getPageCommentList(page, blogId, -1L));
		PageResult<PageComment> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
		Map<String, Object> map = new HashMap<>(8);
		map.put("allComment", allComment);
		map.put("closeComment", allComment - openComment);
		map.put("comments", pageResult);
		return Result.ok("获取成功", map);
	}

	/**
	 * 提交评论 又长又臭 能用就不改了:)
	 * 单个ip，30秒内允许提交1次评论
	 *
	 * @param comment 评论DTO
	 * @param request 获取ip
	 * @param jwt     博主身份Token
	 * @return
	 */
	@AccessLimit(seconds = 30, maxCount = 1, msg = "30秒内只能提交一次评论")
	@PostMapping("/comment")
	public Result postComment(@RequestBody Comment comment,
	                          HttpServletRequest request,
	                          @RequestHeader(value = "Authorization", defaultValue = "") String jwt) {
		//评论内容合法性校验
		if (StringUtils.isEmpty(comment.getContent()) || comment.getContent().length() > 250 ||
				comment.getPage() == null || comment.getParentCommentId() == null) {
			return Result.error("参数有误");
		}
		//是否访客的评论
		boolean isVisitorComment = false;
		//父评论
		top.naccl.entity.Comment parentComment = null;
		//对于有指定父评论的评论，应该以父评论为准，只判断页面可能会被绕过“评论开启状态检测”
		if (comment.getParentCommentId() != -1) {
			//当前评论为子评论
			parentComment = commentService.getCommentById(comment.getParentCommentId());
			Integer page = parentComment.getPage();
			Long blogId = page == 0 ? parentComment.getBlog().getId() : null;
			comment.setPage(page);
			comment.setBlogId(blogId);
		} else {
			//当前评论为顶级评论
			if (comment.getPage() != 0) {
				comment.setBlogId(null);
			}
		}
		//判断是否可评论
		CommentOpenStateEnum openState = commentUtils.judgeCommentState(comment.getPage(), comment.getBlogId());
		switch (openState) {
			case NOT_FOUND:
				return Result.create(404, "该博客不存在");
			case CLOSE:
				return Result.create(403, "评论已关闭");
			case PASSWORD:
				//文章受密码保护
				//验证Token合法性
				if (JwtUtils.judgeTokenIsExist(jwt)) {
					String subject;
					try {
						subject = JwtUtils.getTokenBody(jwt).getSubject();
					} catch (Exception e) {
						e.printStackTrace();
						return Result.create(403, "Token已失效，请重新验证密码！");
					}
					//博主评论，不受密码保护限制，根据博主信息设置评论属性
					if (subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
						//Token验证通过，获取Token中用户名
						String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
						User admin = (User) userService.loadUserByUsername(username);
						if (admin == null) {
							return Result.create(403, "博主身份Token已失效，请重新登录！");
						}
						commentUtils.setAdminComment(comment, request, admin);
						isVisitorComment = false;
					} else {//普通访客经文章密码验证后携带Token
						//对访客的评论昵称、邮箱合法性校验
						if (StringUtils.isEmpty(comment.getNickname(), comment.getEmail()) || comment.getNickname().length() > 15) {
							return Result.error("参数有误");
						}
						//对于受密码保护的文章，则Token是必须的
						Long tokenBlogId = Long.parseLong(subject);
						//博客id不匹配，验证不通过，可能博客id改变或客户端传递了其它密码保护文章的Token
						if (!tokenBlogId.equals(comment.getBlogId())) {
							return Result.create(403, "Token不匹配，请重新验证密码！");
						}
						commentUtils.setVisitorComment(comment, request);
						isVisitorComment = true;
					}
				} else {//不存在Token则无评论权限
					return Result.create(403, "此文章受密码保护，请验证密码！");
				}
				break;
			case OPEN:
				//评论正常开放
				//有Token则为博主评论，或文章原先为密码保护，后取消保护，但客户端仍存在Token
				if (JwtUtils.judgeTokenIsExist(jwt)) {
					String subject;
					try {
						subject = JwtUtils.getTokenBody(jwt).getSubject();
					} catch (Exception e) {
						e.printStackTrace();
						return Result.create(403, "Token已失效，请重新验证密码");
					}
					//博主评论，根据博主信息设置评论属性
					if (subject.startsWith(JwtConstants.ADMIN_PREFIX)) {
						//Token验证通过，获取Token中用户名
						String username = subject.replace(JwtConstants.ADMIN_PREFIX, "");
						User admin = (User) userService.loadUserByUsername(username);
						if (admin == null) {
							return Result.create(403, "博主身份Token已失效，请重新登录！");
						}
						commentUtils.setAdminComment(comment, request, admin);
						isVisitorComment = false;
					} else {//文章原先为密码保护，后取消保护，但客户端仍存在Token，则忽略Token
						//对访客的评论昵称、邮箱合法性校验
						if (StringUtils.isEmpty(comment.getNickname(), comment.getEmail()) || comment.getNickname().length() > 15) {
							return Result.error("参数有误");
						}
						commentUtils.setVisitorComment(comment, request);
						isVisitorComment = true;
					}
				} else {
					//访客评论
					//对访客的评论昵称、邮箱合法性校验
					if (StringUtils.isEmpty(comment.getNickname(), comment.getEmail()) || comment.getNickname().length() > 15) {
						return Result.error("参数有误");
					}
					commentUtils.setVisitorComment(comment, request);
					isVisitorComment = true;
				}
				break;
			default:
				break;
		}
		commentService.saveComment(comment);
		commentUtils.judgeSendNotify(comment, isVisitorComment, parentComment);
		return Result.ok("评论成功");
	}
}