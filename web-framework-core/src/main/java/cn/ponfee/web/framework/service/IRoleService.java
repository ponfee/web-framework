package cn.ponfee.web.framework.service;

import java.util.List;
import java.util.Map;

import cn.ponfee.web.framework.model.Role;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;

/**
 * The role service interface
 *
 * @author Ponfee
 */
public interface IRoleService {

    /**
     * 新增角色（包含权限List<String> permitIds）
     * 
     * @param role
     * @return
     */
    Result<Void> add(Role role);

    /**
     * 更新角色信息（包含权限List<String> permitIds）
     * 先删除已有权限（t_role_permit）数据，再新增
     * 
     * @param role
     * @return
     */
    Result<Void> update(Role role);

    /**
     * 重新配置权限
     * 先删除已有权限（t_role_permit）数据，再新增
     * 
     * @param roleId
     * @param permitIds
     * @return
     */
    Result<Void> updatePermits(long roleId, List<String> permitIds);

    /**
     * 查询角色所配置的所有权限编号列表
     * 
     * @param roleId
     * @return
     */
    Result<List<String>> queryRolePermits(long roleId);

    /**
     * 根据id删除角色
     * 删除角色权限关联表t_role_permit数据
     * 删除用户角色关联表t_user_role数据
     * 
     * @param id
     * @return
     */
    Result<Void> deleteById(long id);

    /**
     * 通过角色代码删除
     * 删除角色权限关联表t_role_permit数据
     * 删除用户角色关联表t_user_role数据
     * 
     * @param roleCode
     * @return
     */
    Result<Void> deleteByRoleCode(String roleCode);

    /**
     * 通过id角色获取数据
     * 会获取角色的所有权限编号列表List<String> permitIds
     * 
     * @param id
     * @return
     */
    Result<Role> getById(long id);

    /**
     * 通过角色代码获取数据
     * 会获取角色的所有权限编号列表List<String> permitIds
     * 
     * @param roleCode
     * @return
     */
    Result<Role> getByRoleCode(String roleCode);

    /**
     * 分页查询数据
     * 
     * @param params
     * @return
     */
    Result<Page<Role>> query4page(Map<String, ?> params);
}
