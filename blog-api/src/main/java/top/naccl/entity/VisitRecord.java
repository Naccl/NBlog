package top.naccl.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: 访问记录
 * @Author: Naccl
 * @Date: 2021-02-23
 */
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VisitRecord {
	private Long id;
	private Integer pv;//访问量
	private Integer uv;//独立用户
	private String date;//日期"02-23"

	public VisitRecord(Integer pv, Integer uv, String date) {
		this.pv = pv;
		this.uv = uv;
		this.date = date;
	}
}
