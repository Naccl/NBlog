package top.naccl.service;

import top.naccl.entity.Blog;
import top.naccl.model.dto.BlogVisibility;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.NewBlog;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.RandomBlog;
import top.naccl.model.vo.SearchBlog;

import java.util.List;
import java.util.Map;

public interface BlogService {
	List<Blog> getListByTitleAndCategoryId(String title, Integer categoryId);

	List<SearchBlog> getSearchBlogListByQueryAndIsPublished(String query);

	List<Blog> getIdAndTitleList();

	List<NewBlog> getNewBlogListByIsPublished();

	PageResult<BlogInfo> getBlogInfoListByIsPublished(Integer pageNum);

	PageResult<BlogInfo> getBlogInfoListByCategoryNameAndIsPublished(String categoryName, Integer pageNum);

	PageResult<BlogInfo> getBlogInfoListByTagNameAndIsPublished(String tagName, Integer pageNum);

	Map<String, Object> getArchiveBlogAndCountByIsPublished();

	List<RandomBlog> getRandomBlogListByLimitNumAndIsPublishedAndIsRecommend();

	void deleteBlogById(Long id);

	void deleteBlogTagByBlogId(Long blogId);

	void saveBlog(top.naccl.model.dto.Blog blog);

	void saveBlogTag(Long blogId, Long tagId);

	void updateBlogRecommendById(Long blogId, Boolean recommend);

	void updateBlogVisibilityById(Long blogId, BlogVisibility blogVisibility);

	void updateBlogTopById(Long blogId, Boolean top);

	void updateViewsToRedis(Long blogId);

	void updateViews(Long blogId, Integer views);

	Blog getBlogById(Long id);

	String getTitleByBlogId(Long id);

	BlogDetail getBlogByIdAndIsPublished(Long id);

	String getBlogPassword(Long blogId);

	void updateBlog(top.naccl.model.dto.Blog blog);

	int countBlogByIsPublished();

	int countBlogByCategoryId(Long categoryId);

	int countBlogByTagId(Long tagId);

	Boolean getCommentEnabledByBlogId(Long blogId);

	Boolean getPublishedByBlogId(Long blogId);
}
