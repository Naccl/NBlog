package top.naccl.service;

import org.springframework.scheduling.annotation.Async;
import top.naccl.entity.VisitLog;

import java.util.List;

public interface VisitLogService {
	List<VisitLog> getVisitLogListByUUIDAndDate(String uuid, String startDate, String endDate);

	@Async
	void saveVisitLog(VisitLog log);

	void deleteVisitLogById(Long id);
}
