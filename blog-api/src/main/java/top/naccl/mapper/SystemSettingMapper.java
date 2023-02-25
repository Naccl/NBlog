package top.naccl.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.naccl.entity.SystemSetting;

import java.util.List;

/**
 * @Description: 系统配置持久层接口
 * @Author: Naccl
 * @Date: 2023-02-25
 */
@Mapper
@Repository
public interface SystemSettingMapper {
	List<SystemSetting> list();

	int updateValueById(SystemSetting systemSetting);
}
