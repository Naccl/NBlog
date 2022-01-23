package top.naccl.util.comment.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.naccl.config.properties.BlogProperties;
import top.naccl.config.properties.TelegramProperties;
import top.naccl.model.dto.Comment;
import top.naccl.util.TelegramUtils;
import top.naccl.util.comment.enums.CommentPageEnum;
import top.naccl.util.comment.CommentUtils;

import java.text.SimpleDateFormat;

/**
 * Telegram提醒方式
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
@Lazy
@Component
public class TelegramChannel implements CommentNotifyChannel {
	@Autowired
	private TelegramUtils telegramUtils;
	@Autowired
	private BlogProperties blogProperties;
	@Autowired
	private TelegramProperties telegramProperties;

	/**
	 * 发送Telegram消息提醒我自己
	 *
	 * @param comment 当前收到的评论
	 */
	@Override
	public void notifyMyself(Comment comment) {
		String content = getContent(comment);
		if (telegramProperties.getUseReverseProxy()) {
			telegramUtils.send2ReverseProxy(content);
		} else {
			telegramUtils.send2TelegramApi(content);
		}
	}

	private String getContent(Comment comment) {
		CommentPageEnum commentPageEnum = CommentUtils.getCommentPageEnum(comment);
		return String.format(
				"<b>您的文章<a href=\"%s\">《%s》</a>有了新的评论~</b>\n" +
						"\n" +
						"<b>%s</b> 给您的评论：\n" +
						"\n" +
						"<pre>%s</pre>\n" +
						"\n" +
						"<b>其他信息：</b>\n" +
						"IP：%s\n" +
						"时间：<u>%s</u>\n" +
						"邮箱：<code>%s</code>\n" +
						"状态：%s [<a href=\"%s\">管理评论</a>]\n",
				blogProperties.getView() + commentPageEnum.getPath(),
				commentPageEnum.getTitle(),
				comment.getNickname(),
				comment.getContent(),
				comment.getIp(),
				new SimpleDateFormat("yyyy-MM-dd hh:mm").format(comment.getCreateTime()),
				comment.getEmail(),
				comment.getPublished() ? "公开" : "待审核",
				blogProperties.getCms() + "/comments"
		);
	}
}
