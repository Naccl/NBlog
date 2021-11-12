package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.annotation.AccessLimit;
import top.naccl.entity.User;
import top.naccl.model.dto.Comment;
import top.naccl.model.vo.FriendInfo;
import top.naccl.model.vo.PageComment;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.AboutService;
import top.naccl.service.BlogService;
import top.naccl.service.CommentService;
import top.naccl.service.FriendService;
import top.naccl.service.impl.UserServiceImpl;
import top.naccl.util.HashUtils;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.JwtUtils;
import top.naccl.util.MailUtils;
import top.naccl.util.QQInfoUtils;
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
public class CommentController {
	@Autowired
	CommentService commentService;
	@Autowired
	BlogService blogService;
	@Autowired
	AboutService aboutService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	FriendService friendService;
	@Autowired
	MailProperties mailProperties;
	@Autowired
	MailUtils mailUtils;
	private String blogName;
	private String cmsUrl;
	private String websiteUrl;

	@Value("${custom.blog.name}")
	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}

	@Value("${custom.url.cms}")
	public void setCmsUrl(String cmsUrl) {
		this.cmsUrl = cmsUrl;
	}

	@Value("${custom.url.website}")
	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

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
		int judgeResult = judgeCommentEnabled(page, blogId);
		if (judgeResult == 2) {
			return Result.create(404, "该博客不存在");
		} else if (judgeResult == 1) {
			return Result.create(403, "评论已关闭");
		} else if (judgeResult == 3) {//文章受密码保护，需要验证Token
			if (JwtUtils.judgeTokenIsExist(jwt)) {
				try {
					String subject = JwtUtils.getTokenBody(jwt).getSubject();
					if (subject.startsWith("admin:")) {//博主身份Token
						String username = subject.replace("admin:", "");
						User admin = (User) userService.loadUserByUsername(username);
						if (admin == null) {
							return Result.create(403, "博主身份Token已失效，请重新登录！");
						}
					} else {//经密码验证后的Token
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
		}
		//查询该页面所有评论的数量
		Integer allComment = commentService.countByPageAndIsPublished(page, blogId, null);
		//查询该页面公开评论的数量
		Integer openComment = commentService.countByPageAndIsPublished(page, blogId, true);
		PageHelper.startPage(pageNum, pageSize);
		PageInfo<PageComment> pageInfo = new PageInfo<>(commentService.getPageCommentList(page, blogId, (long) -1));
		PageResult<PageComment> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
		Map<String, Object> map = new HashMap<>();
		map.put("allComment", allComment);
		map.put("closeComment", allComment - openComment);
		map.put("comments", pageResult);
		return Result.ok("获取成功", map);
	}

	/**
	 * 查询对应页面评论是否开启
	 *
	 * @param page   页面分类（0普通文章，1关于我，2友链）
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
		} else if (page == 2) {//友链页面
			FriendInfo friendInfo = friendService.getFriendInfo(true, false);
			if (!friendInfo.getCommentEnabled()) {
				return 1;
			}
		}
		return 0;
	}

	/**
	 * 提交评论 又长又臭 能用就不改了:) https://cdn.jsdelivr.net/gh/Naccl/blog-resource/img/1stLaw4Coding.jpg
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
		int judgeResult = judgeCommentEnabled(comment.getPage(), comment.getBlogId());
		if (judgeResult == 2) {
			return Result.create(404, "该博客不存在");
		} else if (judgeResult == 1) {
			return Result.create(403, "评论已关闭");
		} else if (judgeResult == 3) {//文章受密码保护
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
				if (subject.startsWith("admin:")) {
					//Token验证通过，获取Token中用户名
					String username = subject.replace("admin:", "");
					User admin = (User) userService.loadUserByUsername(username);
					if (admin == null) {
						return Result.create(403, "博主身份Token已失效，请重新登录！");
					}
					setAdminComment(comment, request, admin);
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
					setVisitorComment(comment, request);
					isVisitorComment = true;
				}
			} else {//不存在Token则无评论权限
				return Result.create(403, "此文章受密码保护，请验证密码！");
			}
		} else if (judgeResult == 0) {//普通文章
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
				if (subject.startsWith("admin:")) {
					//Token验证通过，获取Token中用户名
					String username = subject.replace("admin:", "");
					User admin = (User) userService.loadUserByUsername(username);
					if (admin == null) {
						return Result.create(403, "博主身份Token已失效，请重新登录！");
					}
					setAdminComment(comment, request, admin);
					isVisitorComment = false;
				} else {//文章原先为密码保护，后取消保护，但客户端仍存在Token，则忽略Token
					//对访客的评论昵称、邮箱合法性校验
					if (StringUtils.isEmpty(comment.getNickname(), comment.getEmail()) || comment.getNickname().length() > 15) {
						return Result.error("参数有误");
					}
					setVisitorComment(comment, request);
					isVisitorComment = true;
				}
			} else {//访客评论
				//对访客的评论昵称、邮箱合法性校验
				if (StringUtils.isEmpty(comment.getNickname(), comment.getEmail()) || comment.getNickname().length() > 15) {
					return Result.error("参数有误");
				}
				setVisitorComment(comment, request);
				isVisitorComment = true;
			}
		}
		commentService.saveComment(comment);
		judgeSendMail(comment, isVisitorComment, parentComment);
		return Result.ok("评论成功");
	}

	/**
	 * 设置博主评论属性
	 *
	 * @param comment 评论DTO
	 * @param request 获取ip
	 * @param admin   博主信息
	 */
	private void setAdminComment(Comment comment, HttpServletRequest request, User admin) {
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
	 * 设置访客评论属性
	 *
	 * @param comment 评论DTO
	 * @param request 用于获取ip
	 */
	private void setVisitorComment(Comment comment, HttpServletRequest request) {
		String commentNickname = comment.getNickname();
		try {
			if (QQInfoUtils.isQQNumber(commentNickname)) {
				comment.setQq(commentNickname);
				comment.setNickname(QQInfoUtils.getQQNickname(commentNickname));
				comment.setAvatar(QQInfoUtils.getQQAvatarURLByGithubUpload(commentNickname));
			} else {
				comment.setNickname(comment.getNickname().trim());
				setCommentRandomAvatar(comment);
			}
		} catch (Exception e) {
			e.printStackTrace();
			comment.setNickname(comment.getNickname().trim());
			setCommentRandomAvatar(comment);
		}

		//set website
		String website = comment.getWebsite().trim();
		if (!"".equals(website) && !website.startsWith("http://") && !website.startsWith("https://")) {
			website = "http://" + website;
		}
		comment.setAdminComment(false);
		comment.setCreateTime(new Date());
		comment.setPublished(true);//默认不需要审核
		comment.setWebsite(website);
		comment.setEmail(comment.getEmail().trim());
		comment.setIp(IpAddressUtils.getIpAddress(request));
	}

	/**
	 * 对于昵称不是QQ号的评论，根据昵称Hash设置头像
	 *
	 * @param comment 评论DTO
	 */
	private void setCommentRandomAvatar(Comment comment) {
		//设置随机头像
		long nicknameHash = HashUtils.getMurmurHash32(comment.getNickname());//根据评论昵称取Hash，保证每一个昵称对应一个头像
		long num = nicknameHash % 6 + 1;//计算对应的头像
		String avatar = "/img/comment-avatar/" + num + ".jpg";
		comment.setAvatar(avatar);
	}

	/**
	 * 判断是否发送邮件
	 * 6种情况：
	 * 1.我以父评论提交：不用邮件提醒
	 * 2.我回复我自己：不用邮件提醒
	 * 3.我回复访客的评论：只提醒该访客
	 * 4.访客以父评论提交：只提醒我自己
	 * 5.访客回复我的评论：只提醒我自己
	 * 6.访客回复访客的评论(即使是他自己先前的评论)：提醒我自己和他回复的评论
	 *
	 * @param comment          当前评论
	 * @param isVisitorComment 是否访客评论
	 * @param parentComment    父评论
	 */
	private void judgeSendMail(Comment comment, boolean isVisitorComment, top.naccl.entity.Comment parentComment) {
		if (parentComment != null && !parentComment.getAdminComment() && parentComment.getNotice()) {
			//我回复访客的评论，且对方接收提醒，邮件提醒对方(3)
			//访客回复访客的评论(即使是他自己先前的评论)，且对方接收提醒，邮件提醒对方(6)
			sendMailToParentComment(parentComment, comment);
		}
		if (isVisitorComment) {
			//访客以父评论提交，只邮件提醒我自己(4)
			//访客回复我的评论，邮件提醒我自己(5)
			//访客回复访客的评论，不管对方是否接收提醒，都要提醒我有新评论(6)
			sendMailToMe(comment);
		}
	}

	/**
	 * 发送邮件提醒回复对象
	 *
	 * @param parentComment 父评论
	 * @param comment       当前评论
	 */
	private void sendMailToParentComment(top.naccl.entity.Comment parentComment, Comment comment) {
		String path = "";
		String title = "";
		if (comment.getPage() == 0) {
			//普通博客
			title = parentComment.getBlog().getTitle();
			path = "/blog/" + comment.getBlogId();
		} else if (comment.getPage() == 1) {
			//关于我页面
			title = "关于我";
			path = "/about";
		} else if (comment.getPage() == 2) {
			//友链页面
			title = "友人帐";
			path = "/friends";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("parentNickname", parentComment.getNickname());
		map.put("nickname", comment.getNickname());
		map.put("title", title);
		map.put("time", comment.getCreateTime());
		map.put("parentContent", parentComment.getContent());
		map.put("content", comment.getContent());
		map.put("url", websiteUrl + path);
		String toAccount = parentComment.getEmail();
		String subject = "您在 " + blogName + " 的评论有了新回复";
		mailUtils.sendHtmlTemplateMail(map, toAccount, subject, "guest.html");
	}

	/**
	 * 发送邮件提醒我自己
	 *
	 * @param comment 当前评论
	 */
	private void sendMailToMe(Comment comment) {
		String path = "";
		String title = "";
		if (comment.getPage() == 0) {
			//普通博客
			title = blogService.getTitleByBlogId(comment.getBlogId());
			path = "/blog/" + comment.getBlogId();
		} else if (comment.getPage() == 1) {
			//关于我页面
			title = "关于我";
			path = "/about";
		} else if (comment.getPage() == 2) {
			//友链页面
			title = "友人帐";
			path = "/friends";
		}
		Map<String, Object> map = new HashMap<>();
		map.put("title", title);
		map.put("time", comment.getCreateTime());
		map.put("nickname", comment.getNickname());
		map.put("content", comment.getContent());
		map.put("ip", comment.getIp());
		map.put("email", comment.getEmail());
		map.put("status", comment.getPublished() ? "公开" : "待审核");
		map.put("url", websiteUrl + path);
		map.put("manageUrl", cmsUrl + "/comments");
		String toAccount = mailProperties.getUsername();
		String subject = blogName + " 收到新评论";
		mailUtils.sendHtmlTemplateMail(map, toAccount, subject, "owner.html");
	}
}