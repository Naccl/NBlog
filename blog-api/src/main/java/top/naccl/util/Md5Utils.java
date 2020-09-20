package top.naccl.util;

import org.springframework.util.DigestUtils;

/**
 * @Description: MD5加密
 * @Author: Naccl
 * @Date: 2020-07-19
 */
public class Md5Utils {
	public static String getMd5(CharSequence str) {
		return DigestUtils.md5DigestAsHex(str.toString().getBytes());
	}

	public static void main(String[] args) {

	}
}