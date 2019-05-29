package cn.ponfee.web.framework.service.impl;

import static code.ponfee.commons.util.RegexUtils.REGEXP_MOBILE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import cn.ponfee.web.framework.dao.IPermitDao;
import cn.ponfee.web.framework.dao.IUserDao;
import cn.ponfee.web.framework.dao.IUserRoleDao;
import cn.ponfee.web.framework.model.Permit;
import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.User;
import cn.ponfee.web.framework.model.UserRole;
import cn.ponfee.web.framework.service.IUserService;

import code.ponfee.commons.collect.Collects;
import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.constrain.Constraints;
import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.log.LogAnnotation.LogType;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.tree.TreeNode;

/**
 * The user service interface implementation
 *
 * @author Ponfee
 */
@Service("userService")
public class UserServiceImpl implements IUserService {

    private @Resource IUserDao userDao;
    private @Resource IUserRoleDao userRoleDao;
    private @Resource IPermitDao permitDao;

    @LogAnnotation(type = LogType.ADD)
    @Constraint(field = "username", notBlank = true, maxLen = 60, msg = "用户名不能为空或超长")
    @Constraint(field = "password", notBlank = true, maxLen = 100, msg = "密码不能为空或超长")
    @Constraint(field = "mobilePhone", notNull = false, regExp = REGEXP_MOBILE, msg = "手机号码格式错误")
    @Constraint(field = "status", series = { 0, 1 })
    @Constraint(field = "deleted", msg = "deleted不能为空")
    @Constraint(field = "roleIds", notEmpty= true)
    @Constraint(field = "createBy", min = 1)
    @Constraint(field = "updateBy", min = 1)
    @Constraint(field = "nickname", notNull = false, maxLen = 30, msg = "姓名超长")
    public @Override Result<Void> save(User user) {
        // check username is exists
        if (userDao.checkUsernameIsExists(user.getUsername())) {
            return Result.failure(ResultCode.OPS_CONFLICT, "用户名已存在");
        }

        // insert table t_user
        userDao.batchAdd(Arrays.asList(user));

        // add roles
        List<UserRole> list = new ArrayList<>();
        for (long roleId : user.getRoleIds()) {
            list.add(new UserRole(user.getId(), roleId));
        }
        userRoleDao.add(list);

        return Result.SUCCESS;
    }

    @Override
    public Result<Void> batchSave(List<User> users) {
        if (CollectionUtils.isEmpty(users)) {
            return Result.failure(ResultCode.BAD_REQUEST, "用户列表为空");
        }

        userDao.batchAdd(users);

        List<UserRole> roles = new ArrayList<>();
        for (User user : users) {
            for (long roleId : user.getRoleIds()) {
                roles.add(new UserRole(user.getId(), roleId));
            }
        }
        userRoleDao.add(roles);
        return Result.SUCCESS;
    }

    @Constraint(field = "id", min = 1)
    @Constraint(field = "password", notNull = false, maxLen = 100, msg = "密码不能为空或超长")
    @Constraint(field = "mobilePhone", notNull = false, regExp = REGEXP_MOBILE, msg = "手机号码格式错误")
    @Constraint(field = "nickname", notNull = false, maxLen = 30, msg = "姓名超长")
    @Constraint(field = "roleIds", notEmpty= true)
    @Constraint(field = "updateBy", min = 1)
    @Override
    public Result<Void> update(User user) {
        // update user
        userDao.update(user);

        // update roles
        List<Long> list = Collects.flatList(userRoleDao.queryByUserId(user.getId()), "id");
        if (CollectionUtils.isNotEmpty(Collects.different(list, user.getRoleIds()))) {
            userRoleDao.delByUserId(user.getId());
            List<UserRole> roles = new ArrayList<>(user.getRoleIds().size());
            for (long roleId : user.getRoleIds()) {
                roles.add(new UserRole(user.getId(), roleId));
            }
            userRoleDao.add(roles);
        }

        return Result.SUCCESS;
    }

    @Constraint(index = 0, min = 1)
    @Constraint(index = 1, min = 1)
    @Constraint(index = 2, series = { 0, 1 })
    public @Override Result<Void> changeStatus(long id, long updateBy, int status) {
        int affectedRows = userDao.changeStatus(id, updateBy, status);
        return Result.assertAffectedOne(affectedRows);
    }

    @Constraint(index = 0, notEmpty = true, msg = "用户ID列表不能为空")
    @Constraint(index = 1, min = 1)
    @Override
    public Result<Void> logicDelete(long[] uids, long updateBy) {
        int affectedRows = userDao.logicDelete(uids, updateBy);
        if (affectedRows == 0) {
            Result.failure(ResultCode.OPS_CONFLICT, "操作失败");
        }
        return Result.SUCCESS;
    }

    @Constraint(index = 0, min = 1, msg = "userId不能小于1")
    @Constraint(index = 1, msg = "角色编号列表不能为null")
    @Override
    public Result<Void> updateRoles(long userId, long[] roleIds) {
        userRoleDao.delByUserId(userId);

        if (roleIds.length > 0) {
            List<UserRole> list = new ArrayList<>();
            for (long roleId : roleIds) {
                list.add(new UserRole(userId, roleId));
            }
            userRoleDao.add(list);
        }

        return Result.SUCCESS;
    }

    public Result<Boolean> checkUsernameIsExists(String username) {
        return Result.success(userDao.checkUsernameIsExists(username));
    }

    @LogAnnotation(type = LogType.QUERY)
    @Constraints(@Constraint(notBlank = true, msg = "用户名不能为空"))
    public @Override Result<User> getByUsername(String username) {
        return Result.success(userDao.getByUsername(username));
    }

    @LogAnnotation(type = LogType.QUERY)
    @Constraint(field = "pageNum", min = 1)
    @Constraint(field = "pageSize", min = 0)
    public @Override Result<Page<Map<String, Object>>> query4page(Map<String, ?> params) {
        return Result.success(userDao.query4page(params));
    }

    @Constraint(field = "id", min = 1)
    @Constraint(field = "password", notNull = false, maxLen = 100, msg = "密码超长")
    @Constraint(field = "mobilePhone", notNull = false, regExp = REGEXP_MOBILE, msg = "手机号码格式错误")
    @Constraint(field = "nickname", notNull = false, maxLen = 30, msg = "姓名超长")
    @Constraint(field = "updateBy", min = 1)
    public @Override Result<Void> modifyInfo(User user) {
        int affectedRows = userDao.update(user);
        return Result.assertAffectedOne(affectedRows);
    }

    @Override
    public Result<List<Role>> queryUserRoles(long userId) {
        return Result.success(userRoleDao.queryByUserId(userId));
    }

    @Override
    public Result<List<String>> permitList(long userId) {
        return Result.success(userRoleDao.queryUserPermits(userId));
    }

    @Override
    public Result<TreeNode<String, Permit>> permitsTree(long userId) {
        return Result.success(Permit.buildTree(queryPermits(userId)));
    }

    private List<Permit> queryPermits(long userId) {
        List<String> userPermits = userRoleDao.queryUserPermits(userId);
        List<Permit> allPermits  = permitDao.queryAll();
        allPermits.forEach(p -> p.setStatus(
            userPermits.contains(p.getPermitId()) ? Permit.STATUS_ENABLE : Permit.STATUS_DISABLE
        ));
        return allPermits;
    }

}
