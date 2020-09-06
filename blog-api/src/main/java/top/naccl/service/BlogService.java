package top.naccl.service;

import top.naccl.entity.Blog;
import top.naccl.model.dto.BlogVisibility;
import top.naccl.model.vo.ArchiveBlog;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.NewBlog;
import top.naccl.model.vo.RandomBlog;
import top.naccl.model.vo.SearchBlog;

import java.util.List;
import java.util.Map;

public interface BlogService {
	List<Blog> getListByTitleAndCategoryId(String title, Integer CategoryId);

	List<SearchBlog> getSearchBlogListByQueryAndIsPublished(String query);

	List<Blog> getIdAndTitleList();

	List<NewBlog> getNewBlogListByIsPublishedAndIsRecommend();

	List<BlogInfo> getBlogInfoListByIsPublished();

	List<BlogInfo> getBlogInfoListByCategoryNameAndIsPublished(String categoryName);

	List<BlogInfo> getBlogInfoListByTagNameAndIsPublished(String tagName);

	Map<String, List<ArchiveBlog>> getArchiveBlogMapByIsPublished();

	List<RandomBlog> getRandomBlogListByLimitNumAndIsPublished(Integer limitNum);

	void deleteBlogById(Long id);

	void deleteBlogTagByBlogId(Long blogId);

	void saveBlog(top.naccl.model.dto.Blog blog);

	void saveBlogTag(Long blogId, Long tagId);

	void updateBlogRecommendById(Long BlogId, Boolean recommend);

	void updateBlogVisibilityById(Long BlogId, BlogVisibility blogVisibility);

	void updateBlogTopById(Long blogId, Boolean top);

	int updateViews(Long blogId);

	Blog getBlogById(Long id);

	BlogDetail getBlogByIdAndIsPublished(Long id);

	String getBlogPassword(Long blogId);

	void updateBlog(top.naccl.model.dto.Blog blog);

	int countBlog();

	int countBlogByIsPublished();

	int countBlogByCategoryId(Long categoryId);

	int countBlogByTagId(Long tagId);

	Boolean getCommentEnabledByBlogId(Long blogId);

	Boolean getPublishedByBlogId(Long blogId);
}
