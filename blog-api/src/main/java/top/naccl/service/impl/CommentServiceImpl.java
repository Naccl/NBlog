package top.naccl.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.naccl.model.entity.Comment;
import top.naccl.mapper.CommentMapper;
import top.naccl.service.CommentService;

import java.util.List;

/**
 * @Description: 博空评论业务层实现
 * @Author: Naccl
 * @Date: 2020-08-03
 */
@Service
public class CommentServiceImpl implements CommentService {
	@Autowired
	CommentMapper commentMapper;

	@Override
	public List<Comment> getListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId) {
		List<Comment> comments = commentMapper.getListByPageAndParentCommentId(page, blogId, parentCommentId);
		for (Comment c : comments) {
			//递归查询子评论及其子评论
			List<Comment> replyComments = getListByPageAndParentCommentId(page, blogId, c.getId());
			c.setReplyComments(replyComments);
		}
		return comments;
	}

	@Override
	public List<Comment> getListByParentCommentId(Long parentCommentId) {
		return commentMapper.getListByParentCommentId(parentCommentId);
	}

	@Transactional
	@Override
	public int updateCommentPublishedById(Long commentId, Boolean published) {
		return commentMapper.updateCommentPublishedById(commentId, published);
	}

	@Transactional
	@Override
	public int updateCommentNoticeById(Long commentId, Boolean notice) {
		return commentMapper.updateCommentNoticeById(commentId, notice);
	}

	@Transactional
	@Override
	public int deleteCommentById(Long commentId) {
		List<Comment> comments = getAllReplyComments(commentId);
		for (Comment c : comments) {
			delete(c);
		}
		return commentMapper.deleteCommentById(commentId);
	}

	@Transactional
	@Override
	public int updateComment(Comment comment) {
		return commentMapper.updateComment(comment);
	}

	/**
	 * 递归删除子评论
	 *
	 * @param comment 需要删除子评论的父评论
	 * @return
	 */
	private int delete(Comment comment) {
		for (Comment c : comment.getReplyComments()) {
			delete(c);
		}
		return commentMapper.deleteCommentById(comment.getId());
	}

	/**
	 * 按id递归查询子评论
	 *
	 * @param parentCommentId 需要查询子评论的父评论id
	 * @return
	 */
	private List<Comment> getAllReplyComments(Long parentCommentId) {
		List<Comment> comments = commentMapper.getListByParentCommentId(parentCommentId);
		for (Comment c : comments) {
			List<Comment> replyComments = getAllReplyComments(c.getId());
			c.setReplyComments(replyComments);
		}
		return comments;
	}
}
