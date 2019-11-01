package cn.ponfee.web.framework.redis;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import cn.ponfee.web.framework.model.User;

import code.ponfee.commons.jedis.JedisClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
@SuppressWarnings("unchecked")
public class JedisClientTest {

    private @Resource JedisClient jedisClient;
    
    @Test
    public void test1() {
        jedisClient.valueOps().setObject("test:123", Lists.newArrayList(), false);
        List<String> list = jedisClient.valueOps().getObject("test:123", ArrayList.class, false);
        System.out.println(list.getClass());
        System.out.println(list);
    }

    @Test
    public void test2() {
        jedisClient.valueOps().set("screen:statis:cache:1:2:a", "a");
        jedisClient.valueOps().set("screen:statis:cache:1:2:b", "b");
        jedisClient.valueOps().set("screen:statis:cache:1:2:c", "c");
        jedisClient.valueOps().set("screen:statis:cache:1:2:d", "d");
        jedisClient.valueOps().set("screen:statis:cache:1:2:e", "e");
        System.out.println(jedisClient.valueOps().getWithWildcard("screen:statis:cache:1:2:*"));
        jedisClient.keysOps().delWithWildcard("screen:statis:cache:1:2:*");
        System.out.println(jedisClient.valueOps().getWithWildcard("screen:statis:cache:1:2:*"));
    }

    @Test
    public void test3() {
        jedisClient.keysOps().delWithWildcard("screen:*");
    }

    @Test
    public void test4() {
        System.out.println(new String(Base64.getDecoder().decode("dXNlcjpjYWNoZTp1bm06OTU0MzE=")));
        User user = jedisClient.valueOps().getObject("abc", User.class);
        System.out.println(user);
    }
    
    @Test
    public void testDel() {
        jedisClient.keysOps().delWithWildcard("*");
    }
}
