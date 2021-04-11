package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.annotation.OperationLogger;
import top.naccl.entity.SiteSetting;
import top.naccl.model.vo.Result;
import top.naccl.service.SiteSettingService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 站点设置后台管理
 * @Author: Naccl
 * @Date: 2020-08-09
 */
@RestController
@RequestMapping("/admin")
public class SiteSettingAdminController {
	@Autowired
	SiteSettingService siteSettingService;

	/**
	 * 获取所有站点配置信息
	 *
	 * @return
	 */
	@GetMapping("/siteSettings")
	public Result siteSettings() {
		Map<String, List<SiteSetting>> typeMap = siteSettingService.getList();
		return Result.ok("请求成功", typeMap);
	}

	/**
	 * 修改、删除(部分配置可为空，但不可删除)、添加(只能添加部分)站点配置
	 *
	 * @param map 包含所有站点信息更新后的数据 map => {settings=[更新后的所有配置List], deleteIds=[要删除的配置id List]}
	 * @return
	 */
	@OperationLogger("更新站点配置信息")
	@PostMapping("/siteSettings")
	public Result updateAll(@RequestBody Map<String, Object> map) {
		List<LinkedHashMap> siteSettings = (List<LinkedHashMap>) map.get("settings");
		List<Integer> deleteIds = (List<Integer>) map.get("deleteIds");
		siteSettingService.updateSiteSetting(siteSettings, deleteIds);
		return Result.ok("更新成功");
	}

	/**
	 * 查询网页标题后缀
	 *
	 * @return
	 */
	@GetMapping("/webTitleSuffix")
	public Result getWebTitleSuffix() {
		return Result.ok("请求成功", siteSettingService.getWebTitleSuffix());
	}
}
