package cn.ponfee.web.framework.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import cn.ponfee.web.framework.auth.UrlPermissionMatcher;
import cn.ponfee.web.framework.dao.IPermitDao;
import cn.ponfee.web.framework.dao.IRolePermitDao;
import cn.ponfee.web.framework.model.Permit;
import cn.ponfee.web.framework.service.IPermitService;
import cn.ponfee.web.framework.util.Constants;
import code.ponfee.commons.constrain.Constraint;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.model.ResultCode;
import code.ponfee.commons.tree.FlatNode;
import code.ponfee.commons.tree.TreeNode;

/**
 * The permit service interface implementation
 *
 * @author Ponfee
 */
@Service("permitService")
public class PermitServiceImpl implements IPermitService {

    private @Resource IPermitDao permitDao;
    private @Resource IRolePermitDao rolePermitDao;

    @Override
    public Result<Void> add(Permit permit) {
        int affectedRows = permitDao.add(permit);
        UrlPermissionMatcher.invalidPermits();
        return Result.assertAffectedOne(affectedRows);
    }

    @Override
    public Result<Void> update(Permit permit) {
        int affectedRows = permitDao.update(permit);
        UrlPermissionMatcher.invalidPermits();
        return Result.assertAffectedOne(affectedRows);
    }

    @Constraint(notEmpty = true, msg = "权限编号列表不能为空")
    @Override
    public Result<Void> delete(String[] ids) {
        int affectedRows = permitDao.delByPermitIds(Lists.newArrayList(ids));
        if (affectedRows < Constants.ONE_ROW_AFFECTED) {
            return Result.failure(ResultCode.OPS_CONFLICT, "操作失败");
        }

        for (String id : ids) {
            rolePermitDao.delByPermitId(id);
        }
        UrlPermissionMatcher.invalidPermits();
        return Result.SUCCESS;
    }

    @Override
    public Result<Permit> get(String id) {
        return Result.success(permitDao.getByPermitId(id));
    }

    @Override
    public Result<TreeNode<String, Permit>> treeAll() {
        return Result.success(
            Permit.buildTree(permitDao.queryAll())
        );
    }

    @Override
    public Result<List<FlatNode<String, Permit>>> flatsAll() {
        return Result.success(
            Permit.buildTree(permitDao.queryAll()).dfsFlat()
        );
    }

}
