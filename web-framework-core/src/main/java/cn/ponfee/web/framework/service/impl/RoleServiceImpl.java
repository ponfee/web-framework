package cn.ponfee.web.framework.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.ponfee.web.framework.dao.IRoleDao;
import cn.ponfee.web.framework.dao.IRolePermitDao;
import cn.ponfee.web.framework.dao.IUserRoleDao;
import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.RolePermit;
import cn.ponfee.web.framework.service.IRoleService;
import cn.ponfee.web.framework.util.Constants;

import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;

/**
 * The role service interface implementation
 *
 * @author Ponfee
 */
@Service("roleService")
public class RoleServiceImpl implements IRoleService {

    private @Resource IRoleDao roleDao;
    private @Resource IRolePermitDao rolePermitDao;
    private @Resource IUserRoleDao userRoleDao;

    @Override
    public Result<Void> add(Role role) {
        int affectedRows = roleDao.add(role);
        if (affectedRows == Constants.ONE_ROW_AFFECTED
            && CollectionUtils.isNotEmpty(role.getPermitIds())) {
            List<RolePermit> list = new ArrayList<>();
            for (String permitId : role.getPermitIds()) {
                list.add(new RolePermit(role.getId(), permitId));
            }
            rolePermitDao.add(list);
        }
        return Result.assertAffectedOne(affectedRows);
    }

    @Constraint(field = "id", msg = "ID不能为空")
    @Constraint(field = "updateBy", min = 1)
    @Override
    public Result<Void> update(Role role) {
        int affectedRows = roleDao.update(role);
        if (affectedRows == Constants.ONE_ROW_AFFECTED) {
            rolePermitDao.delByRoleId(role.getId()); // 先删除
            if (CollectionUtils.isNotEmpty(role.getPermitIds())) {
                List<RolePermit> list = new ArrayList<>();
                for (String permitId : role.getPermitIds()) {
                    list.add(new RolePermit(role.getId(), permitId));
                }
                rolePermitDao.add(list); // 新增
            }
        }
        return Result.assertAffectedOne(affectedRows);
    }

    @Override
    public Result<Void> updatePermits(long roleId, List<String> permitIds) {
        rolePermitDao.delByRoleId(roleId);
        if (CollectionUtils.isNotEmpty(permitIds)) {
            List<RolePermit> list = new ArrayList<>();
            for (String permitId : permitIds) {
                list.add(new RolePermit(roleId, permitId));
            }
            rolePermitDao.add(list);
        }
        return Result.SUCCESS;
    }

    @Override
    public Result<Void> deleteById(long roleId) {
        int affectedRows = roleDao.delete(roleId);
        if (affectedRows == Constants.ONE_ROW_AFFECTED) {
            rolePermitDao.delByRoleId(roleId); // 删除角色权限表数据
            userRoleDao.delByRoleId(roleId); // 删除用户角色表数据
        }
        return Result.assertAffectedOne(affectedRows);
    }

    @Override
    public Result<Void> deleteByRoleCode(String roleCode) {
        Role role = roleDao.getByRoleCode(roleCode);
        if (role == null) {
            return Result.failure(ResultCode.OPS_CONFLICT, "不存在的角色代码：" + roleCode);
        }
        return deleteById(role.getId());
    }

    @Override
    public Result<Role> getById(long id) {
        return Result.success(roleDao.getById(id));
    }

    @Override
    public Result<Role> getByRoleCode(String roleCode) {
        return Result.success(roleDao.getByRoleCode(roleCode));
    }

    @Override
    public Result<Page<Role>> query4page(Map<String, ?> params) {
        return Result.success(roleDao.query4page(params));
    }

    @Override
    public Result<List<String>> queryRolePermits(long roleId) {
        return Result.success(rolePermitDao.queryPermitsByRoleId(roleId));
    }

}
