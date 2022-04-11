package top.naccl.task;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.naccl.constant.RedisKeyConstants;
import top.naccl.entity.CityVisitor;
import top.naccl.entity.VisitRecord;
import top.naccl.model.dto.VisitLogUuidTime;
import top.naccl.service.CityVisitorService;
import top.naccl.service.RedisService;
import top.naccl.service.VisitLogService;
import top.naccl.service.VisitRecordService;
import top.naccl.service.VisitorService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 访客统计相关定时任务
 * @Author: Naccl
 * @Date: 2021-02-05
 */
@Component
public class VisitorSyncScheduleTask {
	@Autowired
	RedisService redisService;
	@Autowired
	VisitLogService visitLogService;
	@Autowired
	VisitorService visitorService;
	@Autowired
	VisitRecordService visitRecordService;
	@Autowired
	CityVisitorService cityVisitorService;

	/**
	 * 这个方法不应该被直接调用，应当作为定时任务的task，在每天0点执行
	 * 每日访问量很大时，这个任务可能很耗时
	 * <p>
	 * 清空昨天Redis访客标识
	 * 记录昨天的PV和UV
	 * 更新昨天所有访客的PV和最后访问时间
	 * 更新城市新增访客UV数
	 */
	public void syncVisitInfoToDatabase() {
		//清空昨天Redis的访客标识Set，以便统计每日UV
		redisService.deleteCacheByKey(RedisKeyConstants.IDENTIFICATION_SET);
		//获取昨天的所有访问日志
		//为避免缓存击穿导致第二天的数据统计不准确，以数据库访问日志为准，而不从Redis中获取这个Set
		//比如在这个定时任务执行期间，产生大量访客的请求，而这些访客的uuid都在任务执行结束后被清空了，没有被第二天的定时任务记录到
		List<VisitLogUuidTime> yesterdayLogList = visitLogService.getUUIDAndCreateTimeByYesterday();
		//按每日UV 700的标准初始化map
		//TODO 这里可以做个优化，根据近期每日UV数量做对应初始容量适配，避免访问量很大的站点执行此任务时调用过多的resize
		Map<String, Integer> PVMap = new HashMap<>(1024);
		Map<String, Date> lastTimeMap = new HashMap<>(1024);
		yesterdayLogList.forEach(log -> {
			String uuid = log.getUuid();
			Date createTime = log.getTime();
			//对应uuid的PV++
			PVMap.merge(uuid, 1, Integer::sum);
			//因为sql中order by create_time desc，直接put第一次出现的uuid的createTime，就是最后一次访问时间
			lastTimeMap.putIfAbsent(uuid, createTime);
		});
		int pv = yesterdayLogList.size();
		int uv = PVMap.size();
		//获取昨天的日期字符串
		String date = new SimpleDateFormat("MM-dd").format(DateUtils.addDays(new Date(), -1));
		//记录昨天的PV和UV
		visitRecordService.saveVisitRecord(new VisitRecord(pv, uv, date));
		//更新昨天所有访客的PV和最后访问时间到数据库
		PVMap.forEach((uuid, views) -> {
			VisitLogUuidTime uuidPVTimeDTO = new VisitLogUuidTime(uuid, lastTimeMap.get(uuid), views);
			visitorService.updatePVAndLastTimeByUUID(uuidPVTimeDTO);
		});
		//查询昨天新增访客的ip来源
		List<String> ipSource = visitorService.getNewVisitorIpSourceByYesterday();
		//按每日新增UV 300的标准初始化map
		Map<String, Integer> cityVisitorMap = new HashMap<>(512);
		ipSource.forEach(i -> {
			if (i.startsWith("中国")) {
				String[] split = i.split("\\|");
				if (split.length == 4) {
					String city = split[2];
					cityVisitorMap.merge(city, 1, Integer::sum);
				}
			}
		});
		//更新城市新增访客UV数
		cityVisitorMap.forEach((k, v) -> cityVisitorService.saveCityVisitor(new CityVisitor(k, v)));
	}
}
