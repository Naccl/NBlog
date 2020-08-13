package top.naccl.controller.admin;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import top.naccl.model.vo.Result;
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

	/**
	 * 获取博客文章列表
	 *
	 * @param title      按标题模糊查询
	 * @param CategoryId 按分类id查询
	 * @param pageNum    页码
	 * @param pageSize   每页个数
	 * @return
	 */
	@GetMapping("/blogs")
	public Result blogs(@RequestParam(defaultValue = "") String title,
	                    @RequestParam(defaultValue = "") Integer CategoryId,
	                    @RequestParam(defaultValue = "1") Integer pageNum,
	                    @RequestParam(defaultValue = "10") Integer pageSize) {
		try {
			String orderBy = "create_time desc";
			PageHelper.startPage(pageNum, pageSize, orderBy);
			PageInfo<Blog> pageInfo = new PageInfo<>(blogService.getListByTitleOrCategoryId(title, CategoryId));
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
	@DeleteMapping("/blog")
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
		return getResult(map, "save");
	}

	/**
	 * 更新博客置顶状态
	 *
	 * @param id  博客id
	 * @param top 是否置顶
	 * @return
	 */
	@PutMapping("/blog/top")
	public Result updateTop(@RequestParam Long id, @RequestParam Boolean top) {
		try {
			int r = blogService.updateBlogTopById(id, top);
			if (r == 1) {
				return Result.ok("操作成功");
			} else {
				return Result.error("操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 更新博客推荐状态
	 *
	 * @param id        博客id
	 * @param recommend 是否推荐
	 * @return
	 */
	@PutMapping("/blog/recommend")
	public Result updateRecommend(@RequestParam Long id, @RequestParam Boolean recommend) {
		try {
			int r = blogService.updateBlogRecommendById(id, recommend);
			if (r == 1) {
				return Result.ok("操作成功");
			} else {
				return Result.error("操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 更新博客发布状态
	 *
	 * @param id        博客id
	 * @param published 是否发布
	 * @return
	 */
	@PutMapping("/blog/published")
	public Result updatePublished(@RequestParam Long id, @RequestParam Boolean published) {
		try {
			int r = blogService.updateBlogPublishedById(id, published);
			if (r == 1) {
				return Result.ok("操作成功");
			} else {
				return Result.error("操作失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 按id获取博客详情
	 *
	 * @param id 博客id
	 * @return
	 */
	@GetMapping("/blog")
	public Result getBlog(@RequestParam Long id) {
		try {
			Blog blog = blogService.getBlogById(id);
			if (blog == null) {
				return Result.error("博客不存在");
			}
			return Result.ok("获取成功", blog);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 更新博客
	 *
	 * @param map 包含了博客文章 LinkedHashMap 对象的 LinkedHashMap => map = {blog: {...}}
	 * @return
	 */
	@PutMapping("/blog")
	public Result updateBlog(@RequestBody Map<String, Object> map) {
		return getResult(map, "update");
	}

	/**
	 * 执行博客添加或更新操作：校验参数是否合法，添加分类、标签，维护博客标签关联表
	 *
	 * @param map  博客文章map对象
	 * @param type 添加或更新
	 * @return
	 */
	private Result getResult(Map<String, Object> map, String type) {
		try {
			JSONObject blogJsonObject = new JSONObject(map);
			Blog blog = JSONObject.toJavaObject(blogJsonObject, Blog.class);

			//验证普通字段
			if (StringUtils.isEmpty(blog.getTitle(), blog.getContent(), blog.getDescription())
					|| blog.getWords() == null || blog.getWords() < 0) {
				return Result.error("参数有误");
			}

			//处理分类
			Object cate = map.get("cate");
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
				//查询分类是否已存在
				Category category = categoryService.getCategoryByName((String) cate);
				if (category != null) {
					return Result.error("不可添加已存在的分类");
				}
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
			List<Object> tagList = (List<Object>) map.get("tagList");
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
					//查询标签是否已存在
					Tag tag1 = tagService.getTagByName((String) t);
					if (tag1 != null) {
						return Result.error("不可添加已存在的标签");
					}
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
				user.setId((long) 1);//个人博客默认只有一个作者
				blog.setUser(user);

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
			} else {
				blog.setUpdateTime(date);
				int r = blogService.updateBlog(blog);
				if (r == 1) {//更新博客成功
					//关联博客和标签(维护 blog_tag 表)
					int r1 = blogService.deleteBlogTagByBlogId(blog.getId());
					if (r1 != 0) {
						for (Tag t : tags) {
							int r2 = blogService.saveBlogTag(blog.getId(), t.getId());
							if (r2 != 1) Result.error("维护博客标签关联表失败");
						}
					} else {
						Result.error("维护博客标签关联表失败");
					}
				} else {//更新博客失败
					return Result.error("更新博客失败");
				}
				return Result.ok("更新成功");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
