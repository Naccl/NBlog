package top.naccl.util;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Description: AOP工具类
 * @Author: Naccl
 * @Date: 2020-12-02
 */
public class AopUtils {
	private static Set<String> ignoreParams = new HashSet<String>() {
		{
			add("jwt");
		}
	};

	/**
	 * 获取请求参数
	 *
	 * @param joinPoint
	 * @return
	 */
	public static Map<String, Object> getRequestParams(JoinPoint joinPoint) {
		Map<String, Object> map = new LinkedHashMap<>();
		String[] parameterNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
		Object[] args = joinPoint.getArgs();
		for (int i = 0; i < args.length; i++) {
			if (!isIgnoreParams(parameterNames[i]) && !isFilterObject(args[i])) {
				map.put(parameterNames[i], args[i]);
			}
		}
		return map;
	}

	/**
	 * consider if the data is file, httpRequest or response
	 *
	 * @param o the data
	 * @return if match return true, else return false
	 */
	private static boolean isFilterObject(final Object o) {
		return o instanceof HttpServletRequest || o instanceof HttpServletResponse || o instanceof MultipartFile;
	}

	/**
	 * 判断是否忽略参数
	 *
	 * @param params
	 * @return
	 */
	private static boolean isIgnoreParams(String params) {
		return ignoreParams.contains(params);
	}
}
