package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Blog;
import top.naccl.entity.Category;
import top.naccl.service.BlogService;
import top.naccl.service.CategoryService;
import top.naccl.util.Result;

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

	@GetMapping("/blogs")
	public Result blogs(@RequestParam(defaultValue = "") String query,
	                    @RequestParam(defaultValue = "") Integer typeId,
	                    @RequestParam(defaultValue = "1") Integer pageNum,
	                    @RequestParam(defaultValue = "10") Integer pageSize) {
		try {
			String orderBy = "create_time desc";
			PageHelper.startPage(pageNum, pageSize, orderBy);
			PageInfo<Blog> pageInfo = new PageInfo<>(blogService.getListByTitleOrType(query, typeId));
			List<Category> categories = categoryService.getCategoryList();

			Map<String, Object> map = new HashMap<>();
			map.put("blogs", pageInfo);
			map.put("categories", categories);
			return Result.ok("请求成功", map);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.create(500, "异常错误");
		}
	}

	@DeleteMapping("/blogs")
	public Result delete(@RequestParam Long id) {
		try {
			blogService.deleteBlogById(id);
			return Result.ok("删除成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.create(500, "异常错误");
		}
	}
}
