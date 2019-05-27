package cn.ponfee.web.framework.dao.mapper;

import java.util.List;

import cn.ponfee.web.framework.model.Permit;

/**
 * The mybatis mapper for table t_permit
 * 
 * @author Ponfee
 */
public interface PermitMapper {

    int insert(Permit permit);

    int update(Permit permit);

    int deleteByPermitIds(List<String> ids);

    Permit getByPermitId(String id);

    List<Permit> listAll();

}
