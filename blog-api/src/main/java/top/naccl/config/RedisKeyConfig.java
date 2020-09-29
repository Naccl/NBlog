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

	//分类名列表
	public static final String CATEGORY_NAME_LIST = "categoryNameList";
}
