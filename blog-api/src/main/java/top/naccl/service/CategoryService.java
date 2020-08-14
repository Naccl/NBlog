package top.naccl.service;

import top.naccl.entity.Category;

import java.util.List;

public interface CategoryService {
	List<Category> getCategoryList();

	void saveCategory(Category category);

	Category getCategoryById(Long id);

	Category getCategoryByName(String name);

	void deleteCategoryById(Long id);

	void updateCategory(Category category);
}
