package top.naccl.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.DigestUtils;

/**
 * @Description: 加密工具类
 * @Author: Naccl
 * @Date: 2020-11-17
 */
public class EncryptUtils {
	private static final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

	public static String getMd5(CharSequence str) {
		return DigestUtils.md5DigestAsHex(str.toString().getBytes());
	}

	public static String getBC(CharSequence rawPassword) {
		return bCryptPasswordEncoder.encode(rawPassword);
	}

	public static boolean matchBC(CharSequence rawPassword, String encodedPassword) {
		return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
	}
}
