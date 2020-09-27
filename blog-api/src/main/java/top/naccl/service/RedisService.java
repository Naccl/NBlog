package top.naccl.service;

import top.naccl.model.vo.BlogInfo;
import top.naccl.model.vo.PageResult;

public interface RedisService {
	PageResult<BlogInfo> getPageResultByHash(String hash, Integer pageNum);

	void setPageResultToHash(String hash, Integer pageNum, Object object);
}
