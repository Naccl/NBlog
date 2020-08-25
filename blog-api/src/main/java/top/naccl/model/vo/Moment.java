package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @Description: 动态vo
 * @Author: Naccl
 * @Date: 2020-08-25
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Moment {
	private Long id;
	private String content;//动态内容
	private Date createTime;//创建时间
	private Integer likes;//点赞数量
}
