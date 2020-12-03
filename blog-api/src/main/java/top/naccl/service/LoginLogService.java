package top.naccl.service;

import org.springframework.scheduling.annotation.Async;
import top.naccl.entity.LoginLog;

import java.util.List;

public interface LoginLogService {
	List<LoginLog> getLoginLogList();

	@Async
	void saveLoginLog(LoginLog log);

	void deleteLoginLogById(Long id);
}
