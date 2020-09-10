package top.naccl.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Description: 评论DTO
 * @Author: Naccl
 * @Date: 2020-08-27
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
	private Long id;
	private String nickname;//昵称
	private String email;//邮箱
	private String content;//评论内容
	private String avatar;//头像(图片路径)
	private Date createTime;//评论时间
	private String website;//个人网站
	private String ip;//评论者ip地址
	private Boolean published;//公开或回收站
	private Boolean adminComment;//博主回复
	private Integer page;//0普通文章，1关于我页面
	private Boolean notice;//接收邮件提醒
	private Long parentCommentId;//父评论id
	private Long blogId;//所属的文章id
	private String qq;//如果评论昵称为QQ号，则将昵称和头像置为QQ昵称和QQ头像，并将此字段置为QQ号备份
}
