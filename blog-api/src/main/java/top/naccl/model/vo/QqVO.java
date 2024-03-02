package top.naccl.model.vo;

import lombok.Data;

/**
 * @author raxcl
 * @date 2024-01-19 9:54:53
 */
@Data
public class QqVO {
    /**
     * qq号
     */
    private Long qq;

    /**
     * qq昵称
     */
    private String name;

    /**
     * qq邮箱
     */
    private String email;

    /**
     * qq头像
     */
    private String avatar;
}
