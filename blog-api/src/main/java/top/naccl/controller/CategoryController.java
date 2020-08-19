package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
