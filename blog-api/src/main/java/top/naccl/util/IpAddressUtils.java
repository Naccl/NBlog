package top.naccl.util;

import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description: ip记录
 * @Author: Naccl
 * @Date: 2020-08-18
 */
@Slf4j
public class IpAddressUtils {
	/**
	 * 在Nginx等代理之后获取用户真实IP地址
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
			if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
				//根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					log.error("getIpAddress exception:", e);
				}
				ip = inet.getHostAddress();
			}
		}
		return ip;
	}

	/**
	 * 根据ip从 ip2region.db 中获取地理位置
	 *
	 * @param ip
	 * @return
	 */
	public static String getCityInfo(String ip) {
		if (ip == null || !Util.isIpAddress(ip)) {
			log.error("Error: Invalid ip address");
			return null;
		}
		try {
			DbConfig config = new DbConfig();
			String dbPath = IpAddressUtils.class.getResource("/ipdb/ip2region.db").getPath();
			DbSearcher searcher = new DbSearcher(config, dbPath);
			Method method = searcher.getClass().getMethod("btreeSearch", String.class);
			DataBlock dataBlock = (DataBlock) method.invoke(searcher, ip);
			String ipInfo = dataBlock.getRegion();
			if (!StringUtils.isEmpty(ipInfo)) {
				ipInfo = ipInfo.replace("|0", "");
				ipInfo = ipInfo.replace("0|", "");
			}
			return ipInfo;
		} catch (Exception e) {
			log.error("getCityInfo exception:", e);
		}
		return null;
	}
}