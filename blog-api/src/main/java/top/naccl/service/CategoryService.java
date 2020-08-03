package top.naccl.service;

import top.naccl.model.entity.Category;

import java.util.List;

public interface CategoryService {
	List<Category> getCategoryList();

	int saveCategory(Category category);

	Category getCategoryById(Long id);

	Category getCategoryByName(String name);

	int deleteCategoryById(Long id);

	int updateCategory(Category category);
}
