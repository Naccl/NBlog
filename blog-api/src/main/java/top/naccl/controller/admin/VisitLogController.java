package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.VisitLog;
import top.naccl.model.vo.Result;
import top.naccl.service.VisitLogService;

/**
 * @Description: 访问日志后台管理
 * @Author: Naccl
 * @Date: 2020-12-04
 */
@RestController
@RequestMapping("/admin")
public class VisitLogController {
	@Autowired
	VisitLogService visitLogService;

	/**
	 * 分页查询访问日志列表
	 *
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @return
	 */
	@GetMapping("/visitLogs")
	public Result visitLogs(@RequestParam(defaultValue = "1") Integer pageNum,
	                        @RequestParam(defaultValue = "10") Integer pageSize) {
		String orderBy = "create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<VisitLog> pageInfo = new PageInfo<>(visitLogService.getVisitLogList());
		return Result.ok("请求成功", pageInfo);
	}

	/**
	 * 按id删除访问日志
	 *
	 * @param id 日志id
	 * @return
	 */
	@DeleteMapping("/visitLog")
	public Result delete(@RequestParam Long id) {
		visitLogService.deleteVisitLogById(id);
		return Result.ok("删除成功");
	}
}
