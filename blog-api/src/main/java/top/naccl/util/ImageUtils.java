package top.naccl.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.naccl.exception.BadRequestException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @Description: 图片下载保存工具类
 * @Author: Naccl
 * @Date: 2021-11-11
 */
@Component
public class ImageUtils {
	private static RestTemplate restTemplate = new RestTemplate();
	private static String uploadPath;
	private static String serverUrl;

	@Value("${upload.path}")
	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	@Value("${custom.url.api}")
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public static ImageResource getImageByRequest(String url) {
		ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
		if ("image".equals(responseEntity.getHeaders().getContentType().getType())) {
			return new ImageResource(responseEntity.getBody(), responseEntity.getHeaders().getContentType().getSubtype());
		}
		throw new BadRequestException("response contentType unlike image");
	}

	public static String saveImage(ImageResource image) throws IOException {
		File folder = new File(uploadPath);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String fileName = UUID.randomUUID() + "." + image.getType();
		FileOutputStream fileOutputStream = new FileOutputStream(uploadPath + fileName);
		fileOutputStream.write(image.getData());
		fileOutputStream.close();
		return serverUrl + "/image/" + fileName;
	}

	@AllArgsConstructor
	@Getter
	static class ImageResource {
		byte[] data;
		//图片拓展名 jpg png
		String type;
	}
}
