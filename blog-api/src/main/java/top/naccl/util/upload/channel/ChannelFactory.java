package top.naccl.util.upload.channel;

import top.naccl.constant.UploadConstants;
import top.naccl.util.common.SpringContextUtils;

/**
 * 文件上传方式
 *
 * @author: Naccl
 * @date: 2022-01-23
 */
public class ChannelFactory {
	/**
	 * 创建文件上传方式
	 *
	 * @param channelName 方式名称
	 * @return 文件上传Channel
	 */
	public static FileUploadChannel getChannel(String channelName) {
		switch (channelName.toLowerCase()) {
			case UploadConstants.LOCAL:
				return SpringContextUtils.getBean(LocalChannel.class);
			case UploadConstants.GITHUB:
				return SpringContextUtils.getBean(GithubChannel.class);
			case UploadConstants.UPYUN:
				return SpringContextUtils.getBean(UpyunChannel.class);
		}
		throw new RuntimeException("Unsupported value in [application.properties]: [upload.channel]");
	}
}
