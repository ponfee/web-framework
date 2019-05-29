package cn.ponfee.web.framework.service;

import java.util.List;
import java.util.Map;

import cn.ponfee.web.framework.model.Permit;
import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.User;

import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.tree.TreeNode;

/**
 * The user service interface
 *
 * @author Ponfee
 */
public interface IUserService {

    /**
     * 保存用户信息
     * 包括用户角色信息数据
     * 
     * @param user
     * @return
     */
    Result<Void> save(User user);

    /**
     * 批量增加
     * 
     * @param user
     * @return
     */
    Result<Void> batchSave(List<User> user);

    /**
     * 更新用户
     * @param user
     * @return
     */
    Result<Void> update(User user);

    /**
     * 更改用户状态
     * 
     * @param id
     * @param updateBy
     * @param status
     * @return
     */
    Result<Void> changeStatus(long id, long updateBy, int status);

    /**
     * 逻辑删除用户
     * 
     * @param uids     用户ID列表
     * @param updateBy
     * @return
     */
    Result<Void> logicDelete(long[] uids, long updateBy);

    /**
     * 更改用户基础信息（nickname, mobilephone）
     * @param user
     * @return
     */
    Result<Void> modifyInfo(User user);

    /**
     * 重置用户角色信息
     * 
     * @param userId
     * @param roleIds
     * @return
     */
    Result<Void> updateRoles(long userId, long[] roleIds);

    /**
     * 校验用户名是否存在
     * 
     * @param username the username
     * @return {@code true} is exists
     */
    Result<Boolean> checkUsernameIsExists(String username);

    /**
     * 通过用户名获取用户信息
     * 
     * @param username
     * @return
     */
    Result<User> getByUsername(String username);

    /**
     * 分页查询用户
     * 
     * @param params
     * @return
     */
    Result<Page<Map<String, Object>>> query4page(Map<String, ?> params);

    /**
     * 查找用户的角色列表
     * 
     * @param userId
     * @return
     */
    Result<List<Role>> queryUserRoles(long userId);

    /**
     * 获取用户权限编号列表
     * 
     * @param usreId
     * @return
     */
    Result<List<String>> permitList(long userId);

    /**
     * 获取用户权限树
     * 
     * @param userId
     * @return
     */
    Result<TreeNode<String, Permit>> permitsTree(long userId);

}
