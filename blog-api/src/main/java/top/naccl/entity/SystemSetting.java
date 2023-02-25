package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 系统配置
 * @Author: Naccl
 * @Date: 2023-02-25
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SystemSetting {
	private Long id;
	private String nameEn;
	private String nameZh;
	private String value;
	private Integer type;
	private Integer sortOrder;
}
