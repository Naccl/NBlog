package top.naccl.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.naccl.entity.SiteSetting;
import top.naccl.model.vo.Result;
import top.naccl.service.SiteSettingService;

import java.util.List;
import java.util.Map;

/**
 * @Description: 站点设置后台管理
 * @Author: Naccl
 * @Date: 2020-08-09
 */
@RestController
@RequestMapping("/admin")
public class SiteSettingController {
	@Autowired
	SiteSettingService siteSettingService;

	@GetMapping("/siteSettings")
	public Result siteSettings() {
		try {
			Map<String, List<SiteSetting>> typeMap = siteSettingService.getList();
			return Result.ok("请求成功", typeMap);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}

}
