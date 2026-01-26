package top.naccl.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @Description: 访问日志Excel导出模型
 * @Author: Naccl
 * @Date: 2025-01-25
 */
@Getter
@Setter
public class VisitLogExcel {
    @ExcelProperty(value = "序号", index = 0)
    @ColumnWidth(10)
    private Integer serialNumber;

    @ExcelProperty(value = "访客标识", index = 1)
    @ColumnWidth(32)
    private String uuid;

    @ExcelProperty(value = "访问行为", index = 2)
    @ColumnWidth(20)
    private String behavior;

    @ExcelProperty(value = "访问内容", index = 3)
    @ColumnWidth(50)
    private String content;

    @ExcelProperty(value = "IP", index = 4)
    @ColumnWidth(15)
    private String ip;

    @ExcelProperty(value = "IP来源", index = 5)
    @ColumnWidth(20)
    private String ipSource;

    @ExcelProperty(value = "操作系统", index = 6)
    @ColumnWidth(20)
    private String os;

    @ExcelProperty(value = "浏览器", index = 7)
    @ColumnWidth(20)
    private String browser;

    @ExcelProperty(value = "访问时间", index = 8)
    @ColumnWidth(20)
    private String createTime;
}