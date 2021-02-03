package top.naccl.service;

import org.springframework.scheduling.annotation.Async;
import top.naccl.entity.Visitor;

import java.util.List;

public interface VisitorService {
	List<Visitor> getVisitorListByDate(String startDate, String endDate);

	boolean hasUUID(String uuid);

	@Async
	void saveVisitor(Visitor visitor);

	void deleteVisitor(Long id, String uuid);
}
