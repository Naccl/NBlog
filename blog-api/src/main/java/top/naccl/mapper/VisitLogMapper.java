package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.naccl.entity.VisitLog;

import java.util.List;

/**
 * @Description: 访问日志持久层接口
 * @Author: Naccl
 * @Date: 2020-12-04
 */
@Mapper
@Repository
public interface VisitLogMapper {
	List<VisitLog> getVisitLogList();

	int saveVisitLog(VisitLog log);

	int deleteVisitLogById(Long id);
}
