package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.LoginLog;
import top.naccl.model.vo.Result;
import top.naccl.service.LoginLogService;

/**
 * @Description: 登录日志后台管理
 * @Author: Naccl
 * @Date: 2020-12-03
 */
@RestController
@RequestMapping("/admin")
public class LoginLogController {
	@Autowired
	LoginLogService loginLogService;

	/**
	 * 分页查询登录日志列表
	 *
	 * @param date     按操作时间查询
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @return
	 */
	@GetMapping("/loginLogs")
	public Result loginLogs(@RequestParam(defaultValue = "") String[] date,
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
		PageInfo<LoginLog> pageInfo = new PageInfo<>(loginLogService.getLoginLogListByDate(startDate, endDate));
		return Result.ok("请求成功", pageInfo);
	}

	/**
	 * 按id删除登录日志
	 *
	 * @param id 日志id
	 * @return
	 */
	@DeleteMapping("/loginLog")
	public Result delete(@RequestParam Long id) {
		loginLogService.deleteLoginLogById(id);
		return Result.ok("删除成功");
	}
}
