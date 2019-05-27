package cn.ponfee.web.framework.controller;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Preconditions;
import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.User;
import cn.ponfee.web.framework.service.IRoleService;
import cn.ponfee.web.framework.service.IUserService;
import cn.ponfee.web.framework.startup.SpringStartupListener;
import cn.ponfee.web.framework.util.CommonUtils;
import cn.ponfee.web.framework.util.WebContextHolder;

import code.ponfee.commons.math.Numbers;
import code.ponfee.commons.model.AbstractDataConverter;
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
@RequestMapping("usermgr")
public class UserManageController {

    private @Value("${user.import.max.cloumn.number:30}") int userImportMaxColumnNumber;
    private @Resource IUserService service;
    private @Resource IRoleService roleService;

    @GetMapping("page")
    public Result<Page<Map<String, Object>>> page(PageRequestParams params) {
        if (params.getPageSize() < 1) {
            return Result.failure(ResultCode.BAD_REQUEST, "pageSize不能小于1");
        }
        params.getParams().put("deleted", false);
        return service.query4page(params.getParams());
    }

    @PostMapping("add")
    public Result<Void> add(@RequestBody User user) {
        if (StringUtils.isBlank(user.getUsername())) {
            return Result.failure(ResultCode.BAD_REQUEST, "用户名为空");
        }
        if (!RegexUtils.isValidUserName(user.getUsername())) {
            return Result.failure(ResultCode.BAD_REQUEST, "用户名格式错误");
        }

        userDefaultProp(user, WebContextHolder.currentUser().getId());
        return service.save(user);
    }

    @PostMapping("update")
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
        user.setUpdateBy(WebContextHolder.currentUser().getId());
        return service.update(user);
    }

    @PostMapping("changestatus")
    public Result<Void> changeStatus(@RequestBody Map<String, Object> params) {
        long userId = Numbers.toLong(params.get("userId"));
        int status = Numbers.toInt(params.get("status"));
        return service.changeStatus(userId, WebContextHolder.currentUser().getId(), status);
    }

    @PostMapping("resetpwd")
    public Result<Void> resetpwd(@RequestBody Map<String, Object> params) {
        long userId = Numbers.toLong(params.get("userId"));
        Preconditions.checkArgument(userId > 0, "用户ID不能小于0");
        String userName = (String) params.get("username");
        Preconditions.checkArgument(StringUtils.isNotBlank(userName), "用户名不能为空");

        User user = new User();
        user.setId(userId);
        user.setPassword(CommonUtils.cryptPassword(userName));
        user.setUpdateBy(WebContextHolder.currentUser().getId());
        return service.modifyInfo(user);
    }

    @PostMapping("delete")
    public Result<Void> delete(@RequestBody long[] userIds) {
        return service.logicDelete(userIds, WebContextHolder.currentUser().getId());
    }

    @PostMapping("updatepwd")
    public Result<Void> updatepwd(@RequestBody Map<String, Object> params) {
        long userId = Numbers.toLong(params.get("userId"));
        String password = (String) params.get("password");
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
        user.setUpdateBy(WebContextHolder.currentUser().getId());
        return service.modifyInfo(user);
    }

    @PostMapping("updaterole")
    public Result<Void> updateRole(@RequestBody Map<String, Object> params) {
        long userId = Numbers.toLong(params.get("userId"));
        long roleId = Numbers.toInt(params.get("roleId"));
        return service.updateRoles(userId, new long[] {roleId});
    }

    @GetMapping("userrole")
    public Result<Role> userrole(@RequestParam("userId") long userId) {
        return AbstractDataConverter.convertResultBean(
           service.queryUserRoles(userId), 
           roles -> CollectionUtils.isEmpty(roles) ? null : roles.get(0)
       );
    }

    private void userDefaultProp(User user, long createBy) {
        user.setStatus(User.STATUS_ENABLE);
        user.setDeleted(false);
        user.setRoleIds(Arrays.asList(SpringStartupListener.roleGeneral())); // XXX 默认普通角色
        user.setPassword(CommonUtils.cryptPassword(user.getUsername()));
        user.setCreateBy(createBy);
        user.setUpdateBy(createBy);
    }

}
