package top.naccl.service;

import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;

import java.util.List;

public interface RedisService {
	PageResult<BlogInfo> getBlogInfoPageResultByHash(String hash, Integer pageNum);

	void saveBlogInfoPageResultToHash(String hash, Integer pageNum, Object object);

	<T> List<T> getListByValue(String key);

	<T> void saveListToValue(String key, List<T> list);
}
