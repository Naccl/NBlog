package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.About;
import top.naccl.mapper.AboutMapper;
import top.naccl.service.AboutService;
import top.naccl.util.markdown.MarkdownUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: 关于我页面业务层实现
 * @Author: Naccl
 * @Date: 2020-08-31
 */
@Service
public class AboutServiceImpl implements AboutService {
	@Autowired
	AboutMapper aboutMapper;

	@Override
	public Map<String, String> getAboutInfo() {
		List<About> abouts = aboutMapper.getList();
		Map<String, String> map = new HashMap<>();
		for (About about : abouts) {
			if ("content".equals(about.getNameEn())) {
				about.setValue(MarkdownUtils.markdownToHtmlExtensions(about.getValue()));
			}
			map.put(about.getNameEn(), about.getValue());
		}
		return map;
	}
}
