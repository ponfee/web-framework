package cn.ponfee.web.framework.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import cn.ponfee.web.framework.BaseTest;
import cn.ponfee.web.framework.model.Permit;
import code.ponfee.commons.collect.Collects;
import code.ponfee.commons.tree.FlatNode;

/**
 * Permit Service Test
 *
 * @author Ponfee
 */
public class PermitServiceTest extends BaseTest<IPermitService> {

    @Test
    public void add() {
        getBean().add(create("1000", null, Permit.TYPE_MENU, 4, Permit.STATUS_ENABLE));
        getBean().add(create("1010", "1000", Permit.TYPE_MENU, 2, Permit.STATUS_ENABLE));
        getBean().add(create("1020", "1000", Permit.TYPE_MENU, 1, Permit.STATUS_DISABLE));
        getBean().add(create("1021", "1020", Permit.TYPE_BUTTON, 1, Permit.STATUS_ENABLE));

        getBean().add(create("2000", null, Permit.TYPE_MENU, 2, Permit.STATUS_ENABLE));

        getBean().add(create("3000", null, Permit.TYPE_MENU, 3, Permit.STATUS_DISABLE));
        getBean().add(create("3010", "3000", Permit.TYPE_BUTTON, 1, Permit.STATUS_ENABLE));

        getBean().add(create("4000", null, Permit.TYPE_MENU, 1, Permit.STATUS_ENABLE));
        getBean().add(create("4010", "4000", Permit.TYPE_MENU, 1, Permit.STATUS_ENABLE));
        getBean().add(create("4011", "4010", Permit.TYPE_BUTTON, 1, Permit.STATUS_DISABLE));
    }

    @Test
    public void update() {
        Permit p = new Permit();
        p.setOrders(2);
        p.setPermitId("1000");
        p.setParentId(null);
        p.setPermitName("name");
        p.setPermitIcon("icon");
        p.setPermitType(Permit.TYPE_BUTTON);
        p.setStatus(Permit.STATUS_DISABLE);
        p.setUpdateBy(2L);
        p.setPermitUrl("url");
        print(getBean().update(p));
    }

    @Test
    public void delete() {
        print(getBean().delete(new String[] { "1000", "10001" }));
    }

    @Test
    public void get() {
        print(getBean().get("1000"));
    }

    @Test
    public void permitsTree() {
        print(getBean().treeAll());
    }

    @Test
    public void permitsFlat() {
        List<FlatNode<String, Permit>> list = getBean().flatsAll().getData();
        List<Map<String, Object>> res = list.stream().map(x -> {
          return Collects.toMap(
            "nid", x.getNid(),
            "pid", x.getPid(),
            "orders", x.getOrders(),
            "enabled", x.isEnabled(),
            "available", x.isAvailable(),
            "path", String.join(",",x.getPath()),
            "level", x.getLevel(),
            "leaf", x.isLeaf(),
            "childLeafCount", x.getChildLeafCount(),
            "leftLeafCount", x.getLeftLeafCount(),
            "treeNodeCount", x.getTreeNodeCount(),
            "treeMaxDepth", x.getTreeMaxDepth(),
            "name", x.getAttach()==null? null:x.getAttach().getPermitName(),
            "type", x.getAttach()==null? null:x.getAttach().getPermitType()
          );
        }).collect(Collectors.toList());
        print(res);
    }

    private static Permit create(String permitId, String parentId, 
                                 int permitType, int orders, int status) {
        Permit p = new Permit();
        p.setPermitId(permitId);
        p.setParentId(parentId);
        p.setPermitType(permitType);
        p.setOrders(orders);
        p.setStatus(status);
        p.setPermitName("id" + permitId);
        p.setCreateBy(1L);
        p.setUpdateBy(1L);
        return p;
    }
}
