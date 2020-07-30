package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.naccl.entity.Category;
import top.naccl.mapper.CategoryMapper;
import top.naccl.service.CategoryService;

import java.util.List;

/**
 * @Description:
 * @Author: Naccl
 * @Date: 2020-07-29
 */
@Service
public class CategoryServiceImpl implements CategoryService {
	@Autowired
	CategoryMapper categoryMapper;

	@Override
	public List<Category> getCategoryList() {
		return categoryMapper.getCategoryList();
	}
}
