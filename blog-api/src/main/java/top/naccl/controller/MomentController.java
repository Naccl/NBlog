package top.naccl.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.model.vo.Moment;
import top.naccl.model.vo.PageResult;
import top.naccl.model.vo.Result;
import top.naccl.service.MomentService;

/**
 * @Description: 动态
 * @Author: Naccl
 * @Date: 2020-08-25
 */
@RestController
@RequestMapping
public class MomentController {
	@Autowired
	MomentService momentService;

	/**
	 * 分页查询动态List
	 *
	 * @param pageNum 页码
	 * @return
	 */
	@GetMapping("/moments")
	public Result moments(@RequestParam(defaultValue = "1") Integer pageNum) {
		int pageSize = 5;//每页显示5条
		String orderBy = "create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<Moment> pageInfo = new PageInfo<>(momentService.getMomentListByPublished());
		PageResult<Moment> pageResult = new PageResult<>(pageInfo.getPages(), pageInfo.getList());
		return Result.ok("获取成功", pageResult);
	}

	/**
	 * 给动态点赞
	 *
	 * @param id 动态id
	 * @return
	 */
	@PutMapping("/moment/like")
	public Result like(@RequestParam Long id) {
		momentService.addLikeByMomentId(id);
		return Result.ok("点赞成功");
	}
}
