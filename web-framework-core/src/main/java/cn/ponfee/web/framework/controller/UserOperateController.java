package cn.ponfee.web.framework.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.ponfee.web.framework.model.Permit;
import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.User;
import cn.ponfee.web.framework.service.IUserService;
import cn.ponfee.web.framework.util.CommonUtils;
import cn.ponfee.web.framework.web.WebContext;
import code.ponfee.commons.model.AbstractDataConverter;
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
@RequestMapping("userops")
public class UserOperateController {

    private @Resource IUserService userService;

    /**
     * Modify info: mobilePhone, nickname, password
     * 
     * @param u
     * @return
     */
    @PostMapping("modify/info")
    public Result<Void> modifyInfo(@RequestBody User u) {
        if (StringUtils.isAllBlank(u.getNickname(), u.getMobilePhone(), u.getPassword())) {
            return Result.failure(ResultCode.BAD_REQUEST, "请填写要修改的用户信息");
        }

        if (StringUtils.isNotBlank(u.getPassword())) {
            u.setPassword(passwd(u.getPassword(), u.getOriginPassword()));
        }

        long userId = WebContext.currentUser().getId();
        u.setId(userId);
        u.setUpdateBy(userId);
        return userService.modifyInfo(u);
    }

    @PostMapping("modify/name")
    public Result<Void> modifyname(@RequestBody Map<String, Object> map) {
        String nickname = (String) map.get("nickname");
        if (StringUtils.isBlank(nickname)) {
            return Result.failure(ResultCode.BAD_REQUEST, "姓名不能为空");
        }
        long userId = WebContext.currentUser().getId();
        User user = new User();
        user.setId(userId);
        user.setNickname(nickname);
        user.setUpdateBy(userId);
        return userService.modifyInfo(user);
    }

    @PostMapping("modify/phone")
    public Result<Void> modifyphone(@RequestBody Map<String, Object> map) {
        String mobilePhone = (String) map.get("mobilePhone");
        if (StringUtils.isBlank(mobilePhone)) {
            return Result.failure(ResultCode.BAD_REQUEST, "手机号码不能为空");
        }
        long userId = WebContext.currentUser().getId();
        User user = new User();
        user.setId(userId);
        user.setMobilePhone(mobilePhone);
        user.setUpdateBy(userId);
        return userService.modifyInfo(user);
    }

    @PostMapping("modify/pwd")
    public Result<Void> modifypwd(@RequestBody Map<String, Object> map) {
        long userId = WebContext.currentUser().getId();
        User user = new User();
        user.setId(userId);
        user.setPassword(passwd((String) map.get("password"), (String) map.get("originPassword")));
        user.setUpdateBy(userId);
        return userService.modifyInfo(user);
    }

    /**
     * Get my role info
     * 
     * @return
     */
    @GetMapping("my/role")
    public Result<Role> myRole() {
        return AbstractDataConverter.convertResultBean(
            userService.queryUserRoles(WebContext.currentUser().getId()), 
            roles -> CollectionUtils.isEmpty(roles) ? null : roles.get(0)
        );
    }

    /**
     * Get my info
     * 
     * @return
     */
    @GetMapping("my/info")
    public Result<User> myInfo() {
        return Result.success(WebContext.currentUser());
    }

    @GetMapping("permit/tree")
    public Result<TreeNode<String, Permit>> permitTree() {
        User user = WebContext.currentUser();
        return userService.permitTree(user.getId());
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
        User user = WebContext.currentUser();
        if (!CommonUtils.checkPassword(originPassword, user.getPassword())) {
            throw new IllegalArgumentException("原密码错误");
        }

        return CommonUtils.cryptPassword(password);
    }
}
