package cn.ponfee.web.framework.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import cn.ponfee.web.framework.dao.IRolePermitDao;
import cn.ponfee.web.framework.dao.mapper.RolePermitMapper;
import cn.ponfee.web.framework.model.RolePermit;

/**
 * The IRolePermitDao implementation class
 *
 * @author Ponfee
 */
@Repository
public class RolePermitDaoImpl implements IRolePermitDao {

    private @Resource RolePermitMapper mapper;

    @Override
    public int add(List<RolePermit> list) {
        return mapper.insert(list);
    }

    @Override
    public int delByRoleId(long roleId) {
        return mapper.delByRoleId(roleId);
    }

    @Override
    public int delByPermitId(String permitId) {
        return mapper.delByPermitId(permitId);
    }

    @Override
    public List<String> queryPermitsByRoleId(long roleId) {
        return mapper.queryPermitsByRoleId(roleId);
    }

}
