package top.naccl.service;

import top.naccl.entity.Blog;
import top.naccl.model.vo.ArchiveBlog;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.BlogInfo;

import java.util.List;
import java.util.Map;

public interface BlogService {
	List<Blog> getListByTitleAndCategoryId(String title, Integer CategoryId);

	List<Blog> getIdAndTitleList();

	List<Blog> getIdAndTitleListByIsPublishedAndIsRecommend();

	List<BlogInfo> getBlogInfoListByIsPublished();

	Map<String, List<ArchiveBlog>> getArchiveBlogMapByIsPublished();

	void deleteBlogById(Long id);

	void deleteBlogTagByBlogId(Long blogId);

	void saveBlog(Blog blog);

	void saveBlogTag(Long blogId, Long tagId);

	void updateBlogRecommendById(Long BlogId, Boolean recommend);

	void updateBlogPublishedById(Long BlogId, Boolean published);

	void updateBlogTopById(Long blogId, Boolean top);

	Blog getBlogById(Long id);

	BlogDetail getBlogByIdAndIsPublished(Long id);

	void updateBlog(Blog blog);

	int countBlog();

	int countBlogByIsPublished();

	int countBlogByCategoryId(Long categoryId);

	int countBlogByTagId(Long tagId);

	Boolean getCommentEnabledByBlogId(Long blogId);
}
