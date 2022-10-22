package top.naccl.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @Description: UserAgent解析DTO
 * @Author: Naccl
 * @Date: 2022-10-13
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserAgentDTO {
	private String os;
	private String browser;
}
