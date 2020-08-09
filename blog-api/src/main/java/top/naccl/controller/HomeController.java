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
import top.naccl.service.BlogService;
import top.naccl.model.vo.Result;
import top.naccl.service.TagService;

/**
 * @Description: 主页
 * @Author: Naccl
 * @Date: 2020-08-08
 */
@RestController
@RequestMapping("/")
public class HomeController {
	@Autowired
	BlogService blogService;
	@Autowired
	TagService tagService;

	@GetMapping("/blogs")
	public Result blogs(@RequestParam(defaultValue = "1") Integer pageNum) {
		try {
			String orderBy = "create_time desc";
			PageHelper.startPage(pageNum, 5, orderBy);
			PageInfo<BlogInfo> pageInfo = new PageInfo<>(blogService.getBlogInfoListByIsPublished());
			PageResult<BlogInfo> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
			return Result.ok("请求成功", pageResult);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
