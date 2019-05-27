package cn.ponfee.web.framework.service;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

import cn.ponfee.web.framework.BaseTest;
import cn.ponfee.web.framework.model.User;
import code.ponfee.commons.model.Page;
import code.ponfee.commons.model.Result;

/**
 * User Service Test
 *
 * @author Ponfee
 */
public class UserServiceTest extends BaseTest<IUserService> {

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
        print(getBean().save(user));
        System.out.println("userId: "+user.getId());
    }

    @Test
    public void update() {
        User user = new User();
        user.setId(18L);
        user.setRoleIds(Arrays.asList(1L));
        user.setUpdateBy(1L);
        print(getBean().update(user));
        System.out.println("userId: "+user.getId());
    }

    @Test
    public void checkUsernameIsExists() {
        print(getBean().checkUsernameIsExists("admin"));
        print(getBean().checkUsernameIsExists("abc"));
    }

    @Test
    public void getByUsername() {
        Result<User> result = getBean().getByUsername("username2");
        assertNotNull(result.getData());
        print(result);
    }

    @Test
    public void get() {
        Result<User> result = getBean().getByUsername("username1");
        assertNotNull(result.getData());
        print(result.getData());
    }

    @Test
    public void query4page() {
        Result<Page<Map<String, Object>>> result = getBean().query4page(
            ImmutableMap.of("pageSize", 5, "pageNum", 1, "nickname", "abc")
        );
        print(result);
    }

    @Test
    public void changeStatus() {
        print(getBean().changeStatus(9, 10, 1));
    }

    @Test
    public void logicDelete() {
        print(getBean().logicDelete(new long[] { 9 }, 1L));
    }

    @Test
    public void modifyInfo() {
        User user = new User();
        user.setId(9L);
        user.setNickname("test");
        user.setUpdateBy(1L);
        print(getBean().modifyInfo(user));
    }

    @Test
    public void updateRoles() {
        print(getBean().updateRoles(9L, new long[] { 4L }));
    }

    @Test
    public void queryUserRoles() {
        print(getBean().queryUserRoles(9L));
    }

}
