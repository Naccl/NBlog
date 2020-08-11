package top.naccl.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

	@PutMapping("/siteSettings")
	public Result updateAll(@RequestBody Map<String, Object> map) {
		try {
			List<LinkedHashMap> siteSettings = (List<LinkedHashMap>) map.get("settings");
			List<Integer> deleteIds = (List<Integer>) map.get("deleteIds");
			System.out.println(deleteIds);
			for (Integer id : deleteIds) {//删除
				int r = siteSettingService.deleteSiteSettingById(id);
				if (r != 1) {
					return Result.error("删除失败");
				}
			}
			for (LinkedHashMap s : siteSettings) {
				JSONObject siteSettingJsonObject = new JSONObject(s);
				SiteSetting siteSetting = JSON.toJavaObject(siteSettingJsonObject, SiteSetting.class);
				if (siteSetting.getId() != null) {//修改
					int r = siteSettingService.updateSiteSetting(siteSetting);
					if (r != 1) {
						return Result.error("修改失败");
					}
				} else {//添加
					int r = siteSettingService.saveSiteSetting(siteSetting);
					if (r != 1) {
						return Result.error("添加失败");
					}
				}
			}
			return Result.ok("更新成功");
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error();
		}
	}
}
