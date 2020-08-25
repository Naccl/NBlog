package top.naccl.service;

import top.naccl.entity.Moment;

import java.util.List;

public interface MomentService {
	List<Moment> getMomentList();

	List<top.naccl.model.vo.Moment> getMomentListByPublished();

	void addLikeByMomentId(Long momentId);

	void updateMomentPublishedById(Long momentId, Boolean published);
}
