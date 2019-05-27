package cn.ponfee.web.framework.dao;

import java.util.List;

import cn.ponfee.web.framework.model.RolePermit;

/**
 * The Role Permit Dao Interface
 *
 * @author Ponfee
 */
public interface IRolePermitDao {

    int add(List<RolePermit> list);

    int delByRoleId(long roleId);

    int delByPermitId(String permitId);

    List<String> queryPermitsByRoleId(long roleId);
}
