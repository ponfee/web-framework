package cn.ponfee.web.framework.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.service.IRoleService;
import cn.ponfee.web.framework.web.AppContext;
import code.ponfee.commons.math.Numbers;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.PageHandler;
import code.ponfee.commons.model.PageRequestParams;
import code.ponfee.commons.model.Result;

/**
 * Role Controller
 * 
 * @author Ponfee
 */
@RestController
@RequestMapping("role")
public class RoleController {

    private @Resource IRoleService roleService;

    @PostMapping("add")
    public Result<Void> add(@RequestBody Role role) {
        long currentUserId = AppContext.currentUser().getId();
        role.setCreateBy(currentUserId);
        role.setUpdateBy(currentUserId);
        return roleService.add(role);
    }

    @PutMapping("update")
    public Result<Void> update(@RequestBody Role role) {
        role.setUpdateBy(AppContext.currentUser().getId());
        return roleService.update(role);
    }

    @DeleteMapping("delete")
    public Result<Void> delete(@RequestParam("roleId") long roleId) {
        return roleService.deleteById(roleId);
    }

    @SuppressWarnings("unchecked")
    @PostMapping("updatepermits")
    public Result<Void> updatepermits(@RequestBody Map<String, Object> params) {
        long roleId = Numbers.toLong(params.get("roleId"));
        List<String> permitIds = (List<String>) params.get("permitIds");
        return roleService.updatePermits(roleId, permitIds);
    }

    @GetMapping("permits")
    public Result<List<String>> permits(@RequestParam("roleId") long roleId) {
        return roleService.queryRolePermits(roleId);
    }

    @GetMapping("get/roleid")
    public Result<Role> get(@RequestParam("roleId") long roleId) {
        return roleService.getById(roleId);
    }

    @GetMapping("get/rolecode")
    public Result<Role> get(@RequestParam("roleCode") String roleCode) {
        return roleService.getByRoleCode(roleCode);
    }

    @GetMapping("query4page")
    public Result<Page<Role>> query4page(PageRequestParams params) {
        return roleService.query4page(params.getParams());
    }

    @GetMapping("listall")
    public Result<List<Role>> listAll() {
        return Result.success(roleService.query4page(PageHandler.QUERY_ALL_PARAMS).getData().getRows());
    }
}
