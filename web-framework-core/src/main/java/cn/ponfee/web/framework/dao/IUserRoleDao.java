package cn.ponfee.web.framework.dao;

import java.util.List;

import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.UserRole;

/**
 * The database operator interface for t_user_role table
 *
 * @author Ponfee
 */
public interface IUserRoleDao {

    int add(List<UserRole> list);

    int delByUserId(long userId);

    int delByRoleId(long roleId);

    List<Role> queryByUserId(long userId);

    List<String> queryUserPermits(long userId);
}
