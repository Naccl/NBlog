package top.naccl.service;

import top.naccl.entity.Category;

import java.util.List;

public interface CategoryService {
	List<Category> getCategoryList();

	int saveCategory(Category category);

	Category getCategoryById(Long id);
}
