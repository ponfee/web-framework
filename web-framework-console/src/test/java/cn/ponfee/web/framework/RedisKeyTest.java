package cn.ponfee.web.framework;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Test;

import cn.ponfee.web.framework.model.RedisKey;

public class RedisKeyTest {

    @Test
    public void test0() {
        List<RedisKey> list = new ArrayList<>();
        list.add(null);
        list.add(new RedisKey("test", null, null, 10));
        list.add(null);
        list.sort(Comparator.nullsLast(Comparator.naturalOrder()));
        System.out.println(list.stream().map(x -> Objects.toString(x)).collect(Collectors.joining()));
    }
    
    @Test
    public void test1() {
        List<RedisKey> list = new ArrayList<>();
        list.add(new RedisKey("test", null, null, 10));
        System.out.println(list);
        list.remove(new RedisKey("test", null, null, 10));
        System.out.println(list);
    }
}
