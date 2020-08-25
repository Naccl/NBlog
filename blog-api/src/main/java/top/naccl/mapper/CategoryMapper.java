package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Category;
import top.naccl.model.vo.BlogInfo;

import java.util.List;

/**
 * @Description: 博客分类持久层接口
 * @Author: Naccl
 * @Date: 2020-07-29
 */
@Mapper
@Repository
public interface CategoryMapper {
	List<Category> getCategoryList();

	List<Category> getCategoryListNotId();

	List<BlogInfo> getBlogInfoListByCategoryNameAndIsPublished(String categoryName);

	int saveCategory(Category category);

	Category getCategoryById(Long id);

	Category getCategoryByName(String name);

	int deleteCategoryById(Long id);

	int updateCategory(Category category);
}
