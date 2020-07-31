package top.naccl.service;

import top.naccl.entity.Blog;

import java.util.List;

public interface BlogService {
	List<Blog> getListByTitleOrCategory(String query, Integer CategoryId);

	int deleteBlogById(Long id);

	int deleteBlogTagByBlogId(Long blogId);

	int saveBlog(Blog blog);

	int saveBlogTag(Long blogId, Long tagId);
}
