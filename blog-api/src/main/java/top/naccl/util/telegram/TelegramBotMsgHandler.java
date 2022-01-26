package top.naccl.util.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.naccl.config.properties.BlogProperties;
import top.naccl.config.properties.TelegramProperties;
import top.naccl.entity.Comment;
import top.naccl.entity.Moment;
import top.naccl.enums.CommentPageEnum;
import top.naccl.service.CommentService;
import top.naccl.service.MomentService;
import top.naccl.util.comment.CommentUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

/**
 * TelegramBot命令处理
 *
 * @author: Naccl
 * @date: 2022-01-25
 */
@Slf4j
@Lazy
@Component
public class TelegramBotMsgHandler {
	@Autowired
	private CommentService commentService;
	@Autowired
	private MomentService momentService;
	@Autowired
	private TelegramUtils telegramUtils;
	@Autowired
	private CommentUtils commentUtils;
	@Autowired
	private BlogProperties blogProperties;
	@Autowired
	private TelegramProperties telegramProperties;

	private SimpleDateFormat simpleDateFormat;

	private static final String CONTENT_UNKNOWN = "";

	private static final String CMD_HELP = "/help";
	private static final String CMD_HIDE = "/hide";
	private static final String CMD_SHOW = "/show";
	private static final String CMD_DEL = "/del";
	private static final String CMD_REPLY = "/reply";
	private static final String CMD_MOMENT = "/moment";

	private static final String HELP_MESSAGE = "<b>可用命令如下：</b>\n" +
			"\n" +
			"/help - 显示此帮助信息\n" +
			"/hide [评论ID] - 隐藏评论\n" +
			"/show [评论ID] - 公开评论\n" +
			"/del [评论ID] - 删除评论(将删除该评论下的所有子评论)\n" +
			"/reply [评论ID] [内容] - 回复评论\n" +
			"/moment [内容] - 发布动态\n" +
			"\n" +
			"例如：/reply 123 好！";

	public TelegramBotMsgHandler() {
		this.simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		this.simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}

	/**
	 * 处理指令
	 *
	 * @param message 消息内容
	 */
	public void processCommand(String message) {
		log.debug("message content: {}", message);

		String cmd = message;
		String content = CONTENT_UNKNOWN;
		int splitIndex = message.indexOf(" ");
		if (splitIndex != -1) {
			cmd = message.substring(0, splitIndex);
			if (message.length() > splitIndex + 1) {
				content = message.substring(splitIndex + 1);
			}
		}
		log.debug("cmd: {}, content: {}", cmd, content);

		String result = "";
		try {
			switch (cmd) {
				//按id隐藏评论
				case CMD_HIDE: {
					result = updateCommentStatus(content, false);
					break;
				}
				//按id公开评论
				case CMD_SHOW: {
					result = updateCommentStatus(content, true);
					break;
				}
				//按id删除评论
				case CMD_DEL: {
					result = deleteComment(content);
					break;
				}
				//快捷回复评论
				case CMD_REPLY: {
					result = replyComment(content);
					break;
				}
				//发动态
				case CMD_MOMENT: {
					result = newMoment(content);
					break;
				}
				//显示帮助
				case CMD_HELP: {
					result = HELP_MESSAGE;
					break;
				}
				//未知命令，显示帮助
				default: {
					result = "<b>未知命令</b>\n\n" + HELP_MESSAGE;
					break;
				}
			}
		} catch (Exception e) {
			log.error("processCommand catch exception: {}", e);
			handleCommandException(e);
		}
		sendResult(result);
	}

	/**
	 * 修改评论公开状态
	 *
	 * @param msgContent 命令后的消息内容
	 * @param status     是否公开
	 * @return
	 */
	private String updateCommentStatus(String msgContent, boolean status) {
		long commentId = Long.parseLong(msgContent);
		//先查一遍看看是否存在，不存在的话可以catch到提示信息
		commentService.getCommentById(commentId);
		//更新评论状态
		commentService.updateCommentPublishedById(commentId, status);
		return String.format(
				"<b>操作成功！</b>\n" +
						"\n" +
						"评论ID：%d\n" +
						"状态：%s",
				commentId,
				status ? "公开" : "隐藏"
		);
	}

