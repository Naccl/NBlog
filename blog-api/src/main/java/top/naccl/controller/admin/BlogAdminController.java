package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.annotation.OperationLogger;
import top.naccl.entity.Blog;
import top.naccl.entity.Category;
import top.naccl.entity.Tag;
import top.naccl.entity.User;
import top.naccl.model.dto.BlogVisibility;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;
import top.naccl.service.CategoryService;
import top.naccl.service.CommentService;
import top.naccl.service.TagService;
import top.naccl.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 博客文章后台管理
 * @Author: Naccl
 * @Date: 2020-07-29
 */
@RestController
@RequestMapping("/admin")
public class BlogAdminController {
	@Autowired
	BlogService blogService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	TagService tagService;
	@Autowired
	CommentService commentService;

	/**
	 * 获取博客文章列表
	 *
	 * @param title      按标题模糊查询
	 * @param categoryId 按分类id查询
	 * @param pageNum    页码
	 * @param pageSize   每页个数
	 * @return
	 */
	@GetMapping("/blogs")
	public Result blogs(@RequestParam(defaultValue = "") String title,
	                    @RequestParam(defaultValue = "") Integer categoryId,
	                    @RequestParam(defaultValue = "1") Integer pageNum,
	                    @RequestParam(defaultValue = "10") Integer pageSize) {
		String orderBy = "create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<Blog> pageInfo = new PageInfo<>(blogService.getListByTitleAndCategoryId(title, categoryId));
		List<Category> categories = categoryService.getCategoryList();
		Map<String, Object> map = new HashMap<>(4);
		map.put("blogs", pageInfo);
		map.put("categories", categories);
		return Result.ok("请求成功", map);
	}

	/**
	 * 删除博客文章、删除博客文章下的所有评论、同时维护 blog_tag 表
	 *
	 * @param id 文章id
	 * @return
	 */
	@OperationLogger("删除博客")
	@DeleteMapping("/blog")
	public Result delete(@RequestParam Long id) {
		blogService.deleteBlogTagByBlogId(id);
		blogService.deleteBlogById(id);
		commentService.deleteCommentsByBlogId(id);
		return Result.ok("删除成功");
	}

	/**
	 * 获取分类列表和标签列表
	 *
	 * @return
	 */
	@GetMapping("/categoryAndTag")
	public Result categoryAndTag() {
		List<Category> categories = categoryService.getCategoryList();
		List<Tag> tags = tagService.getTagList();
		Map<String, Object> map = new HashMap<>(4);
		map.put("categories", categories);
		map.put("tags", tags);
		return Result.ok("请求成功", map);
	}

	/**
	 * 更新博客置顶状态
	 *
	 * @param id  博客id
	 * @param top 是否置顶
	 * @return
	 */
	@OperationLogger("更新博客置顶状态")
	@PutMapping("/blog/top")
	public Result updateTop(@RequestParam Long id, @RequestParam Boolean top) {
		blogService.updateBlogTopById(id, top);
		return Result.ok("操作成功");
	}

	/**
	 * 更新博客推荐状态
	 *
	 * @param id        博客id
	 * @param recommend 是否推荐
	 * @return
	 */
	@OperationLogger("更新博客推荐状态")
	@PutMapping("/blog/recommend")
	public Result updateRecommend(@RequestParam Long id, @RequestParam Boolean recommend) {
		blogService.updateBlogRecommendById(id, recommend);
		return Result.ok("操作成功");
	}

	/**
	 * 更新博客可见性状态
	 *
	 * @param id             博客id
	 * @param blogVisibility 博客可见性DTO
	 * @return
	 */
	@OperationLogger("更新博客可见性状态")
	@PutMapping("blog/{id}/visibility")
	public Result updateVisibility(@PathVariable Long id, @RequestBody BlogVisibility blogVisibility) {
		blogService.updateBlogVisibilityById(id, blogVisibility);
		return Result.ok("操作成功");
	}

