package top.naccl.service;

import org.springframework.scheduling.annotation.Async;
import top.naccl.entity.OperationLog;

import java.util.List;

public interface OperationLogService {
	List<OperationLog> getOperationLogList();

	@Async
	void saveOperationLog(OperationLog log);

	void deleteOperationLogById(Long id);
}
