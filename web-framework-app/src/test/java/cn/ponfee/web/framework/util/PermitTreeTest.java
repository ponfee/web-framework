package cn.ponfee.web.framework.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.ponfee.web.framework.model.Permit;
import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.model.Result;
import code.ponfee.commons.tree.TreeNode;

public class PermitTreeTest {

    @Test
    public void test1() {
        List<Permit> list = new ArrayList<>();
        list.add(create(null, "100000", 1, Permit.STATUS_ENABLE));
        list.add(create("100000", "100010", 1, Permit.STATUS_ENABLE));
        list.add(create("100010", "100011", 1, Permit.STATUS_ENABLE));

        list.add(create(null, "200000", 2, Permit.STATUS_ENABLE));
        list.add(create("200000", "200010", 1, Permit.STATUS_ENABLE));
        list.add(create("200010", "200011", 1, Permit.STATUS_ENABLE));
        list.add(create("200010", "200012", 2, Permit.STATUS_ENABLE));

        list.add(create(null, "300000", 4, Permit.STATUS_ENABLE));

        list.add(create(null, "400000", 3, Permit.STATUS_DISABLE));
        list.add(create("400000", "400010", 1, Permit.STATUS_DISABLE));
        list.add(create("400000", "400020", 2, Permit.STATUS_ENABLE));

        TreeNode<String, Permit> root = Permit.buildTree(list);
        System.out.println(Jsons.NON_NULL.string(root));
        System.out.println(Jsons.NON_NULL.string(root.dfsFlat()));
        System.out.println(Jsons.NON_NULL.string(root.bfsFlat()));
    }

    /**
     * 节点无效
     */
    @Test
    public void test2() {
        List<Permit> list = new ArrayList<>();
        list.add(create(null, "100000", 1, Permit.STATUS_ENABLE));
        list.add(create("100000", "100010", 1, Permit.STATUS_ENABLE));
        list.add(create("100010", "100011", 1, Permit.STATUS_ENABLE));

        list.add(create(null, "200000", 2, Permit.STATUS_ENABLE));
        list.add(create("200000", "200010", 1, Permit.STATUS_ENABLE));
        list.add(create("200010", "200011", 1, Permit.STATUS_ENABLE));
        list.add(create("200010", "200012", 2, Permit.STATUS_ENABLE));

        list.add(create(null, "300000", 4, Permit.STATUS_ENABLE));

        list.add(create(null, "400000", 3, Permit.STATUS_DISABLE));
        list.add(create("400000", "400010", 1, Permit.STATUS_DISABLE));
        list.add(create("400000", "400020", 2, Permit.STATUS_ENABLE));

        list.add(create("500000", "500020", 2, Permit.STATUS_ENABLE)); // FIXME 孤儿节点

        TreeNode<String, Permit> root = Permit.buildTree(list);
        System.out.println(Jsons.NON_NULL.string(root));
        System.out.println(Jsons.NON_NULL.string(root.dfsFlat()));
        System.out.println(Jsons.NON_NULL.string(root.bfsFlat()));
    }

    /**
     * 节点循环依赖
     */
    @Test
    public void test3() {
        List<Permit> list = new ArrayList<>();
        list.add(create(null, "100000", 1, Permit.STATUS_ENABLE));
        list.add(create("100000", "100010", 1, Permit.STATUS_ENABLE));
        list.add(create("100010", "100011", 1, Permit.STATUS_ENABLE));

        list.add(create(null, "200000", 2, Permit.STATUS_ENABLE));
        list.add(create("200000", "200010", 1, Permit.STATUS_ENABLE));
        list.add(create("200010", "200011", 1, Permit.STATUS_ENABLE));

        list.add(create("200011", null, 1, Permit.STATUS_ENABLE)); // FIXME 循环依赖

        list.add(create(null, "300000", 4, Permit.STATUS_ENABLE));

        list.add(create(null, "400000", 3, Permit.STATUS_DISABLE));
        list.add(create("400000", "400010", 1, Permit.STATUS_DISABLE));
        list.add(create("400000", "400020", 2, Permit.STATUS_ENABLE));

        TreeNode<String, Permit> root = Permit.buildTree(list);
        System.out.println(Jsons.NON_NULL.string(root));
        System.out.println(Jsons.NON_NULL.string(root.dfsFlat()));
        System.out.println(Jsons.NON_NULL.string(root.bfsFlat()));
    }

    public static Permit create(String parentId, String permitId, 
                                Integer orders, Integer status) {
        Permit p = new Permit();
        p.setParentId(parentId);
        p.setPermitId(permitId);
        p.setOrders(orders);
        p.setStatus(status);
        p.setPermitType(0);
        return p;
    }

    public static void main(String[] args) {
        System.out.println(new HashSet<>().add(null));

        System.out.println(Jsons.toJson(Result.failure(-1, "fdsa\nfdsaf")));
        System.out.println(JSON.toJSONString(Result.failure(-1, "fdsa\nfdsaf")));
        String s = "fdsa\\nfdsaf";
        System.out.println(s);
        JSONObject obj = new JSONObject();
        obj.put("abc", "fdsa\nfdsaf");
        System.out.println(obj.toJSONString());
    }
    
}
