package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.entity.Moment;
import top.naccl.exception.PersistenceException;
import top.naccl.mapper.MomentMapper;
import top.naccl.service.MomentService;
import top.naccl.util.markdown.MarkdownUtils;

import java.util.List;

/**
 * @Description: 博客动态业务层实现
 * @Author: Naccl
 * @Date: 2020-08-24
 */
@Service
public class MomentServiceImpl implements MomentService {
	@Autowired
	MomentMapper momentMapper;

	@Override
	public List<Moment> getMomentList() {
		return momentMapper.getMomentList();
	}

	@Override
	public List<top.naccl.model.vo.Moment> getMomentListByPublished() {
		List<top.naccl.model.vo.Moment> moments = momentMapper.getMomentListByPublished();
		for (top.naccl.model.vo.Moment moment : moments) {
			moment.setContent(MarkdownUtils.markdownToHtmlExtensions(moment.getContent()));
		}
		return moments;
	}

	@Transactional
	@Override
	public void addLikeByMomentId(Long momentId) {
		if (momentMapper.addLikeByMomentId(momentId) != 1) {
			throw new PersistenceException("操作失败");
		}
		return;
	}

	@Transactional
	@Override
	public void updateMomentPublishedById(Long momentId, Boolean published) {
		if (momentMapper.updateMomentPublishedById(momentId, published) != 1) {
			throw new PersistenceException("操作失败");
		}
	}
}
