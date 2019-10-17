package cn.ponfee.web.framework.dao.mapper;

import org.apache.ibatis.annotations.Param;

import cn.ponfee.web.framework.model.RegisterContention;

/**
 * The mybatis mapper for table t_register_contention
 * 
 * @author Ponfee
 */
public interface RegisterContentionMapper {

    int insert(RegisterContention rc);

    String get(@Param("typ") String typ, @Param("key") String key);

}
