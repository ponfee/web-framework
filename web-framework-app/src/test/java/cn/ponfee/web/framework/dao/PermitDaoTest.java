package cn.ponfee.web.framework.dao;

import java.util.List;

import org.junit.Test;

import cn.ponfee.web.framework.SpringBaseTest;
import cn.ponfee.web.framework.model.Permit;
import code.ponfee.commons.tree.FlatNode;
import code.ponfee.commons.tree.TreeNode;

/**
 * Permit Dao Test
 *
 * @author Ponfee
 */
public class PermitDaoTest extends SpringBaseTest<IPermitDao> {

    @Test
    public void test1() {
        List<Permit> permits = getBean().queryAll();
        consoleJson(permits);
        TreeNode<String, Permit> tree = Permit.buildTree(permits);
        consoleJson(tree);
        List<FlatNode<String, Permit>> flats = tree.dfsFlat();
        consoleJson(flats);
    }
}
