package cn.ponfee.web.framework.service;

import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageHelper;

import cn.ponfee.web.framework.SpringBaseTest;
import code.ponfee.commons.data.lookup.MultipleDataSourceContext;
import code.ponfee.commons.data.lookup.MultipletCachedDataSource;
import code.ponfee.commons.mybatis.SqlMapper;

/**
 * User Service Test
 *
 * @author Ponfee
 */
public class MutipleDataSourceTest extends SpringBaseTest<MultipletCachedDataSource> {

    private @Resource SqlMapper sqlMapper;
    
    @SuppressWarnings("unchecked")
    @Test
    public void test() {
        String sql = "select table_name from INFORMATION_SCHEMA.TABLES";
        PageHelper.startPage(30, 10);
        List<?> list = sqlMapper.selectList(sql, LinkedHashMap.class);
        ((List<LinkedHashMap<String, Object>>) list).stream().forEach(map -> {
            System.out.println(map.get("table_name"));
        });

        System.out.println("\n======================================");
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        datasource.setUrl("jdbc:mysql://10.202.4.26:3307/ddtm?useUnicode=true&characterEncoding=UTF8");
        datasource.setUsername("ddt-dev");
        datasource.setPassword("ddt-dev123456");

        PageHelper.startPage(50, 10);
        MultipletCachedDataSource mcds = getBean();
        mcds.addIfAbsent("test", datasource, 0);
        MultipleDataSourceContext.set("test");
        list = sqlMapper.selectList(sql, LinkedHashMap.class);
        ((List<LinkedHashMap<String, Object>>) list).stream().forEach(map -> {
            System.out.println(map.get("table_name"));
        });
    }

}
