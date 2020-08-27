package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.naccl.entity.Comment;
import top.naccl.model.vo.PageComment;

import java.util.List;

/**
 * @Description: 博客评论持久层接口
 * @Author: Naccl
 * @Date: 2020-08-03
 */
@Mapper
@Repository
public interface CommentMapper {
	List<Comment> getListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId);

	List<Comment> getListByParentCommentId(Long parentCommentId);

	List<PageComment> getPageCommentListByPageAndParentCommentId(Integer page, Long blogId, Long parentCommentId);

	int updateCommentPublishedById(Long commentId, Boolean published);

	int updateCommentNoticeById(Long commentId, Boolean notice);

	int deleteCommentById(Long commentId);

	int updateComment(Comment comment);

	int countByPageAndIsPublished(Integer page, Long blogId);

	int saveComment(top.naccl.model.dto.Comment comment);
}
