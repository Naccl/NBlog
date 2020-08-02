package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.entity.Blog;
import top.naccl.mapper.BlogMapper;
import top.naccl.service.BlogService;

import java.util.List;

/**
 * @Description: 博客文章业务层实现
 * @Author: Naccl
 * @Date: 2020-07-29
 */
@Service
public class BlogServiceImpl implements BlogService {
	@Autowired
	BlogMapper blogMapper;

	@Override
	public List<Blog> getListByTitleOrCategory(String query, Integer CategoryId) {
		return blogMapper.getListByTitleOrCategory(query, CategoryId);
	}

	@Transactional
	@Override
	public int deleteBlogById(Long id) {
		return blogMapper.deleteBlogById(id);
	}

	@Transactional
	@Override
	public int deleteBlogTagByBlogId(Long blogId) {
		return blogMapper.deleteBlogTagByBlogId(blogId);
	}

	@Transactional
	@Override
	public int saveBlog(Blog blog) {
		return blogMapper.saveBlog(blog);
	}

	@Transactional
	@Override
	public int saveBlogTag(Long blogId, Long tagId) {
		return blogMapper.saveBlogTag(blogId, tagId);
	}

	@Transactional
	@Override
	public int updateBlogRecommendById(Long BlogId, Boolean recommend) {
		return blogMapper.updateBlogRecommendById(BlogId, recommend);
	}

	@Transactional
	@Override
	public int updateBlogPublishedById(Long BlogId, Boolean published) {
		return blogMapper.updateBlogPublishedById(BlogId, published);
	}

	@Override
	public Blog getBlogById(Long id) {
		return blogMapper.getBlogById(id);
	}

	@Transactional
	@Override
	public int updateBlog(Blog blog) {
		return blogMapper.updateBlog(blog);
	}

	@Override
	public int countBlogByCategoryId(Long categoryId) {
		return blogMapper.countBlogByCategoryId(categoryId);
	}

	@Override
	public int countBlogByTagId(Long tagId) {
		return blogMapper.countBlogByTagId(tagId);
	}
}
