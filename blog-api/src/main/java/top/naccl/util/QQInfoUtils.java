package top.naccl.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import top.naccl.model.vo.QqResultVO;
import top.naccl.model.vo.QqVO;
import top.naccl.util.upload.UploadUtils;

/**
 * @Description: 获取QQ昵称头像信息
 * @Author: Naccl
 * @Date: 2020-09-10
 */
public class QQInfoUtils {
	private static RestTemplate restTemplate = new RestTemplate();
	// 原接口半失效，需要提供cookie才可使用，暂时替换为备用接口，感谢 苏晓晴 大佬友情提供
	private static final String QQ_NICKNAME_URL = "https://api.toubiec.cn/api/qqinfo_v4.php?qq={1}";
	private static final String QQ_AVATAR_URL = "https://q.qlogo.cn/g?b=qq&nk=%s&s=100";

	/**
	 * 获取QQ昵称
	 *
	 * @param qq qq
	 */
	public static String getQQNickname(String qq) {
		QqResultVO qqResultVO = restTemplate.getForObject(QQ_NICKNAME_URL, QqResultVO.class, qq);
		if (qqResultVO != null) {
			return new ObjectMapper().convertValue(qqResultVO.getData(), QqVO.class).getName();
		}
		return "nickname";
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
