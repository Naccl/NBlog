package top.naccl.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.naccl.config.RedisKeyConfig;
import top.naccl.model.dto.VisitLogUuidTime;
import top.naccl.service.RedisService;
import top.naccl.service.VisitLogService;
import top.naccl.service.VisitorService;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	/**
	 * 清空当天Redis访客标识
	 * 更新当天所有访客的PV和最后访问时间
	 */
	public void syncVisitorPVAndLastTimeToDatabase() {
		//清空当天Redis的访客标识Set，以便统计每日UV
		redisService.deleteCacheByKey(RedisKeyConfig.IDENTIFICATION_SET);
		//获取昨天的所有访问日志
		List<VisitLogUuidTime> yesterdayLogList = visitLogService.getUUIDAndCreateTimeByYesterday();
		//用Set去重得到当天所有访客的uuid
		//为避免缓存击穿导致第二天的数据统计不准确，以数据库访问日志为准，而不从Redis中获取这个Set
		//比如在这个定时任务执行期间，产生大量访客的请求，而这些访客的uuid都在任务执行结束后被清空了，没有被第二天的定时任务记录到
		Set<String> identificationSet = new HashSet<>();
		Map<String, Integer> PVMap = new HashMap<>();
		Map<String, Date> lastTimeMap = new HashMap<>();
		for (VisitLogUuidTime log : yesterdayLogList) {
			String uuid = log.getUuid();
			Date createTime = log.getTime();
			//记录当天访客的uuid
			identificationSet.add(uuid);
			//对应uuid的PV++
			PVMap.merge(uuid, 1, Integer::sum);
			//因为sql中order by create_time desc，直接put第一次出现的uuid的createTime，就是最后一次访问时间
			lastTimeMap.putIfAbsent(uuid, createTime);
		}
		//更新当天所有访客的PV和最后访问时间到数据库
		identificationSet.forEach(uuid -> {
			VisitLogUuidTime uuidPVTimeDTO = new VisitLogUuidTime(uuid, lastTimeMap.get(uuid), PVMap.get(uuid));
			visitorService.updatePVAndLastTimeByUUID(uuidPVTimeDTO);
		});
	}
}
