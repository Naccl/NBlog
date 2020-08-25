package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Moment;
import top.naccl.model.vo.Result;
import top.naccl.service.MomentService;

/**
 * @Description: 博客动态后台管理
 * @Author: Naccl
 * @Date: 2020-08-24
 */
@RestController
@RequestMapping("/admin")
public class MomentAdminController {
	@Autowired
	MomentService momentService;

	/**
	 * 分页查询动态列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页条数
	 * @return
	 */
	@GetMapping("/moments")
	public Result moments(@RequestParam(defaultValue = "1") Integer pageNum,
	                      @RequestParam(defaultValue = "10") Integer pageSize) {
		String orderBy = "create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<Moment> pageInfo = new PageInfo<>(momentService.getMomentList());
		return Result.ok("请求成功", pageInfo);
	}

	/**
	 * 更新动态发布状态
	 *
	 * @param id        动态id
	 * @param published 是否发布
	 * @return
	 */
	@PutMapping("/moment/published")
	public Result updatePublished(@RequestParam Long id, @RequestParam Boolean published) {
		momentService.updateMomentPublishedById(id, published);
		return Result.ok("操作成功");
	}
}
