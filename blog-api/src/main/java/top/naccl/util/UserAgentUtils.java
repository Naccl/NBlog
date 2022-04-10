package top.naccl.util;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: UserAgent解析工具类
 * @Author: Naccl
 * @Date: 2020-11-30
 */
@Component
public class UserAgentUtils {
	private UserAgentAnalyzer uaa;

	public UserAgentUtils() {
		this.uaa = UserAgentAnalyzer
				.newBuilder()
				.hideMatcherLoadStats()
				.withField(UserAgent.OPERATING_SYSTEM_NAME_VERSION_MAJOR)
				.withField(UserAgent.AGENT_NAME_VERSION)
				.build();
	}

	/**
	 * 从User-Agent解析客户端操作系统和浏览器版本
	 *
	 * @param userAgent
	 * @return
	 */
	public Map<String, String> parseOsAndBrowser(String userAgent) {
		UserAgent agent = uaa.parse(userAgent);
		String os = agent.getValue(UserAgent.OPERATING_SYSTEM_NAME_VERSION_MAJOR);
		String browser = agent.getValue(UserAgent.AGENT_NAME_VERSION);
		Map<String, String> map = new HashMap<>(4);
		map.put("os", os);
		map.put("browser", browser);
		return map;
	}
}
