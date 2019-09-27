package cn.ponfee.web.framework.dao.mapper;

import org.apache.ibatis.annotations.Param;

public interface TestMapper {

    int delete(@Param("datasource") String datasource, @Param("index") String index);
}
