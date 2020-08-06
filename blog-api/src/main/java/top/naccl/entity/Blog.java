package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 博客文章
 * @Author: Naccl
 * @Date: 2020-07-26
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Blog {
	private Long id;
	private String title;//文章标题
	private String content;//文章正文
	private String description;//描述
	private String flag;//原创、转载、翻译
	private boolean published;//发布或草稿
	private boolean recommend;//推荐开关
	private boolean appreciation;//赞赏开关
	private boolean shareStatement;//版权声明开关
	private boolean commentEnabled;//评论开关
	private Date createTime;//创建时间
	private Date updateTime;//更新时间
	private Integer views;//浏览次数
	private Integer words;//文章字数
	private Integer readTime;//阅读时长(分钟)

	private User user;//文章作者(因为是个人博客，也可以不加作者字段，暂且加上)
	private Category category;//文章分类
	private List<Tag> tags = new ArrayList<>();//文章标签
}
