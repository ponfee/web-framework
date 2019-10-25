package cn.ponfee.web.framework.service;

import javax.annotation.Resource;

import org.junit.Test;

import cn.ponfee.web.framework.SpringBaseTest;
import cn.ponfee.web.framework.model.Role;
import cn.ponfee.web.framework.model.User;

public class ConstraintValidateTest extends SpringBaseTest<Void> {

    private @Resource IPermitService permitService;
    private @Resource IRoleService roleService;
    private @Resource IUserService userService;

    @Test
    public void test1() {
        consoleJson(permitService.delete(null));
        consoleJson(permitService.delete(new String[] {}));
        consoleJson(permitService.delete(new String[] { "xxx" }));
    }

    @Test
    public void test2() {
        consoleJson(roleService.update(null));

        Role role = new Role();
        console("\n=====================================");
        consoleJson(roleService.update(role));

        role.setId(0L);
        console("\n=====================================");
        consoleJson(roleService.update(role));

        role.setUpdateBy(0L);
        console("\n=====================================");
        consoleJson(roleService.update(role));

        role.setUpdateBy(1L);
        console("\n=====================================");
        consoleJson(roleService.update(role));
    }

    @Test
    public void test3() {
        consoleJson(userService.save(null));

        User user = new User();

        console("\n=====================================");
        consoleJson(userService.save(user));

        console("\n=====================================");
        user.setUsername("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        user.setMobilePhone("4561616");
        consoleJson(userService.save(user));

    }
}
