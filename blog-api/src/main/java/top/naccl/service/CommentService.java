package top.naccl.service;

import top.naccl.entity.Comment;
import top.naccl.model.vo.PageComment;

import java.util.List;

public interface CommentService {
	List<Comment> getListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId);

	List<PageComment> getPageCommentList(Integer page, Long blogId, Long parentCommentId);

	void updateCommentPublishedById(Long commentId, Boolean published);

	void updateCommentNoticeById(Long commentId, Boolean notice);

	void deleteCommentById(Long commentId);

	void updateComment(Comment comment);

	int countByPageAndIsPublished(Integer page, Long blogId);
}
