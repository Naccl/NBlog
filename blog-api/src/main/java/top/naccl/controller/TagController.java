package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Tag;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.TagService;

import java.util.List;

/**
 * @Description: 标签
 * @Author: Naccl
 * @Date: 2020-08-17
 */
@RestController
@RequestMapping
public class TagController {
	@Autowired
	TagService tagService;

	/**
	 * 标签云
	 *
	 * @return
	 */
	@GetMapping("/tags")
	public Result tags() {
		List<Tag> tags = tagService.getTagListNotId();
		return Result.ok("获取成功", tags);
	}

	/**
	 * 根据标签name分页查询公开博客列表
	 *
	 * @param tagName 标签name
	 * @param pageNum 页码
	 * @return
	 */
	@GetMapping("/tag")
	public Result tag(@RequestParam String tagName,
	                  @RequestParam(defaultValue = "1") Integer pageNum) {
		int pageSize = 5;//每页显示5条
		String orderBy = "is_top desc, create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<BlogInfo> pageInfo = new PageInfo<>(tagService.getBlogInfoListByTagNameAndIsPublished(tagName));
		PageResult<BlogInfo> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
		return Result.ok("请求成功", pageResult);
	}
}
