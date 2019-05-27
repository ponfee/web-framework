package cn.ponfee.web.framework.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ponfee.web.framework.model.Permit;
import cn.ponfee.web.framework.service.IPermitService;
import cn.ponfee.web.framework.util.WebContextHolder;

import code.ponfee.commons.model.Result;
import code.ponfee.commons.tree.FlatNode;
import code.ponfee.commons.tree.TreeNode;

/**
 * Role Controller
 * 
 * @author Ponfee
 */
@RestController
@RequestMapping("permit")
public class PermitController {

    private @Resource IPermitService service;

    @PostMapping("add")
    public Result<Void> add(@RequestBody Permit permit) {
        long currentUserId = WebContextHolder.currentUser().getId();
        permit.setCreateBy(currentUserId);
        permit.setUpdateBy(currentUserId);
        return service.add(permit);
    }

    @PostMapping("update")
    public Result<Void> update(@RequestBody Permit permit) {
        permit.setUpdateBy(WebContextHolder.currentUser().getId());
        return service.update(permit);
    }

    @PostMapping("delete")
    public Result<Void> delete(@RequestBody String[] permitIds) {
        return service.delete(permitIds);
    }

    @GetMapping("get")
    public Result<Permit> get(@RequestParam("permitId") String permitId) {
        return service.get(permitId);
    }

    @GetMapping("list/tree")
    public Result<TreeNode<String, Permit>> permitsTree() {
        return service.permitsTree();
    }

    @GetMapping("list/flat")
    public Result<List<FlatNode<String, Permit>>> permitsFlat() {
        return service.permitsFlat();
    }
}
