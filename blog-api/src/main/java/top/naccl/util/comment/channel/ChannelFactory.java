package top.naccl.util.comment.channel;

import top.naccl.constant.CommentConstants;
import top.naccl.util.common.SpringContextUtils;

/**
 * 评论提醒方式
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
public class ChannelFactory {
	/**
	 * 创建评论提醒方式
	 *
	 * @param channelName 方式名称
	 * @return
	 */
	public static CommentNotifyChannel getChannel(String channelName) {
		switch (channelName) {
			case CommentConstants.NONE:
				return SpringContextUtils.getBean(NoneChannel.class);
			case CommentConstants.TELEGRAM:
				return SpringContextUtils.getBean(TelegramChannel.class);
			case CommentConstants.MAIL:
				return SpringContextUtils.getBean(MailChannel.class);
			default:
				throw new RuntimeException("Unsupported value in [application.properties]: [comment.notify.channel]");
		}
	}
}
