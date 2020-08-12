package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.entity.Blog;
import top.naccl.mapper.BlogMapper;
import top.naccl.model.vo.ArchiveBlog;
import top.naccl.model.vo.BlogInfo;
import top.naccl.service.BlogService;
import top.naccl.service.TagService;
import top.naccl.util.markdown.MarkdownUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 博客文章业务层实现
 * @Author: Naccl
 * @Date: 2020-07-29
 */
@Service
public class BlogServiceImpl implements BlogService {
	@Autowired
	BlogMapper blogMapper;
	@Autowired
	TagService tagService;

	@Override
	public List<Blog> getListByTitleOrCategoryId(String title, Integer CategoryId) {
		return blogMapper.getListByTitleOrCategoryId(title, CategoryId);
	}

	@Override
	public List<Blog> getIdAndTitleList() {
		return blogMapper.getIdAndTitleList();
	}

	@Override
	public List<Blog> getIdAndTitleListByIsPublishedAndIsRecommend() {
		return blogMapper.getIdAndTitleListByIsPublishedAndIsRecommend();
	}

	@Override
	public List<BlogInfo> getBlogInfoListByIsPublished() {
		List<BlogInfo> blogInfos = blogMapper.getBlogInfoListByIsPublished();
		for (BlogInfo blogInfo : blogInfos) {
			blogInfo.setDescription(MarkdownUtils.markdownToHtmlExtensions(blogInfo.getDescription()));
			blogInfo.setTags(tagService.getTagListByBlogId(blogInfo.getId()));
		}
		return blogInfos;
	}

	@Override
	public Map<String, List<ArchiveBlog>> getArchiveBlogMapByIsPublished() {
		List<String> groupYearMonth = blogMapper.getGroupYearMonthByIsPublished();
		Map<String, List<ArchiveBlog>> map = new LinkedHashMap<>();
		for (String s : groupYearMonth) {
			map.put(s, blogMapper.getArchiveBlogListByYearMonthAndIsPublished(s));
		}
		return map;
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

	@Transactional
	@Override
	public int updateBlogTopById(Long blogId, Boolean top) {
		return blogMapper.updateBlogTopById(blogId, top);
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
	public int countBlog() {
		return blogMapper.countBlog();
	}

	@Override
	public int countBlogByIsPublished() {
		return blogMapper.countBlogByIsPublished();
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
