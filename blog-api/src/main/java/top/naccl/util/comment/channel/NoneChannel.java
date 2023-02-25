package top.naccl.util.comment.channel;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.naccl.model.dto.Comment;

/**
 * 无提醒方式
 *
 * @author: Naccl
 * @date: 2023-02-26
 */
@Lazy
@Component
public class NoneChannel implements CommentNotifyChannel {

	/**
	 * 什么也不做
	 *
	 * @param comment 当前收到的评论
	 */
	@Override
	public void notifyMyself(Comment comment) {

	}
}
