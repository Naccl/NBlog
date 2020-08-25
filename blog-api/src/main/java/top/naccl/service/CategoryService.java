package top.naccl.service;

import top.naccl.entity.Category;
import top.naccl.model.vo.BlogInfo;

import java.util.List;

public interface CategoryService {
	List<Category> getCategoryList();

	List<Category> getCategoryListNotId();

	List<BlogInfo> getBlogInfoListByCategoryNameAndIsPublished(String categoryName);

	void saveCategory(Category category);

	Category getCategoryById(Long id);

	Category getCategoryByName(String name);

	void deleteCategoryById(Long id);

	void updateCategory(Category category);
}
