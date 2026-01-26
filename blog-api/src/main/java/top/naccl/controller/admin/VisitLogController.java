package top.naccl.controller.admin;

import com.alibaba.excel.EasyExcel;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.VisitLog;
import top.naccl.model.vo.Result;
import top.naccl.model.vo.VisitLogExcel;
import top.naccl.service.VisitLogService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	 * @param uuid     按访客标识码模糊查询
	 * @param date     按访问时间查询
	 * @param pageNum  页码
	 * @param pageSize 每页个数
	 * @return
	 */
	@GetMapping("/visitLogs")
	public Result visitLogs(@RequestParam(defaultValue = "") String uuid,
	                        @RequestParam(defaultValue = "") String[] date,
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
		PageInfo<VisitLog> pageInfo = new PageInfo<>(visitLogService.getVisitLogListByUUIDAndDate(StringUtils.trim(uuid), startDate, endDate));
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

	/**
	 * 导出访问日志
	 *
	 * @param uuid     按访客标识码模糊查询
	 * @param date     按访问时间查询
	 * @param response HTTP响应
	 * @throws IOException IO异常
	 */
	@GetMapping("/visitLog/export")
	public void exportVisitLog(@RequestParam(defaultValue = "") String uuid,
	                           @RequestParam(defaultValue = "") String[] date,
	                           HttpServletResponse response) throws IOException {
		String startDate = null;
		String endDate = null;
		if (date.length == 2) {
			startDate = date[0];
			endDate = date[1];
		} else {
			// 默认导出最近7天
			Date now = new Date();
			endDate = DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss");
			startDate = DateFormatUtils.format(new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000), "yyyy-MM-dd HH:mm:ss");
		}

		List<VisitLog> visitLogList = visitLogService.getVisitLogListByUUIDAndDate(StringUtils.trim(uuid), startDate, endDate);

		// 转换为Excel模型
		List<VisitLogExcel> excelList = new ArrayList<>();
		for (int i = 0; i < visitLogList.size(); i++) {
			VisitLog visitLog = visitLogList.get(i);
			VisitLogExcel excel = new VisitLogExcel();
			excel.setSerialNumber(i + 1);
			excel.setUuid(visitLog.getUuid());
			excel.setBehavior(visitLog.getBehavior());
			excel.setContent(visitLog.getContent());
			excel.setIp(visitLog.getIp());
			excel.setIpSource(visitLog.getIpSource());
			excel.setOs(visitLog.getOs());
			excel.setBrowser(visitLog.getBrowser());
			excel.setCreateTime(DateFormatUtils.format(visitLog.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			excelList.add(excel);
		}

		// 设置响应头
		String fileName = "访问日志_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
		String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedFileName);

		// 写入Excel
		EasyExcel.write(response.getOutputStream(), VisitLogExcel.class).sheet("访问日志").doWrite(excelList);
	}
}
