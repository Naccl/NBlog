package top.naccl.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 城市访客数量
 * @Author: Naccl
 * @Date: 2021-02-26
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CityVisitor {
	private String city;//城市名称
	private Integer uv;//独立访客数量
}
