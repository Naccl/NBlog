package top.naccl.util.comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import top.naccl.config.properties.BlogProperties;
import top.naccl.model.dto.Comment;
import top.naccl.service.BlogService;
import top.naccl.util.MailUtils;
import top.naccl.util.comment.channel.ChannelFactory;
import top.naccl.util.comment.channel.CommentNotifyChannel;

import java.util.HashMap;
import java.util.Map;

/**
 * 评论工具类
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
@Component
@DependsOn("springContextUtils")
public class CommentUtils {
	@Autowired
	private BlogProperties blogProperties;
	@Autowired
	private MailUtils mailUtils;

	private static BlogService blogService;

	private CommentNotifyChannel notifyChannel;

	@Autowired
	public void setBlogService(BlogService blogService) {
		CommentUtils.blogService = blogService;
	}

	@Value("${comment.notify.channel}")
	public void setNotifyChannel(String channelName) {
		this.notifyChannel = ChannelFactory.getChannel(channelName);
	}

	/**
	 * 判断是否发送提醒
	 * 6种情况：
	 * 1.我以父评论提交：不用提醒
	 * 2.我回复我自己：不用提醒
	 * 3.我回复访客的评论：只提醒该访客
	 * 4.访客以父评论提交：只提醒我自己
	 * 5.访客回复我的评论：只提醒我自己
	 * 6.访客回复访客的评论(即使是他自己先前的评论)：提醒我自己和他回复的评论
	 *
	 * @param comment          当前收到的评论
	 * @param isVisitorComment 是否访客评论
	 * @param parentComment    父评论
	 */
	public void judgeSendNotify(Comment comment, boolean isVisitorComment, top.naccl.entity.Comment parentComment) {
		if (parentComment != null && !parentComment.getAdminComment() && parentComment.getNotice()) {
			//我回复访客的评论，且对方接收提醒，邮件提醒对方(3)
			//访客回复访客的评论(即使是他自己先前的评论)，且对方接收提醒，邮件提醒对方(6)
			sendMailToParentComment(parentComment, comment);
		}
		if (isVisitorComment) {
			//访客以父评论提交，只提醒我自己(4)
			//访客回复我的评论，提醒我自己(5)
			//访客回复访客的评论，不管对方是否接收提醒，都要提醒我有新评论(6)
			notifyMyself(comment);
		}
	}

	/**
	 * 发送邮件提醒回复对象
	 *
	 * @param parentComment 父评论
	 * @param comment       当前收到的评论
	 */
	private void sendMailToParentComment(top.naccl.entity.Comment parentComment, Comment comment) {
		CommentPageEnum commentPageEnum = getCommentPageEnum(comment);
		Map<String, Object> map = new HashMap<>(7);
		map.put("parentNickname", parentComment.getNickname());
		map.put("nickname", comment.getNickname());
		map.put("title", commentPageEnum.getTitle());
		map.put("time", comment.getCreateTime());
		map.put("parentContent", parentComment.getContent());
		map.put("content", comment.getContent());
		map.put("url", blogProperties.getView() + commentPageEnum.getPath());
		String toAccount = parentComment.getEmail();
		String subject = "您在 " + blogProperties.getName() + " 的评论有了新回复";
		mailUtils.sendHtmlTemplateMail(map, toAccount, subject, "guest.html");
	}

	/**
	 * 通过指定方式通知自己
	 *
	 * @param comment 当前收到的评论
	 */
	private void notifyMyself(Comment comment) {
		notifyChannel.notifyMyself(comment);
	}

	/**
	 * 获取评论对应的页面
	 *
	 * @param comment 当前收到的评论
	 * @return
	 */
	public static CommentPageEnum getCommentPageEnum(Comment comment) {
		CommentPageEnum commentPageEnum = CommentPageEnum.UNKNOWN;
		switch (comment.getPage()) {
			case 0:
				//普通博客
				commentPageEnum = CommentPageEnum.BLOG;
				commentPageEnum.setTitle(blogService.getTitleByBlogId(comment.getBlogId()));
				commentPageEnum.setPath("/blog/" + comment.getBlogId());
				break;
			case 1:
				//关于我页面
				commentPageEnum = CommentPageEnum.ABOUT;
				break;
			case 2:
				//友链页面
				commentPageEnum = CommentPageEnum.FRIEND;
				break;
		}
		return commentPageEnum;
	}
}
