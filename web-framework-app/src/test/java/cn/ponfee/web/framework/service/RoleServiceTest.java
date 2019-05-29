package cn.ponfee.web.framework.service;

import java.util.Arrays;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import cn.ponfee.web.framework.BaseTest;
import cn.ponfee.web.framework.model.Role;

/**
 * Role Service Test
 *
 * @author Ponfee
 */
public class RoleServiceTest extends BaseTest<IRoleService> {

    @Test
    public void add() {
        Role role = new Role();
        role.setCreateBy(1L);
        role.setUpdateBy(1L);
        role.setRoleCode("ROLE");
        role.setRoleName("角色");
        role.setStatus(Role.STATUS_DISABLE);
        role.setPermitIds(Arrays.asList("1", "2", "3"));
        print(getBean().add(role));
    }

    @Test
    public void update() {
        Role role = new Role();
        role.setId(4L);
        role.setUpdateBy(2L);
        role.setRoleName("普通用户");
        role.setStatus(Role.STATUS_ENABLE);
        role.setPermitIds(Arrays.asList("4", "2", "3"));
        print(getBean().update(role));
    }
    
    @Test
    public void updatePermits() {
        print(getBean().updatePermits(4L, Arrays.asList("6", "5", "7")));
    }
    
    @Test
    public void queryRolePermits() {
        print(getBean().queryRolePermits(4L));
    }

    @Test
    public void deleteById() {
        print(getBean().deleteById(5L));
    }
    
    @Test
    public void deleteByRoleCode() {
        print(getBean().deleteByRoleCode("ROLE"));
    }
    
    @Test
    public void getById() {
        print(getBean().getById(4L));
    }
    
    @Test
    public void getByRoleCode() {
        print(getBean().getByRoleCode("ROLE"));
    }
    
    @Test
    public void query4page() {
        print(getBean().query4page(ImmutableMap.of("pageNum", 1, "pageSize", 5)));
    }
}
