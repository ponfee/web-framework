package cn.ponfee.web.framework.dao.mapper;

import java.util.List;

import cn.ponfee.web.framework.model.RolePermit;

/**
 * The mybatis mapper for table t_role_permit
 * 
 * @author Ponfee
 */
public interface RolePermitMapper {

    int insert(List<RolePermit> list);

    int delByRoleId(long roleId);

    int delByPermitId(String permitId);

    List<String> queryPermitsByRoleId(long roleId);

}
