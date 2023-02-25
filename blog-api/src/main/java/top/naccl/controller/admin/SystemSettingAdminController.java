package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.naccl.annotation.OperationLogger;
import top.naccl.entity.SystemSetting;
import top.naccl.model.vo.Result;
import top.naccl.service.SystemSettingService;

import java.util.List;

/**
 * @Description: 站点设置后台管理
 * @Author: Naccl
 * @Date: 2023-02-25
 */
@RestController
@RequestMapping("/admin")
public class SystemSettingAdminController {
	@Autowired
	private SystemSettingService systemSettingService;

	@GetMapping("/systemSettings")
	public Result systemSettings() {
		return Result.ok("请求成功", systemSettingService.list());
	}

	@OperationLogger("更新系统配置信息")
	@PostMapping("/systemSettings")
	public Result updateAll(@RequestBody List<SystemSetting> systemSettings) {
		systemSettingService.updateAll(systemSettings);
		return Result.ok("更新成功");
	}
}
