package cn.ponfee.web.framework.dao.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.ponfee.web.framework.dao.IRoleDao;
import cn.ponfee.web.framework.dao.mapper.RoleMapper;
import cn.ponfee.web.framework.model.Role;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageHandler;

/**
 * The IRoleDao implementation class
 *
 * @author Ponfee
 */
@Repository
public class RoleDaoImpl implements IRoleDao {

    private @Resource RoleMapper mapper;

    @Override
    public int add(Role role) {
        return mapper.insert(role);
    }

    @Override
    public int update(Role role) {
        return mapper.update(role);
    }

    @Override
    public int delete(long roleId) {
        return mapper.delete(roleId);
    }

    @Override
    public Role getById(long id) {
        return mapper.getById(id);
    }

    @Override
    public Role getByRoleCode(String roleCode) {
        return mapper.getByRoleCode(roleCode);
    }

    @Override
    public Page<Role> query4page(Map<String, ?> params) {
        PageHandler.NORMAL.handle(params);
        return new Page<>(mapper.query4list(params));
    }

}
