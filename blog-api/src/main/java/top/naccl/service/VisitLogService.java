package top.naccl.service;

import org.springframework.scheduling.annotation.Async;
import top.naccl.entity.VisitLog;

import java.util.List;

public interface VisitLogService {
	List<VisitLog> getVisitLogList();

	@Async
	void saveVisitLog(VisitLog log);

	void deleteVisitLogById(Long id);
}
