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
import top.naccl.entity.Category;
import top.naccl.service.BlogService;
import top.naccl.service.CategoryService;
import top.naccl.util.Result;
import top.naccl.util.StringUtils;

import java.util.Map;

/**
 * @Description: 博客分类后台管理
 * @Author: Naccl
 * @Date: 2020-08-02
 */
@RestController
@RequestMapping("/admin")
public class CategoryController {
	@Autowired
	BlogService blogService;
	@Autowired
	CategoryService categoryService;

	/**
	 * 获取博客分类列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @return
	 */
	@GetMapping("/categories")
	public Result categories(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
		try {
			String orderBy = "id desc";
			PageHelper.startPage(pageNum, pageSize, orderBy);
			PageInfo<Category> pageInfo = new PageInfo<>(categoryService.getCategoryList());
			return Result.ok("请求成功", pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 添加新分类
	 *
	 * @param map 包含分类名称的map => {name="123"}
	 * @return
	 */
	@PostMapping("/category")
	public Result saveCategory(@RequestBody Map<String, Object> map) {
		return getResult(map, "save");
	}

	/**
	 * 修改分类名称
	 *
	 * @param map 包含分类id和名称的map => {id=1, name="123"}
	 * @return
	 */
	@PutMapping("/category")
	public Result updateCategory(@RequestBody Map<String, Object> map) {
		return getResult(map, "update");
	}

	/**
	 * 执行分类添加或更新操作：校验参数是否合法，分类是否已存在
	 *
	 * @param map  分类map对象
	 * @param type 添加或更新
	 * @return
	 */
	private Result getResult(Map<String, Object> map, String type) {
		try {
			JSONObject categoryJsonObject = new JSONObject(map);
			Category category = JSONObject.toJavaObject(categoryJsonObject, Category.class);
			if (StringUtils.isEmpty(category.getName())) {
				return Result.error("分类名称不能为空");
			}
			//查询分类是否已存在
			Category category1 = categoryService.getCategoryByName(category.getName());
			if (category1 != null && category1.getId() != category.getId()) {
				return Result.error("该分类已存在");
			}
			if ("save".equals(type)) {
				int r = categoryService.saveCategory(category);
				if (r == 1) {//添加分类成功
					return Result.ok("添加成功");
				} else {//添加分类失败
					return Result.error("添加失败");
				}
			} else {
				int r = categoryService.updateCategory(category);
				if (r == 1) {//更新分类成功
					return Result.ok("更新成功");
				} else {//更新分类失败
					return Result.error("更新失败");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

	/**
	 * 按id删除分类
	 *
	 * @param id 分类id
	 * @return
	 */
	@DeleteMapping("/category")
	public Result delete(@RequestParam Long id) {
		try {
			//删除存在博客关联的分类后，该博客的查询会出异常
			int num = blogService.countBlogByCategoryId(id);
			if (num != 0) {
				return Result.error("已有博客与此分类关联，不可删除");
			}
			int r = categoryService.deleteCategoryById(id);
			if (r == 1) {//删除分类成功
				return Result.ok("删除成功");
			} else {//删除分类失败
				return Result.error("删除失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
