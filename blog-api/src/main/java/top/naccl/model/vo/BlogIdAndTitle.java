package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 评论管理页面按博客title查询评论
 * @Author: Naccl
 * @Date: 2020-08-03
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class BlogIdAndTitle {
	private Long id;
	private String title;
}
