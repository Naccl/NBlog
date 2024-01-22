package top.naccl.model.vo;

import lombok.Data;

import java.util.Map;

/**
 * @author raxcl
 * @date 2024-01-19 9:54:53
 */
@Data
public class QqResultVO {
    private String success;

    private String msg;

    private Map<String, Object> data;

    private String time;

    private  String api_vers;
}
