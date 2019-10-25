package cn.ponfee.web.framework.service;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import cn.ponfee.web.framework.SpringBaseTest;
import cn.ponfee.web.framework.model.User;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;

/**
 * User Service Test
 *
 * @author Ponfee
 */
public class UserServiceTest extends SpringBaseTest<IUserService> {

    @Test
    public void save() {
        User user = new User();
        user.setUsername("userNamea");
        user.setPassword("password4");
        user.setNickname("nickname4");
        user.setMobilePhone("13418467597");
        user.setStatus(1);
        user.setDeleted(true);
        user.setCreateBy(1L);
        user.setCreateTm(new Date());
        user.setUpdateBy(1L);
        user.setUpdateTm(new Date());
        consoleJson(getBean().save(user));
        console("userId: "+user.getId());
    }

    @Test
    public void update() {
        User user = new User();
        user.setId(18L);
        user.setRoleIds(Arrays.asList(1L));
        user.setUpdateBy(1L);
        consoleJson(getBean().update(user));
        console("userId: "+user.getId());
    }

    @Test
    public void checkUsernameIsExists() {
        consoleJson(getBean().checkUsernameIsExists("admin"));
        consoleJson(getBean().checkUsernameIsExists("abc"));
    }

    @Test
    public void getByUsername() {
        Result<User> result = getBean().getByUsername("username2");
        assertNotNull(result.getData());
        consoleJson(result);
    }

    @Test
    public void get() {
        Result<User> result = getBean().getByUsername("username1");
        assertNotNull(result.getData());
        consoleJson(result.getData());
    }

    @Test
    public void query4page() {
        Result<Page<Map<String, Object>>> result = getBean().query4page(
            ImmutableMap.of("pageSize", 5, "pageNum", 1, "nickname", "abc")
        );
        consoleJson(result);
    }

    @Test
    public void changeStatus() {
        consoleJson(getBean().changeStatus(9, 10, 1));
    }

    @Test
    public void logicDelete() {
        consoleJson(getBean().logicDelete(new long[] { 9 }, 1L));
    }

    @Test
    public void modifyInfo() {
        User user = new User();
        user.setId(9L);
        user.setNickname("test");
        user.setUpdateBy(1L);
        consoleJson(getBean().modifyInfo(user));
    }

    @Test
    public void updateRoles() {
        consoleJson(getBean().updateRoles(9L, new long[] { 4L }));
    }

    @Test
    public void queryUserRoles() {
        consoleJson(getBean().queryUserRoles(9L));
    }

}
