package top.naccl.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Blog;
import top.naccl.entity.Category;
import top.naccl.entity.Tag;
import top.naccl.entity.User;
import top.naccl.service.BlogService;
import top.naccl.service.CategoryService;
import top.naccl.service.TagService;
import top.naccl.util.Result;

import java.util.ArrayList;
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
public class BlogController {
	@Autowired
	BlogService blogService;
	@Autowired
	CategoryService categoryService;
	@Autowired
	TagService tagService;

	/**
	 * 获取博客文章列表
	 *
	 * @param query      按标题模糊查询
	 * @param CategoryId 按分类id查询
	 * @param pageNum    页码
	 * @param pageSize   每页个数
	 * @return
	 */
	@GetMapping("/blogs")
	public Result blogs(@RequestParam(defaultValue = "") String query,
	                    @RequestParam(defaultValue = "") Integer CategoryId,
	                    @RequestParam(defaultValue = "1") Integer pageNum,
	                    @RequestParam(defaultValue = "10") Integer pageSize) {
		try {
			String orderBy = "create_time desc";
			PageHelper.startPage(pageNum, pageSize, orderBy);
			PageInfo<Blog> pageInfo = new PageInfo<>(blogService.getListByTitleOrCategory(query, CategoryId));
			List<Category> categories = categoryService.getCategoryList();

			Map<String, Object> map = new HashMap<>();
			map.put("blogs", pageInfo);
			map.put("categories", categories);
			return Result.ok("请求成功", map);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 删除博客文章 同时维护 blog_tag 表
	 *
	 * @param id 文章id
	 * @return
	 */
	@DeleteMapping("/blogs")
	public Result delete(@RequestParam Long id) {
		try {
			int r = blogService.deleteBlogById(id);
			if (r == 1) {//删除博客成功
				int r1 = blogService.deleteBlogTagByBlogId(id);
				if (r1 != 0) {
					return Result.ok("删除成功");
				} else {
					return Result.error("维护博客标签关联表失败");
				}
			} else {//删除博客失败
				return Result.error("删除博客失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 获取分类列表和标签列表
	 *
	 * @return
	 */
	@GetMapping("/categoryAndTag")
	public Result categoryAndTag() {
		try {
			List<Category> categories = categoryService.getCategoryList();
			List<Tag> tags = tagService.getTagList();
			Map<String, Object> map = new HashMap<>();
			map.put("categories", categories);
			map.put("tags", tags);
			return Result.ok("请求成功", map);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 保存草稿或发布新文章
	 *
	 * @param map 包含了博客文章 LinkedHashMap 对象的 LinkedHashMap => map = {blog: {...}}
	 * @return
	 */
	@PostMapping("/blog")
	public Result saveBlog(@RequestBody Map<String, Object> map) {
		try {
			Map<String, Object> blogMap = (Map<String, Object>) map.get("blog");
			JSONObject blogJsonObject = new JSONObject(blogMap);
			Blog blog = JSONObject.toJavaObject(blogJsonObject, Blog.class);

			//验证普通字段
			if (StringUtils.isEmpty(blog.getTitle()) || StringUtils.isEmpty(blog.getContent())
					|| StringUtils.isEmpty(blog.getFirstPicture()) || StringUtils.isEmpty(blog.getDescription())
					|| StringUtils.isEmpty(blog.getFlag()) || blog.getWords() == null || blog.getWords() < 0) {
				return Result.error("参数有误");
			}

			//处理分类
			Object cate = blogMap.get("cate");
			if (cate == null) {
				return Result.error("分类不能为空");
			}
			if (cate instanceof Integer) {//选择了已存在的分类
				Category c = categoryService.getCategoryById(((Integer) cate).longValue());
				if (c != null) {
					blog.setCategory(c);
				} else {
					return Result.error("分类不存在");
				}
			} else if (cate instanceof String) {//添加新分类
				Category c = new Category();
				c.setName((String) cate);
				int r = categoryService.saveCategory(c);
				if (r == 1) {//添加分类成功
					blog.setCategory(c);
				} else {//添加分类失败
					return Result.error("分类添加失败");
				}
			} else {
				return Result.error("分类不正确");
			}

			//处理标签
			List<Object> tagList = (List<Object>) blogMap.get("tagList");
			List<Tag> tags = new ArrayList<>();
			for (Object t : tagList) {
				if (t instanceof Integer) {//选择了已存在的标签
					Tag tag = tagService.getTagById(((Integer) t).longValue());
					if (tag != null) {
						tags.add(tag);
					} else {
						return Result.error("标签不存在");
					}
				} else if (t instanceof String) {//添加新标签
					Tag tag = new Tag();
					tag.setName((String) t);
					int r = tagService.saveTag(tag);
					if (r == 1) {//添加标签成功
						tags.add(tag);
					} else {//添加标签失败
						return Result.error("标签添加失败");
					}
				} else {
					return Result.error("标签不正确");
				}
			}

			User user = new User();
			user.setId((long) 1);//个人博客只有一个作者
			blog.setUser(user);
			blog.setReadTime((int) Math.round(blog.getWords() / 200.0) + 5);//粗略计算阅读时长

			int r = blogService.saveBlog(blog);
			if (r == 1) {//添加博客成功
				//关联博客和标签(维护 blog_tag 表)
				for (Tag t : tags) {
					int r1 = blogService.saveBlogTag(blog.getId(), t.getId());
					if (r1 != 1) Result.error("维护博客标签关联表失败");
				}
			} else {//添加博客失败
				return Result.error("添加博客失败");
			}
			return Result.ok("添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