	/**
	 * 按id获取博客详情
	 *
	 * @param id 博客id
	 * @return
	 */
	@GetMapping("/blog")
	public Result getBlog(@RequestParam Long id) {
		Blog blog = blogService.getBlogById(id);
		return Result.ok("获取成功", blog);
	}

	/**
	 * 保存草稿或发布新文章
	 *
	 * @param blog 博客文章DTO
	 * @return
	 */
	@OperationLogger("发布博客")
	@PostMapping("/blog")
	public Result saveBlog(@RequestBody top.naccl.model.dto.Blog blog) {
		return getResult(blog, "save");
	}

	/**
	 * 更新博客
	 *
	 * @param blog 博客文章DTO
	 * @return
	 */
	@OperationLogger("更新博客")
	@PutMapping("/blog")
	public Result updateBlog(@RequestBody top.naccl.model.dto.Blog blog) {
		return getResult(blog, "update");
	}

	/**
	 * 执行博客添加或更新操作：校验参数是否合法，添加分类、标签，维护博客标签关联表
	 *
	 * @param blog 博客文章DTO
	 * @param type 添加或更新
	 * @return
	 */
	private Result getResult(top.naccl.model.dto.Blog blog, String type) {
		//验证普通字段
		if (StringUtils.isEmpty(blog.getTitle(), blog.getFirstPicture(), blog.getContent(), blog.getDescription())
				|| blog.getWords() == null || blog.getWords() < 0) {
			return Result.error("参数有误");
		}

		//处理分类
		Object cate = blog.getCate();
		if (cate == null) {
			return Result.error("分类不能为空");
		}
		if (cate instanceof Integer) {//选择了已存在的分类
			Category c = categoryService.getCategoryById(((Integer) cate).longValue());
			blog.setCategory(c);
		} else if (cate instanceof String) {//添加新分类
			//查询分类是否已存在
			Category category = categoryService.getCategoryByName((String) cate);
			if (category != null) {
				return Result.error("不可添加已存在的分类");
			}
			Category c = new Category();
			c.setName((String) cate);
			categoryService.saveCategory(c);
			blog.setCategory(c);
		} else {
			return Result.error("分类不正确");
		}

		//处理标签
		List<Object> tagList = blog.getTagList();
		List<Tag> tags = new ArrayList<>();
		for (Object t : tagList) {
			if (t instanceof Integer) {//选择了已存在的标签
				Tag tag = tagService.getTagById(((Integer) t).longValue());
				tags.add(tag);
			} else if (t instanceof String) {//添加新标签
				//查询标签是否已存在
				Tag tag1 = tagService.getTagByName((String) t);
				if (tag1 != null) {
					return Result.error("不可添加已存在的标签");
				}
				Tag tag = new Tag();
				tag.setName((String) t);
				tagService.saveTag(tag);
				tags.add(tag);
			} else {
				return Result.error("标签不正确");
			}
		}

		Date date = new Date();
		if (blog.getReadTime() == null || blog.getReadTime() < 0) {
			blog.setReadTime((int) Math.round(blog.getWords() / 200.0));//粗略计算阅读时长
		}
		if (blog.getViews() == null || blog.getViews() < 0) {
			blog.setViews(0);
		}
		if ("save".equals(type)) {
			blog.setCreateTime(date);
			blog.setUpdateTime(date);
			User user = new User();
			user.setId(1L);//个人博客默认只有一个作者
			blog.setUser(user);

			blogService.saveBlog(blog);
			//关联博客和标签(维护 blog_tag 表)
			for (Tag t : tags) {
				blogService.saveBlogTag(blog.getId(), t.getId());
			}
			return Result.ok("添加成功");
		} else {
			blog.setUpdateTime(date);
			blogService.updateBlog(blog);
			//关联博客和标签(维护 blog_tag 表)
			blogService.deleteBlogTagByBlogId(blog.getId());
			for (Tag t : tags) {
				blogService.saveBlogTag(blog.getId(), t.getId());
			}
			return Result.ok("更新成功");
		}
	}
}
