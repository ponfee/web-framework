package cn.ponfee.web.framework;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import code.ponfee.commons.json.Jsons;
import code.ponfee.commons.reflect.GenericUtils;
import code.ponfee.commons.util.SpringContextHolder;

/**
 * 测试基类
 * @author Ponfee
 * @param <T>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring-context.xml" })
public abstract class BaseTest<T> {

    private T bean;

    protected final T getBean() {
        return bean;
    }

    @Before
    public final void setUp() {
        bean = SpringContextHolder.getBean(GenericUtils.getActualTypeArgument(this.getClass()));
        initiate();
    }

    @After
    public final void tearDown() {
        destory();
    }

    protected void initiate() {
        // do no thing
    }

    protected void destory() {
        // do no thing
    }

    public static void print(Object obj) {
        System.out.println(Jsons.toJson(obj));
    }
}
