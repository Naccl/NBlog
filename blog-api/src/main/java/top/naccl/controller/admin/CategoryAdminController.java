package top.naccl.controller.admin;

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
import top.naccl.annotation.OperationLogger;
import top.naccl.entity.Category;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;
import top.naccl.service.CategoryService;
import top.naccl.util.StringUtils;

/**
 * @Description: 博客分类后台管理
 * @Author: Naccl
 * @Date: 2020-08-02
 */
@RestController
@RequestMapping("/admin")
public class CategoryAdminController {
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
		String orderBy = "id desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<Category> pageInfo = new PageInfo<>(categoryService.getCategoryList());
		return Result.ok("请求成功", pageInfo);
	}

	/**
	 * 添加新分类
	 *
	 * @param category 分类实体
	 * @return
	 */
	@OperationLogger("添加分类")
	@PostMapping("/category")
	public Result saveCategory(@RequestBody Category category) {
		return getResult(category, "save");
	}

	/**
	 * 修改分类名称
	 *
	 * @param category 分类实体
	 * @return
	 */
	@OperationLogger("修改分类")
	@PutMapping("/category")
	public Result updateCategory(@RequestBody Category category) {
		return getResult(category, "update");
	}

	/**
	 * 执行分类添加或更新操作：校验参数是否合法，分类是否已存在
	 *
	 * @param category 分类实体
	 * @param type     添加或更新
	 * @return
	 */
	private Result getResult(Category category, String type) {
		if (StringUtils.isEmpty(category.getName())) {
			return Result.error("分类名称不能为空");
		}
		//查询分类是否已存在
		Category category1 = categoryService.getCategoryByName(category.getName());
		//如果 category1.getId().equals(category.getId()) == true 就是更新分类
		if (category1 != null && !category1.getId().equals(category.getId())) {
			return Result.error("该分类已存在");
		}
		if ("save".equals(type)) {
			categoryService.saveCategory(category);
			return Result.ok("分类添加成功");
		} else {
			categoryService.updateCategory(category);
			return Result.ok("分类更新成功");
		}
	}

	/**
	 * 按id删除分类
	 *
	 * @param id 分类id
	 * @return
	 */
	@OperationLogger("删除分类")
	@DeleteMapping("/category")
	public Result delete(@RequestParam Long id) {
		//删除存在博客关联的分类后，该博客的查询会出异常
		int num = blogService.countBlogByCategoryId(id);
		if (num != 0) {
			return Result.error("已有博客与此分类关联，不可删除");
		}
		categoryService.deleteCategoryById(id);
		return Result.ok("删除成功");
	}
}
