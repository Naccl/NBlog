package top.naccl.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Description: 字符串校验
 * @Author: Naccl
 * @Date: 2020-08-02
 */
public class StringUtils {
	/**
	 * 判断字符串是否为空
	 *
	 * @param str 待校验字符串
	 * @return
	 */
	public static boolean isEmpty(String... str) {
		for (String s : str) {
			if (s == null || "".equals(s.trim())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断字符串中是否包含特殊字符
	 *
	 * @param str 待校验字符串
	 * @return
	 */
	public static boolean hasSpecialChar(String... str) {
		for (String s : str) {
			if (s.contains("%") || s.contains("_") || s.contains("[") || s.contains("#") || s.contains("*")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 截取字符串
	 *
	 * @param str   原始字符串
	 * @param start 起始位置
	 * @param end   结束位置
	 * @return
	 */
	public static String substring(String str, int start, int end) {
		if (str == null || "".equals(str)) {
			return "";
		}
		if (start < 0 || end < 0) {
			return str;
		}
		if (end > str.length()) {
			end = str.length();
		}
		if (start >= end) {
			return "";
		}
		return str.substring(start, end);
	}

	/**
	 * 获取堆栈信息
	 *
	 * @param throwable 异常
	 * @return
	 */
	public static String getStackTrace(Throwable throwable) {
		StringWriter sw = new StringWriter();
		try (PrintWriter pw = new PrintWriter(sw)) {
			throwable.printStackTrace(pw);
			return sw.toString();
		}
	}
}
