package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.entity.SystemSetting;
import top.naccl.exception.PersistenceException;
import top.naccl.mapper.SystemSettingMapper;
import top.naccl.service.SystemSettingService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description: 系统配置业务层实现
 * @Author: Naccl
 * @Date: 2023-02-25
 */
@Service
public class SystemSettingServiceImpl implements SystemSettingService {
	@Autowired
	private SystemSettingMapper systemSettingMapper;

	@Override
	public Map<String, List<SystemSetting>> list() {
		List<SystemSetting> list = systemSettingMapper.list();
		Map<String, List<SystemSetting>> collect = list.stream().collect(Collectors.groupingBy(o -> "type" + o.getType(), Collectors.toList()));
		collect.forEach((k, v) -> v.sort(Comparator.comparingInt(SystemSetting::getSortOrder)));
		return collect;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateAll(List<SystemSetting> systemSettings) {
		for (SystemSetting systemSetting : systemSettings) {
			if (systemSettingMapper.updateValueById(systemSetting) != 1) {
				throw new PersistenceException("配置修改失败");
			}
		}
	}
}
