package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.model.entity.Category;
import top.naccl.mapper.CategoryMapper;
import top.naccl.service.CategoryService;

import java.util.List;

/**
 * @Description: 博空分类业务层实现
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

	@Transactional
	@Override
	public int saveCategory(Category category) {
		return categoryMapper.saveCategory(category);
	}

	@Override
	public Category getCategoryById(Long id) {
		return categoryMapper.getCategoryById(id);
	}

	@Override
	public Category getCategoryByName(String name) {
		return categoryMapper.getCategoryByName(name);
	}

	@Transactional
	@Override
	public int deleteCategoryById(Long id) {
		return categoryMapper.deleteCategoryById(id);
	}

	@Transactional
	@Override
	public int updateCategory(Category category) {
		return categoryMapper.updateCategory(category);
	}
}
