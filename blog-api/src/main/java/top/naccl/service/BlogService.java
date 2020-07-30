package top.naccl.service;

import top.naccl.entity.Blog;

import java.util.List;

public interface BlogService {
	List<Blog> getListByTitleOrType(String query, Integer typeId);

	void deleteBlogById(Long id);
}
