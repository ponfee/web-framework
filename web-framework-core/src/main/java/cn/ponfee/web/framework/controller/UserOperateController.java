package cn.ponfee.web.framework.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ponfee.web.framework.model.Permit;
import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.User;
import cn.ponfee.web.framework.service.IUserService;
import cn.ponfee.web.framework.util.CommonUtils;
import cn.ponfee.web.framework.web.ContextHolder;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.tree.TreeNode;
import code.ponfee.commons.util.RegexUtils;

/**
 * The spring mvc controller for t_user CRUD operators
 * 
 * user self operate
 * 
 * @author Ponfee
 */
@RestController
@RequestMapping("user/ops")
public class UserOperateController {

    private @Resource IUserService userService;

    /**
     * Modify info: mobilePhone, nickname, password
     * 
     * @param u
     * @return
     */
    @PutMapping("modify/info")
    public Result<Void> modifyInfo(@RequestBody User u) {
        if (StringUtils.isAllBlank(u.getNickname(), u.getMobilePhone(), u.getPassword())) {
            return Result.failure(ResultCode.BAD_REQUEST, "请填写要修改的用户信息");
        }

        if (StringUtils.isNotBlank(u.getPassword())) {
            u.setPassword(passwd(u.getPassword(), u.getOriginPassword()));
        }

        long userId = ContextHolder.currentUser().getId();
        u.setId(userId);
        u.setUpdateBy(userId);
        return userService.modifyInfo(u);
    }

    /**
     * Get my role info
     * 
     * @return
     */
    @GetMapping("my/roles")
    public Result<List<Role>> myRoles() {
        return userService.queryUserRoles(ContextHolder.currentUser().getId());
    }

    /**
     * Get my info
     * 
     * @return
     */
    @GetMapping("my/info")
    public Result<User> myInfo() {
        return Result.success(ContextHolder.currentUser());
    }

    @GetMapping("permits/tree")
    public Result<TreeNode<String, Permit>> permitsTree() {
        User user = ContextHolder.currentUser();
        return userService.permitsTree(user.getId());
    }

    // ----------------------------------------------------------------private methods
    private String passwd(String password, String originPassword) {
        if (StringUtils.isBlank(password)) {
            throw new IllegalArgumentException("密码不能为空");
        }
        password = CommonUtils.decryptPassword(password);
        if (!RegexUtils.isValidPassword(password)) {
            throw new IllegalArgumentException("密码格式错误");
        }

        if (StringUtils.isBlank(originPassword)) {
            throw new IllegalArgumentException("原密码不能为空");
        }
        originPassword = CommonUtils.decryptPassword(originPassword);
        User user = ContextHolder.currentUser();
        if (!CommonUtils.checkPassword(originPassword, user.getPassword())) {
            throw new IllegalArgumentException("原密码错误");
        }

        return CommonUtils.cryptPassword(password);
    }
}
