package cn.ponfee.web.framework.dao;

import java.util.List;

import org.junit.Test;

import cn.ponfee.web.framework.BaseTest;
import cn.ponfee.web.framework.model.Permit;
import code.ponfee.commons.tree.FlatNode;
import code.ponfee.commons.tree.TreeNode;

/**
 * Permit Dao Test
 *
 * @author Ponfee
 */
public class PermitDaoTest extends BaseTest<IPermitDao> {

    @Test
    public void test1() {
        List<Permit> permits = getBean().queryAll();
        print(permits);
        TreeNode<String, Permit> tree = Permit.buildTree(permits);
        print(tree);
        List<FlatNode<String, Permit>> flats = tree.dfsFlat();
        print(flats);
    }
}
