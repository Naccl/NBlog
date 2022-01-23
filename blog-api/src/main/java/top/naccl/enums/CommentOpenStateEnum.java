package top.naccl.enums;

/**
 * 评论开放状态枚举类
 *
 * @author: Naccl
 * @date: 2022-01-23
 */
public enum CommentOpenStateEnum {
	/**
	 * 博客不存在，或博客未公开
	 */
	NOT_FOUND,
	/**
	 * 评论正常开放
	 */
	OPEN,
	/**
	 * 评论已关闭
	 */
	CLOSE,
	/**
	 * 评论所在页面需要密码
	 */
	PASSWORD,
}
