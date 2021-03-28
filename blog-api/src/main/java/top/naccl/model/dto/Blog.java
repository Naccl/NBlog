package top.naccl.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import top.naccl.entity.Category;
import top.naccl.entity.Tag;
import top.naccl.entity.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 博客DTO
 * @Author: Naccl
 * @Date: 2020-08-27
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Blog {
	private Long id;
	private String title;//文章标题
	private String firstPicture;//文章首图，用于随机文章展示
	private String content;//文章正文
	private String description;//描述
	private Boolean published;//公开或私密
	private Boolean recommend;//推荐开关
	private Boolean appreciation;//赞赏开关
	private Boolean commentEnabled;//评论开关
	private Boolean top;//是否置顶
	private Date createTime;//创建时间
	private Date updateTime;//更新时间
	private Integer views;//浏览次数
	private Integer words;//文章字数
	private Integer readTime;//阅读时长(分钟)
	private String password;//密码保护

	private User user;//文章作者(因为是个人博客，也可以不加作者字段，暂且加上)
	private Category category;//文章分类
	private List<Tag> tags = new ArrayList<>();//文章标签

	private Object cate;//页面展示层传输的分类对象：正常情况下为 字符串 或 分类id
	private List<Object> tagList;//页面展示层传输的标签对象：正常情况下为 List<Integer>标签id 或 List<String>标签名
}
