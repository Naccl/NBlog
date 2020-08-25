package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Tag;
import top.naccl.model.vo.BlogInfo;

import java.util.List;

/**
 * @Description: 博客标签持久层接口
 * @Author: Naccl
 * @Date: 2020-07-30
 */
@Mapper
@Repository
public interface TagMapper {
	List<Tag> getTagList();

	List<Tag> getTagListNotId();

	List<Tag> getTagListByBlogId(Long blogId);

	List<BlogInfo> getBlogInfoListByTagNameAndIsPublished(String tagName);

	int saveTag(Tag tag);

	Tag getTagById(Long id);

	Tag getTagByName(String name);

	int deleteTagById(Long id);

	int updateTag(Tag tag);
}
