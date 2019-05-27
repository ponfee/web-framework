package cn.ponfee.web.framework.dao.mapper;

import java.util.List;

import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.UserRole;

/**
 * The mybatis mapper for table t_user_role
 * 
 * @author Ponfee
 */
public interface UserRoleMapper {

    int insert(List<UserRole> list);

    int delByUserId(long userId);

    int delByRoleId(long roleId);

    List<Role> queryByUserId(long userId);

    List<String> queryUserPermits(long userId);
}
