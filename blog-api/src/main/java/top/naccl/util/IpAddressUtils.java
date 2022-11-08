package top.naccl.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description: ip记录
 * @Author: Naccl
 * @Date: 2020-08-18
 */
@Slf4j
@Component
public class IpAddressUtils {
	/**
	 * 在Nginx等代理之后获取用户真实IP地址
	 *
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
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
		return StringUtils.substringBefore(ip, ",");
	}

	private static Searcher searcher;
	private static Method method;

	/**
	 * 在服务启动时加载 ip2region.db 到内存中
	 * 解决打包jar后找不到 ip2region.db 的问题
	 *
	 * @throws Exception 出现异常应该直接抛出终止程序启动，避免后续invoke时出现更多错误
	 */
	@PostConstruct
	private void initIp2regionResource() throws Exception {
		InputStream inputStream = new ClassPathResource("/ipdb/ip2region.xdb").getInputStream();
		//将 ip2region.db 转为 ByteArray
		byte[] dbBinStr = FileCopyUtils.copyToByteArray(inputStream);
		// 2、使用上述的 dbBinStr 创建一个完全基于内存的查询对象。
		searcher = new Searcher(null, null, dbBinStr);
		//二进制方式初始化 DBSearcher，需要使用基于内存的查找算法 memorySearch
		method = searcher.getClass().getMethod("search", String.class);
	}

	/**
	 * 根据ip从 ip2region.db 中获取地理位置
	 *
	 * @param ip
	 * @return
	 */
	public static String getCityInfo(String ip) {
		try {
			String ipInfo = (String) method.invoke(searcher, ip);
			if (!StringUtils.isEmpty(ipInfo)) {
				ipInfo = ipInfo.replace("|0", "");
				ipInfo = ipInfo.replace("0|", "");
			}
			return ipInfo;
		} catch (Exception e) {
			log.error("getCityInfo exception:", e);
		}
		return "";
	}
	public static void main(String[] args) throws Exception {
		IpAddressUtils ipAddressUtils = new IpAddressUtils();
		ipAddressUtils.initIp2regionResource();
		String cityInfo = IpAddressUtils.getCityInfo("14.215.177.39");
		System.out.println(cityInfo);
	}
}