	/**
	 * 删除评论及其子评论
	 *
	 * @param msgContent 命令后的消息内容
	 * @return
	 */
	private String deleteComment(String msgContent) {
		long commentId = Long.parseLong(msgContent);
		//先查一遍看看是否存在，不存在的话可以catch到提示信息
		commentService.getCommentById(commentId);
		//删除评论及其子评论
		commentService.deleteCommentById(commentId);
		return String.format(
				"<b>删除成功！</b>\n" +
						"\n" +
						"评论ID：%d\n" +
						"该评论及其子评论已删除",
				commentId
		);
	}

	/**
	 * 快捷回复评论
	 *
	 * @param msgContent 命令后的消息内容
	 * @return
	 */
	private String replyComment(String msgContent) {
		//[评论ID] 和 [回复内容] 之间的分隔符
		int splitIndex2 = msgContent.indexOf(" ");
		if (splitIndex2 != -1 && msgContent.length() > splitIndex2 + 1) {
			String commentIdString = msgContent.substring(0, splitIndex2);
			String commentContent = msgContent.substring(splitIndex2 + 1);

			long commentId = Long.parseLong(commentIdString);
			//先找到要回复的评论
			Comment parentComment = commentService.getCommentById(commentId);

			top.naccl.model.dto.Comment comment = new top.naccl.model.dto.Comment();
			comment.setContent(commentContent);
			comment.setParentCommentId(parentComment.getId());
			//父评论所在页面
			Integer page = parentComment.getPage();
			Long blogId = page == 0 ? parentComment.getBlog().getId() : null;
			comment.setPage(page);
			comment.setBlogId(blogId);
			commentUtils.setAdminCommentByTelegramAction(comment);

			//保存评论
			commentService.saveComment(comment);
			//提醒回复对象
			commentUtils.judgeSendNotify(comment, false, parentComment);

			CommentPageEnum commentPageEnum = CommentUtils.getCommentPageEnum(comment);
			return String.format(
					"<b>回复成功！</b>\n" +
							"\n" +
							"<b>您在<a href=\"%s\">《%s》</a>给 <b>%s</b> 的快捷回复：</b>\n" +
							"\n" +
							"<pre>%s</pre>\n" +
							"\n" +
							"<b>其他信息：</b>\n" +
							"评论ID：<code>%d</code>\n" +
							"IP：%s\n" +
							"时间：<u>%s</u>\n" +
							"邮箱：<code>%s</code>\n" +
							"状态：%s [<a href=\"%s\">管理评论</a>]\n",
					blogProperties.getView() + commentPageEnum.getPath(),
					commentPageEnum.getTitle(),
					parentComment.getNickname(),
					comment.getContent(),
					comment.getId(),
					comment.getIp(),
					simpleDateFormat.format(comment.getCreateTime()),
					comment.getEmail(),
					comment.getPublished() ? "公开" : "待审核",
					blogProperties.getCms() + "/blog/comment/list"
			);
		}
		return "<b>命令格式有误！</b>\n\n" + HELP_MESSAGE;
	}

	/**
	 * 发布新动态
	 *
	 * @param content 动态内容
	 */
	private String newMoment(String content) {
		Moment moment = new Moment();
		moment.setCreateTime(new Date());
		moment.setContent(content);
		moment.setLikes(0);
		moment.setPublished(true);
		momentService.saveMoment(moment);

		return String.format(
				"<b>动态发布成功！\n</b>" +
						"\n" +
						"<pre>%s</pre>\n" +
						"\n" +
						"<b>其他信息：</b>\n" +
						"时间：<u>%s</u>\n" +
						"状态：%s [<a href=\"%s\">管理动态</a>]\n",
				content,
				simpleDateFormat.format(moment.getCreateTime()),
				moment.getPublished() ? "公开" : "隐藏",
				blogProperties.getCms() + "/blog/moment/list"
		);
	}

	/**
	 * 捕获命令处理过程中的全部异常信息并提示
	 *
	 * @param e 异常
	 */
	private void handleCommandException(Exception e) {
		String result = String.format(
				"<b>异常错误：</b>\n" +
						"exception: %s\n" +
						"message: %s",
				e.getClass(),
				e.getMessage()
		);
		sendResult(result);
	}

	/**
	 * 发送命令处理结果
	 *
	 * @param message 处理结果
	 */
	private void sendResult(String message) {
		String url = telegramProperties.getApi() + telegramProperties.getToken() + TelegramUtils.SEND_MESSAGE;
		Map<String, Object> messageBody = telegramUtils.getMessageBody(message);
		telegramUtils.sendByAutoCheckReverseProxy(url, messageBody);
	}
}
