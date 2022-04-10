package top.naccl.constant;

/**
 * @Description: Redis key配置
 * @Author: Naccl
 * @Date: 2020-09-27
 */
public class RedisKeyConstants {
	/**
	 * 首页博客简介列表 分页对象key
	 * homeBlogInfoList : {{1,"第一页的缓存"},{2,"第二页的缓存"}}
	 */
	public static final String HOME_BLOG_INFO_LIST = "homeBlogInfoList";
	/**
	 * 分类名列表key
	 */
	public static final String CATEGORY_NAME_LIST = "categoryNameList";
	/**
	 * 标签云列表key
	 */
	public static final String TAG_CLOUD_LIST = "tagCloudList";
	/**
	 * 站点信息key
	 */
	public static final String SITE_INFO_MAP = "siteInfoMap";
	/**
	 * 最新推荐博客key
	 */
	public static final String NEW_BLOG_LIST = "newBlogList";
	/**
	 * 关于我页面key
	 */
	public static final String ABOUT_INFO_MAP = "aboutInfoMap";
	/**
	 * 友链页面信息key
	 */
	public static final String FRIEND_INFO_MAP = "friendInfoMap";
	/**
	 * 博客归档key
	 */
	public static final String ARCHIVE_BLOG_MAP = "archiveBlogMap";
	/**
	 * 博客访问量key
	 */
	public static final String BLOG_VIEWS_MAP = "blogViewsMap";
	/**
	 * 访客标识码key
	 */
	public static final String IDENTIFICATION_SET = "identificationSet";
	/**
	 * QQ号与对应头像URL key
	 */
	public static final String QQ_AVATAR_URL_MAP = "qqAvatarUrlMap";
}
