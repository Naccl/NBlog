package top.naccl.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.entity.VisitLog;
import top.naccl.exception.PersistenceException;
import top.naccl.mapper.VisitLogMapper;
import top.naccl.model.dto.UserAgentDTO;
import top.naccl.model.dto.VisitLogUuidTime;
import top.naccl.service.VisitLogService;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.UserAgentUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 访问日志业务层实现
 * @Author: Naccl
 * @Date: 2020-12-04
 */
@Service
public class VisitLogServiceImpl implements VisitLogService {
	@Autowired
	VisitLogMapper visitLogMapper;
	@Autowired
	UserAgentUtils userAgentUtils;

	@Override
	public List<VisitLog> getVisitLogListByUUIDAndDate(String uuid, String startDate, String endDate) {
		return visitLogMapper.getVisitLogListByUUIDAndDate(uuid, startDate, endDate);
	}

	@Override
	public List<VisitLogUuidTime> getUUIDAndCreateTimeByYesterday() {
		return visitLogMapper.getUUIDAndCreateTimeByYesterday();
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveVisitLog(VisitLog log) {
		String ipSource = IpAddressUtils.getCityInfo(log.getIp());
		UserAgentDTO userAgentDTO = userAgentUtils.parseOsAndBrowser(log.getUserAgent());
		log.setIpSource(ipSource);
		log.setOs(userAgentDTO.getOs());
		log.setBrowser(userAgentDTO.getBrowser());
		if (visitLogMapper.saveVisitLog(log) != 1) {
			throw new PersistenceException("日志添加失败");
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteVisitLogById(Long id) {
		if (visitLogMapper.deleteVisitLogById(id) != 1) {
			throw new PersistenceException("删除日志失败");
		}
	}

	@Override
	public void exportVisitLogToExcel(String uuid, String startDate, String endDate, HttpServletResponse response) {
		// 如果没有设置时间范围，默认导出最近7天的数据
		if ((startDate == null || startDate.trim().isEmpty()) && (endDate == null || endDate.trim().isEmpty())) {
			Date endTime = new Date();
			Date startTime = DateUtil.offsetDay(endTime, -7);
			startDate = DateUtil.formatDateTime(startTime);
			endDate = DateUtil.formatDateTime(endTime);
		}

		// 获取符合条件的访问日志列表
		List<VisitLog> visitLogList = visitLogMapper.getVisitLogListByUUIDAndDate(uuid, startDate, endDate);

		// 创建Excel数据
		List<Map<String, Object>> rows = new ArrayList<>();
		if (visitLogList != null && !visitLogList.isEmpty()) {
			for (int i = 0; i < visitLogList.size(); i++) {
				VisitLog log = visitLogList.get(i);
				Map<String, Object> row = new LinkedHashMap<>();
				row.put("序号", i + 1);
				row.put("访客标识", log.getUuid());
				row.put("访问行为", log.getBehavior());
				row.put("访问内容", log.getContent());
				row.put("IP", log.getIp());
				row.put("IP来源", log.getIpSource());
				row.put("操作系统", log.getOs());
				row.put("浏览器", log.getBrowser());
				row.put("访问时间", DateUtil.format(log.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
				rows.add(row);
			}
		}

		// 生成文件名
		String fileName = "访问日志_" + DateUtil.format(new Date(), "yyyyMMddHHmmss") + ".xlsx";

		try {
			// 设置响应头
			response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			response.setCharacterEncoding("utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));

			// 创建Excel写入器
			ExcelWriter writer = ExcelUtil.getWriter();
			writer.write(rows, true);

			// 写入响应流
			ServletOutputStream outputStream = response.getOutputStream();
			writer.flush(outputStream, true);
			writer.close();
			outputStream.close();
		} catch (IOException e) {
			throw new RuntimeException("导出Excel文件失败: " + e.getMessage(), e);
		}
	}
}
