package top.naccl.config;

/**
 * @Description: Redis key配置
 * @Author: Naccl
 * @Date: 2020-09-27
 */
public class RedisKeyConfig {
	//首页博客简介列表 分页对象key：homeBlogInfoList : {{1,"第一页的缓存"},{2,"第二页的缓存"}}
	public static final String HOME_BLOG_INFO_LIST = "homeBlogInfoList";

	//按分类名查询博客简介列表 分页对象key前缀：categoryBlogInfoList_Vue : {{1,"第一页的缓存"},{2,"第二页的缓存"}}
	public static final String CATEGORY_BLOG_INFO_LIST = "categoryBlogInfoList_";

	//按标签名查询博客简介列表 分页对象key前缀：tagBlogInfoList_Vue : {{1,"第一页的缓存"},{2,"第二页的缓存"}}
	public static final String TAG_BLOG_INFO_LIST = "tagBlogInfoList_";

	//分类名列表key
	public static final String CATEGORY_NAME_LIST = "categoryNameList";

	//标签云列表key
	public static final String TAG_CLOUD_LIST = "tagCloudList";

	//站点信息key
	public static final String SITE_INFO = "siteInfo";

	//最新推荐博客key
	public static final String NEW_BLOG_LIST = "newBlogList";

	//关于我页面key
	public static final String ABOUT_INFO = "aboutInfo";

	//友链页面信息key
	public static final String FRIEND_INFO = "friendInfo";
}
