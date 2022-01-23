package top.naccl.enums;

/**
 * 评论页面枚举类
 *
 * @author: Naccl
 * @date: 2022-01-22
 */
public enum CommentPageEnum {
	UNKNOWN("UNKNOWN", "UNKNOWN"),

	BLOG("", ""),
	ABOUT("关于我", "/about"),
	FRIEND("友人帐", "/friends"),
	;

	private String title;
	private String path;

	CommentPageEnum(String title, String path) {
		this.title = title;
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
