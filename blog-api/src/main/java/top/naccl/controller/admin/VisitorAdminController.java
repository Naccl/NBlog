package top.naccl.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.Visitor;
import top.naccl.model.vo.Result;
import top.naccl.service.VisitorService;

/**
 * @Description: 访客统计
 * @Author: Naccl
 * @Date: 2021-02-02
 */
@RestController
@RequestMapping("/admin")
public class VisitorAdminController {
	@Autowired
	VisitorService visitorService;

	/**
	 * 分页查询访客列表
	 *
	 * @param date     按最后访问时间查询
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @return
	 */
	@GetMapping("/visitors")
	public Result visitors(@RequestParam(defaultValue = "") String[] date,
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
		PageInfo<Visitor> pageInfo = new PageInfo<>(visitorService.getVisitorListByDate(startDate, endDate));
		return Result.ok("请求成功", pageInfo);
	}

	/**
	 * 按id删除访客
	 * 按uuid删除Redis缓存
	 *
	 * @param id   访客id
	 * @param uuid 访客uuid
	 * @return
	 */
	@DeleteMapping("/visitor")
	public Result delete(@RequestParam Long id, @RequestParam String uuid) {
		visitorService.deleteVisitor(id, uuid);
		return Result.ok("删除成功");
	}
}
