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
				.withField("OperatingSystemNameVersionMajor")
				.withField("AgentNameVersion")
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
		String os = agent.getValue("OperatingSystemNameVersionMajor");
		String browser = agent.getValue("AgentNameVersion");
		Map<String, String> map = new HashMap<>();
		map.put("os", os);
		map.put("browser", browser);
		return map;
	}
}
