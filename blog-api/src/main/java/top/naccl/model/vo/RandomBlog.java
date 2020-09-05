package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.naccl.entity.Category;
import top.naccl.entity.Tag;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 随机博客
 * @Author: Naccl
 * @Date: 2020-08-17
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RandomBlog {
	private Long id;
	private String title;//文章标题
	private String password;//文章密码
	private Boolean privacy;//是否私密文章

	private Category category;//文章分类
	private List<Tag> tags = new ArrayList<>();//文章标签
}
