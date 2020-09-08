package top.naccl.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 友链DTO
 * @Author: Naccl
 * @Date: 2020-09-08
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Friend {
	private Long id;
	private String nickname;//昵称
	private String description;//描述
	private String website;//站点
	private String avatar;//头像
	private Boolean published;//公开或隐藏
}
