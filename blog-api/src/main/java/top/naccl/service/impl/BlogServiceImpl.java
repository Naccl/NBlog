package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.Blog;
import top.naccl.mapper.BlogMapper;
import top.naccl.service.BlogService;

import java.util.List;

/**
 * @Description: 博客文章业务层实现
 * @Author: Naccl
 * @Date: 2020-07-29
 */
@Service
public class BlogServiceImpl implements BlogService {
	@Autowired
	BlogMapper blogMapper;

	@Override
	public List<Blog> getListByTitleOrType(String query, Integer typeId) {
		return blogMapper.getListByTitleOrType(query, typeId);
	}
}
