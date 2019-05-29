package cn.ponfee.web.framework.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.User;
import cn.ponfee.web.framework.service.IRoleService;
import cn.ponfee.web.framework.service.IUserService;
import cn.ponfee.web.framework.startup.SpringStartupListener;
import cn.ponfee.web.framework.util.CommonUtils;
import cn.ponfee.web.framework.web.ContextHolder;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.util.RegexUtils;

/**
 * The spring mvc controller for t_user CRUD operators
 * 
 * admin manage user info
 * 
 * @author Ponfee
 */
@RestController
@RequestMapping("user/mgr")
public class UserManageController {

    private @Resource IUserService userService;
    private @Resource IRoleService roleService;

    @PostMapping("add")
    public Result<Void> add(@RequestBody User user) {
        if (StringUtils.isBlank(user.getUsername())) {
            return Result.failure(ResultCode.BAD_REQUEST, "用户名为空");
        }
        if (!RegexUtils.isValidUserName(user.getUsername())) {
            return Result.failure(ResultCode.BAD_REQUEST, "用户名格式错误");
        }

        userDefaultProp(user, ContextHolder.currentUser().getId());
        return userService.save(user);
    }

    @PutMapping("update")
    public Result<Void> update(@RequestBody User user) {
        if (StringUtils.isNotEmpty(user.getPassword())) {
            // 需要更新密码
            String password = CommonUtils.decryptPassword(user.getPassword());
            if (!RegexUtils.isValidPassword(password)) {
                return Result.failure(ResultCode.BAD_REQUEST, "密码格式错误");
            }
            user.setPassword(CommonUtils.cryptPassword(password));
        }

        user.setRoleIds(Arrays.asList(SpringStartupListener.roleGeneral())); // XXX 默认普通角色
        user.setUpdateBy(ContextHolder.currentUser().getId());
        return userService.update(user);
    }

    @DeleteMapping("delete")
    public Result<Void> delete(@RequestParam("userIds") long[] userIds) {
        return userService.logicDelete(userIds, ContextHolder.currentUser().getId());
    }

    @GetMapping("query/page")
    public Result<Page<Map<String, Object>>> query4page(PageRequestParams params) {
        return userService.query4page(params.getParams());
    }

    @PutMapping("change/status")
    public Result<Void> changeStatus(@RequestParam("userId") long userId, 
                                     @RequestParam("status") int status) {
        return userService.changeStatus(userId, ContextHolder.currentUser().getId(), status);
    }

    @PutMapping("update/passwd")
    public Result<Void> updatePasswd(@RequestParam("userId") long userId, 
                                     @RequestParam("password") String password) {
        if (StringUtils.isBlank(password)) {
            return Result.failure(ResultCode.BAD_REQUEST, "密码不能为空");
        }

        password = CommonUtils.decryptPassword(password);
        if (!RegexUtils.isValidPassword(password)) {
            return Result.failure(ResultCode.BAD_REQUEST, "密码名格式错误");
        }

        User user = new User();
        user.setId(userId);
        user.setPassword(CommonUtils.cryptPassword(password));
        user.setUpdateBy(ContextHolder.currentUser().getId());
        return userService.modifyInfo(user);
    }

    @PutMapping("update/role")
    public Result<Void> updateRole(@RequestParam("userId") long userId, 
                                   @RequestParam("roleIds") long[] roleIds) {
        return userService.updateRoles(userId, roleIds);
    }

    @GetMapping("list/role")
    public Result<List<Role>> listRole(@RequestParam("userId") long userId) {
        return userService.queryUserRoles(userId);
    }

    // ----------------------------------------------------------------private methods
    private void userDefaultProp(User user, long createBy) {
        user.setStatus(User.STATUS_ENABLE);
        user.setDeleted(false);
        user.setRoleIds(Arrays.asList(SpringStartupListener.roleGeneral())); // XXX 默认普通角色
        user.setPassword(CommonUtils.cryptPassword(user.getUsername()));
        user.setCreateBy(createBy);
        user.setUpdateBy(createBy);
    }

}
