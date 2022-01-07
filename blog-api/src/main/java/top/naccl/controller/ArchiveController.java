package top.naccl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.annotation.VisitLogger;
import top.naccl.enums.VisitBehavior;
import top.naccl.model.vo.Result;
import top.naccl.service.BlogService;

import java.util.Map;

/**
 * @Description: 归档页面
 * @Author: Naccl
 * @Date: 2020-08-12
 */
@RestController
public class ArchiveController {
	@Autowired
	BlogService blogService;

	/**
	 * 按年月分组归档公开博客 统计公开博客总数
	 *
	 * @return
	 */
	@VisitLogger(VisitBehavior.ARCHIVE)
	@GetMapping("/archives")
	public Result archives() {
		Map<String, Object> archiveBlogMap = blogService.getArchiveBlogAndCountByIsPublished();
		return Result.ok("请求成功", archiveBlogMap);
	}
}
