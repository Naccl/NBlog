package top.naccl.enums;

/**
 * 访问行为枚举类
 *
 * @author: Naccl
 * @date: 2022-01-08
 */
public enum VisitBehavior {
	UNKNOWN("UNKNOWN", "UNKNOWN"),

	INDEX("访问页面", "首页"),
	ARCHIVE("访问页面", "归档"),
	MOMENT("访问页面", "动态"),
	FRIEND("访问页面", "友链"),
	ABOUT("访问页面", "关于我"),

	BLOG("查看博客", ""),
	CATEGORY("查看分类", ""),
	TAG("查看标签", ""),
	SEARCH("搜索博客", ""),
	CLICK_FRIEND("点击友链", ""),
	LIKE_MOMENT("点赞动态", ""),
	CHECK_PASSWORD("校验博客密码", ""),
	;

	/**
	 * 访问行为
	 */
	private String behavior;
	/**
	 * 访问内容
	 */
	private String content;

	VisitBehavior(String behavior, String content) {
		this.behavior = behavior;
		this.content = content;
	}

	public String getBehavior() {
		return behavior;
	}

	public String getContent() {
		return content;
	}
}
