package top.naccl.service;

import top.naccl.entity.Category;
import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;

import java.util.List;

public interface RedisService {
	PageResult<BlogInfo> getBlogInfoPageResultByHash(String hash, Integer pageNum);

	void setBlogInfoPageResultToHash(String hash, Integer pageNum, Object object);

	List<Category> getCategoryNameListByValue(String key);

	void setCategoryNameListToValue(String key, List<Category> categoryNameList);
}
