package top.naccl.service;

import top.naccl.entity.SystemSetting;

import java.util.List;
import java.util.Map;

public interface SystemSettingService {
	Map<String, List<SystemSetting>> list();

	void updateAll(List<SystemSetting> systemSettings);
}
