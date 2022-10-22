package top.naccl.util.upload.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.naccl.config.properties.GithubProperties;
import top.naccl.util.upload.UploadUtils;

import java.util.Base64;
import java.util.HashMap;
import java.util.UUID;

/**
 * GitHub上传方式
 *
 * @author: Naccl
 * @date: 2022-01-23
 */
@Lazy
@Component
public class GithubChannel implements FileUploadChannel {
	/**
	 * GitHub上传文件API
	 */
	private static final String githubUploadApi = "https://api.github.com/repos/%s/%s/contents%s/%s";
	/**
	 * jsDelivr的CDN链接
	 */
	private static final String cdnUrl4Github = "https://fastly.jsdelivr.net/gh/%s/%s%s/%s";

	@Autowired
	private GithubProperties githubProperties;
	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 将图片上传至GitHub仓库，并返回CDN链接
	 *
	 * @param image 需要上传的图片
	 * @return 访问图片的URL
	 */
	@Override
	public String upload(UploadUtils.ImageResource image) {
		String fileName = UUID.randomUUID() + "." + image.getType();
		String url = String.format(githubUploadApi, githubProperties.getUsername(), githubProperties.getRepos(), githubProperties.getReposPath(), fileName);
		String imgBase64 = Base64.getEncoder().encodeToString(image.getData());

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "token " + githubProperties.getToken());

		HashMap<String, String> body = new HashMap<>(4);
		body.put("message", "Add files via NBlog");
		body.put("content", imgBase64);

		HttpEntity httpEntity = new HttpEntity(body, headers);
		restTemplate.put(url, httpEntity);

		return String.format(cdnUrl4Github, githubProperties.getUsername(), githubProperties.getRepos(), githubProperties.getReposPath(), fileName);
	}
}
