package top.naccl.service;

import top.naccl.model.entity.Tag;

import java.util.List;

public interface TagService {
	List<Tag> getTagList();

	int saveTag(Tag tag);

	Tag getTagById(Long id);

	Tag getTagByName(String name);

	int deleteTagById(Long id);

	int updateTag(Tag tag);
}
