package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.VisitRecord;
import top.naccl.mapper.VisitRecordMapper;
import top.naccl.service.VisitRecordService;

/**
 * @Description: 访问记录业务层实现
 * @Author: Naccl
 * @Date: 2021-02-23
 */
@Service
public class VisitRecordServiceImpl implements VisitRecordService {
	@Autowired
	VisitRecordMapper visitRecordMapper;

	@Override
	public void saveVisitRecord(VisitRecord visitRecord) {
		visitRecordMapper.saveVisitRecord(visitRecord);
	}
}
