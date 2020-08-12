package top.naccl.service;

import top.naccl.entity.Blog;
import top.naccl.model.vo.ArchiveBlog;
import top.naccl.model.vo.BlogDetail;
import top.naccl.model.vo.BlogInfo;

import java.util.List;
import java.util.Map;

public interface BlogService {
	List<Blog> getListByTitleOrCategoryId(String title, Integer CategoryId);

	List<Blog> getIdAndTitleList();

	List<Blog> getIdAndTitleListByIsPublishedAndIsRecommend();

	List<BlogInfo> getBlogInfoListByIsPublished();

	Map<String, List<ArchiveBlog>> getArchiveBlogMapByIsPublished();

	int deleteBlogById(Long id);

	int deleteBlogTagByBlogId(Long blogId);

	int saveBlog(Blog blog);

	int saveBlogTag(Long blogId, Long tagId);

	int updateBlogRecommendById(Long BlogId, Boolean recommend);

	int updateBlogPublishedById(Long BlogId, Boolean published);

	int updateBlogTopById(Long blogId, Boolean top);

	Blog getBlogById(Long id);

	BlogDetail getBlogByIdAndIsPublished(Long id);

	int updateBlog(Blog blog);

	int countBlog();

	int countBlogByIsPublished();

	int countBlogByCategoryId(Long categoryId);

	int countBlogByTagId(Long tagId);
}
