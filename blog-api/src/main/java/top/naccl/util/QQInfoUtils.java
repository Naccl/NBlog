package top.naccl.util;

import org.springframework.web.client.RestTemplate;
import top.naccl.util.upload.UploadUtils;

import java.io.UnsupportedEncodingException;

/**
 * @Description: 获取QQ昵称头像信息
 * @Author: Naccl
 * @Date: 2020-09-10
 */
public class QQInfoUtils {
	private static RestTemplate restTemplate = new RestTemplate();
	private static final String QQ_NICKNAME_URL = "https://r.qzone.qq.com/fcg-bin/cgi_get_portrait.fcg?uins={1}";
	private static final String QQ_AVATAR_URL = "https://q.qlogo.cn/g?b=qq&nk=%s&s=100";

	/**
	 * 获取QQ昵称
	 *
	 * @param qq
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getQQNickname(String qq) throws UnsupportedEncodingException {
		String res = restTemplate.getForObject(QQ_NICKNAME_URL, String.class, qq);
		byte[] bytes = res.getBytes("iso-8859-1");
		String nickname = new String(bytes, "gb18030").split(",")[6].replace("\"", "");
		if ("".equals(nickname)) {
			return "nickname";
		}
		return nickname;
	}

	/**
	 * 从网络获取QQ头像数据
	 *
	 * @param qq
	 * @return
	 */
	private static UploadUtils.ImageResource getImageResourceByQQ(String qq) {
		return UploadUtils.getImageByRequest(String.format(QQ_AVATAR_URL, qq));
	}

	/**
	 * 获取QQ头像URL
	 *
	 * @param qq
	 * @return
	 * @throws Exception
	 */
	public static String getQQAvatarUrl(String qq) throws Exception {
		return UploadUtils.upload(getImageResourceByQQ(qq));
	}

	/**
	 * 判断是否QQ号
	 *
	 * @param number
	 * @return
	 */
	public static boolean isQQNumber(String number) {
		return number.matches("^[1-9][0-9]{4,10}$");
	}
}