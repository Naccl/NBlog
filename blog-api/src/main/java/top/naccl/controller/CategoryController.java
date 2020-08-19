package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.CategoryService;

/**
 * @Description: 分类
 * @Author: Naccl
 * @Date: 2020-08-19
 */
@RestController
@RequestMapping("/")
public class CategoryController {
	@Autowired
	CategoryService categoryService;

	/**
	 * 获取全部分类List
	 *
	 * @return
	 */
	@GetMapping("/categories")
	public Result categories() {
		return Result.ok("请求成功", categoryService.getCategoryList());
	}

	@GetMapping("/category")
	public Result category(@RequestParam Long categoryId,
	                       @RequestParam(defaultValue = "1") Integer pageNum){
		int pageSize = 5;//每页显示5条
		String orderBy = "is_top desc, create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<BlogInfo> pageInfo = new PageInfo<>(categoryService.getBlogInfoListByCategoryIdAndIsPublished(categoryId));
		PageResult<BlogInfo> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
		return Result.ok("请求成功", pageResult);
	}
}
