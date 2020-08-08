package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.entity.Tag;
import top.naccl.mapper.TagMapper;
import top.naccl.service.TagService;

import java.util.List;

/**
 * @Description: 博客标签业务层实现
 * @Author: Naccl
 * @Date: 2020-07-30
 */
@Service
public class TagServiceImpl implements TagService {
	@Autowired
	TagMapper tagMapper;

	@Override
	public List<Tag> getTagList() {
		return tagMapper.getTagList();
	}

	@Override
	public List<Tag> getTagListByBlogId(Long blogId) {
		return tagMapper.getTagListByBlogId(blogId);
	}

	@Transactional
	@Override
	public int saveTag(Tag tag) {
		return tagMapper.saveTag(tag);
	}

	@Override
	public Tag getTagById(Long id) {
		return tagMapper.getTagById(id);
	}

	@Override
	public Tag getTagByName(String name) {
		return tagMapper.getTagByName(name);
	}

	@Transactional
	@Override
	public int deleteTagById(Long id) {
		return tagMapper.deleteTagById(id);
	}

	@Transactional
	@Override
	public int updateTag(Tag tag) {
		return tagMapper.updateTag(tag);
	}
}
