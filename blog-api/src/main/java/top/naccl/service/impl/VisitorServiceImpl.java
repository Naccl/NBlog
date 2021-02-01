package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.entity.Visitor;
import top.naccl.exception.PersistenceException;
import top.naccl.mapper.VisitorMapper;
import top.naccl.service.VisitorService;
import top.naccl.util.IpAddressUtils;
import top.naccl.util.UserAgentUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description: 访客记录业务层实现
 * @Author: Naccl
 * @Date: 2021-01-31
 */
@Service
public class VisitorServiceImpl implements VisitorService {
	@Autowired
	VisitorMapper visitorMapper;
	@Autowired
	UserAgentUtils userAgentUtils;

	@Override
	public List<Visitor> getVisitorList() {
		return visitorMapper.getVisitorList();
	}

	@Override
	public boolean hasUUID(String uuid) {
		return visitorMapper.hasUUID(uuid) == 0 ? false : true;
	}

	@Transactional
	@Override
	public void saveVisitor(Visitor visitor) {
		String ipSource = IpAddressUtils.getCityInfo(visitor.getIp());
		Map<String, String> userAgentMap = userAgentUtils.parseOsAndBrowser(visitor.getUserAgent());
		String os = userAgentMap.get("os");
		String browser = userAgentMap.get("browser");
		visitor.setIpSource(ipSource);
		visitor.setOs(os);
		visitor.setBrowser(browser);
		if (visitorMapper.saveVisitor(visitor) != 1) {
			throw new PersistenceException("访客添加失败");
		}
	}

	@Transactional
	@Override
	public void deleteVisitorById(Long id) {
		if (visitorMapper.deleteVisitorById(id) != 1) {
			throw new PersistenceException("删除访客失败");
		}
	}
}
