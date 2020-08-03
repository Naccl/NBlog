package top.naccl.service;

import top.naccl.model.entity.Comment;

import java.util.List;

public interface CommentService {
	List<Comment> getListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId);
}
