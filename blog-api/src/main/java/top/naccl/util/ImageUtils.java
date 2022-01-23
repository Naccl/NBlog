package top.naccl.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.naccl.config.properties.BlogProperties;
import top.naccl.config.properties.GithubProperties;
import top.naccl.config.properties.UploadProperties;
import top.naccl.exception.BadRequestException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

/**
 * @Description: 图片下载保存工具类
 * @Author: Naccl
 * @Date: 2021-11-11
 */
@Component
public class ImageUtils {
	/**
	 * GitHub上传文件API
	 */
	private static final String githubUploadApi = "https://api.github.com/repos/%s/%s/contents%s/%s";
	/**
	 * jsDelivr的CDN链接
	 */
	private static final String cdnUrl4Github = "https://cdn.jsdelivr.net/gh/%s/%s%s/%s";

	private static RestTemplate restTemplate;

	private static BlogProperties blogProperties;

	private static UploadProperties uploadProperties;

	private static GithubProperties githubProperties;

	@Autowired
	public void setRestTemplate(RestTemplate restTemplate) {
		ImageUtils.restTemplate = restTemplate;
	}

	@Autowired
	public void setBlogProperties(BlogProperties blogProperties) {
		ImageUtils.blogProperties = blogProperties;
	}

	@Autowired
	public void setUploadProperties(UploadProperties uploadProperties) {
		ImageUtils.uploadProperties = uploadProperties;
	}

	@Autowired
	public void setGithubProperties(GithubProperties githubProperties) {
		ImageUtils.githubProperties = githubProperties;
	}

	@AllArgsConstructor
	@Getter
	static class ImageResource {
		byte[] data;
		//图片拓展名 jpg png
		String type;
	}

	public static ImageResource getImageByRequest(String url) {
		ResponseEntity<byte[]> responseEntity = restTemplate.getForEntity(url, byte[].class);
		if ("image".equals(responseEntity.getHeaders().getContentType().getType())) {
			return new ImageResource(responseEntity.getBody(), responseEntity.getHeaders().getContentType().getSubtype());
		}
		throw new BadRequestException("response contentType unlike image");
	}

	public static String saveImage(ImageResource image) throws IOException {
		File folder = new File(uploadProperties.getPath());
		if (!folder.exists()) {
			folder.mkdirs();
		}
		String fileName = UUID.randomUUID() + "." + image.getType();
		FileOutputStream fileOutputStream = new FileOutputStream(uploadProperties.getPath() + fileName);
		fileOutputStream.write(image.getData());
		fileOutputStream.close();
		return blogProperties.getApi() + "/image/" + fileName;
	}

	public static String push2Github(ImageResource image) {
		String fileName = UUID.randomUUID() + "." + image.getType();
		String url = String.format(githubUploadApi, githubProperties.getUsername(), githubProperties.getRepos(), githubProperties.getReposPath(), fileName);
		String imgBase64 = Base64.getEncoder().encodeToString(image.getData());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "token " + githubProperties.getToken());

		HashMap<String, String> body = new HashMap<>(2);
		body.put("message", "Add files via NBlog");
		body.put("content", imgBase64);

		HttpEntity httpEntity = new HttpEntity(body, headers);
		restTemplate.put(url, httpEntity);

		return String.format(cdnUrl4Github, githubProperties.getUsername(), githubProperties.getRepos(), githubProperties.getReposPath(), fileName);
	}
}
