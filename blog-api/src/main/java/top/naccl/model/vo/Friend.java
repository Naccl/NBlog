package top.naccl.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 友链VO
 * @Author: Naccl
 * @Date: 2020-09-08
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Friend {
	private String nickname;//昵称
	private String description;//描述
	private String website;//站点
	private String avatar;//头像
}
