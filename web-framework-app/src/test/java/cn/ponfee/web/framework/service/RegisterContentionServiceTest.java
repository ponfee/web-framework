package cn.ponfee.web.framework.service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

import cn.ponfee.web.framework.SpringBaseTest;
import code.ponfee.commons.concurrent.MultithreadExecutor;

/**
 *
 * @author Ponfee
 */
public class RegisterContentionServiceTest extends SpringBaseTest<IRegisterContentionService> {

    private static final List<String> SERVERS = Arrays.asList(
        "200.137.224.20", "131.196.240.17", "81.216.159.183", "208.253.2.72", 
        "213.42.49.177", "176.242.37.80", "29.36.199.199", "47.243.92.35", 
        "205.234.102.33", "109.106.191.221"
    );
    
    @Test
    public void getOrContend() {
        String[] values = IntStream.range(0, SERVERS.size() + 5).mapToObj(String::valueOf).toArray(String[]::new);

        /*List<String> result = MultithreadExecutor.callAsync(
            SERVERS, 
            x -> x + " -> " + getBean().getOrContend("A", x, values).getData()
        );
        System.out.println(String.join("\n", result));*/

        MultithreadExecutor.runAsync(
            SERVERS, 
            x -> System.out.println(x + " -> " + getBean().getOrContend("A", x, values).getData())
        );
    }

}
