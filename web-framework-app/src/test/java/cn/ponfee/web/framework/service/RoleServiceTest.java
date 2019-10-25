package cn.ponfee.web.framework.service;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import cn.ponfee.web.framework.SpringBaseTest;
import cn.ponfee.web.framework.model.Role;

/**
 * Role Service Test
 *
 * @author Ponfee
 */
public class RoleServiceTest extends SpringBaseTest<IRoleService> {

    @Test
    public void add() {
        Role role = new Role();
        role.setCreateBy(1L);
        role.setUpdateBy(1L);
        role.setRoleCode("ROLE");
        role.setRoleName("角色");
        role.setStatus(Role.STATUS_DISABLE);
        role.setPermitIds(Arrays.asList("1", "2", "3"));
        consoleJson(getBean().add(role));
    }

    @Test
    public void update() {
        Role role = new Role();
        role.setId(4L);
        role.setUpdateBy(2L);
        role.setRoleName("普通用户");
        role.setStatus(Role.STATUS_ENABLE);
        role.setPermitIds(Arrays.asList("4", "2", "3"));
        consoleJson(getBean().update(role));
    }
    
    @Test
    public void updatePermits() {
        consoleJson(getBean().updatePermits(4L, Arrays.asList("6", "5", "7")));
    }
    
    @Test
    public void queryRolePermits() {
        consoleJson(getBean().queryRolePermits(4L));
    }

    @Test
    public void deleteById() {
        consoleJson(getBean().deleteById(5L));
    }
    
    @Test
    public void deleteByRoleCode() {
        consoleJson(getBean().deleteByRoleCode("ROLE"));
    }
    
    @Test
    public void getById() {
        consoleJson(getBean().getById(4L));
    }
    
    @Test
    public void getByRoleCode() {
        consoleJson(getBean().getByRoleCode("ROLE"));
    }
    
    @Test
    public void query4page() {
        consoleJson(getBean().query4page(ImmutableMap.of("pageNum", 1, "pageSize", 5)));
    }
}
