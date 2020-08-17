package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Tag;
import top.naccl.model.vo.Result;
import top.naccl.service.TagService;

import java.util.List;

/**
 * @Description: 标签
 * @Author: Naccl
 * @Date: 2020-08-17
 */
@RestController
@RequestMapping("/")
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
		List<Tag> tags = tagService.getTagList();
		return Result.ok("获取成功", tags);
	}
}
