package top.naccl.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import top.naccl.util.MD5Utils;

/**
 * @Description: 自定义密码加密规则
 * @Author: Naccl
 * @Date: 2020-07-20
 */

public class MyPasswordEncoder implements PasswordEncoder {
	@Override
	public String encode(CharSequence rawPassword) {
		return MD5Utils.getMD5(rawPassword);
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(MD5Utils.getMD5(rawPassword));
	}
}
