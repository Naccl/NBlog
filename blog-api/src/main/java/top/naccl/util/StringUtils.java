package top.naccl.util;

/**
 * @Description: 字符串校验
 * @Author: Naccl
 * @Date: 2020-08-02
 */
public class StringUtils {
	public static boolean isEmpty(String... str) {
		for (String s : str) {
			if (s == null || "".equals(s.trim())) return true;
		}
		return false;
	}
}
