package top.naccl.service;

import org.springframework.scheduling.annotation.Async;
import top.naccl.entity.Visitor;

import java.util.List;

public interface VisitorService {
	List<Visitor> getVisitorList();

	boolean hasUUID(String uuid);

	@Async
	void saveVisitor(Visitor visitor);

	void deleteVisitorById(Long id);
}
