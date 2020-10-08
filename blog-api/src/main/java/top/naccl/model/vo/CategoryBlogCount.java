package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 分类和博客数量
 * @Author: Naccl
 * @Date: 2020-10-08
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryBlogCount {
	private Long id;
	private String name;//分类名
	private Integer value;//分类下博客数量
}
