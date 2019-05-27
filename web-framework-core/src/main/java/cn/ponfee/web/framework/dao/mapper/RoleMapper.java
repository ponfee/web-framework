package cn.ponfee.web.framework.dao.mapper;

import java.util.List;
import java.util.Map;

import cn.ponfee.web.framework.model.Role;

/**
 * The mybatis mapper for table t_role
 *
 * @author Ponfee
 */
public interface RoleMapper {

    int insert(Role role);

    int update(Role role);

    int delete(long id);

    Role getById(long id);

    Role getByRoleCode(String roleCode);

    List<Role> query4list(Map<String, ?> params);
}
