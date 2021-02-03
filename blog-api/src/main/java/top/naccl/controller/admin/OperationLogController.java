package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.OperationLog;
import top.naccl.model.vo.Result;
import top.naccl.service.OperationLogService;

/**
 * @Description: 操作日志后台管理
 * @Author: Naccl
 * @Date: 2020-12-01
 */
@RestController
@RequestMapping("/admin")
public class OperationLogController {
	@Autowired
	OperationLogService operationLogService;

	/**
	 * 分页查询操作日志列表
	 *
	 * @param date     按操作时间查询
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @return
	 */
	@GetMapping("/operationLogs")
	public Result operationLogs(@RequestParam(defaultValue = "") String[] date,
	                            @RequestParam(defaultValue = "1") Integer pageNum,
	                            @RequestParam(defaultValue = "10") Integer pageSize) {
		String startDate = null;
		String endDate = null;
		if (date.length == 2) {
			startDate = date[0];
			endDate = date[1];
		}
		String orderBy = "create_time desc";
		PageHelper.startPage(pageNum, pageSize, orderBy);
		PageInfo<OperationLog> pageInfo = new PageInfo<>(operationLogService.getOperationLogListByDate(startDate, endDate));
		return Result.ok("请求成功", pageInfo);
	}

	/**
	 * 按id删除操作日志
	 *
	 * @param id 日志id
	 * @return
	 */
	@DeleteMapping("/operationLog")
	public Result delete(@RequestParam Long id) {
		operationLogService.deleteOperationLogById(id);
		return Result.ok("删除成功");
	}
}
