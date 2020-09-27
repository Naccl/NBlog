package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Tag;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;
import top.naccl.service.TagService;

import java.util.List;

/**
 * @Description: 标签
 * @Author: Naccl
 * @Date: 2020-08-17
 */
@RestController
public class TagController {
	@Autowired
	TagService tagService;
	@Autowired
	BlogService blogService;

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
		PageResult<BlogInfo> pageResult = blogService.getBlogInfoListByTagNameAndIsPublished(tagName, pageNum);
		return Result.ok("请求成功", pageResult);
	}
}
