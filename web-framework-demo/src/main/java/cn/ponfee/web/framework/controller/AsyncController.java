package cn.ponfee.web.framework.controller;

import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import com.google.common.collect.ImmutableMap;

@Controller
@RequestMapping("/asnyc")
public class AsyncController {

    @RequestMapping("/test1")
    public @ResponseBody String test1() {
        return "test1";
    }

    @RequestMapping("/test2")
    public @ResponseBody WebAsyncTask<Map<String, Object>> test2() {
        return new WebAsyncTask<Map<String, Object>>(() -> {
            Thread.sleep(3000);
            return ImmutableMap.of("status", 0);
        });
    }

    //返回WebAsyncTask的话是不需要我们主动去调用Callback的
    @RequestMapping("/test3")
    public @ResponseBody WebAsyncTask<Map<String, Object>> test3() {
        WebAsyncTask<Map<String, Object>> async = new WebAsyncTask<>(2000, () -> {
            Thread.sleep(3000);
            return ImmutableMap.of("status", 0);
        });

        async.onTimeout(() -> {
            return ImmutableMap.of("status", -1);
        });

        return async;
    }

    // 默认使用SimpleAsyncTaskExecutor来执行 
    @RequestMapping("/test4")
    public @ResponseBody Callable<String> test4() {
        return () -> {
            return "test4-Callable";
        };
    }
    
    @RequestMapping("/test5")
    public @ResponseBody DeferredResult<String> test5() {
        //new DeferredResult<>(timeout)
        //new DeferredResult<>(timeout, timeoutResult)
        DeferredResult<String> deferredResult = new DeferredResult<>();
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                deferredResult.setResult("test5-DeferredResult");
            }
        }.start();

        deferredResult.onTimeout(()->{
            deferredResult.setResult("test5-DeferredResult-timeout");
        });
        
        return deferredResult;
    }
}
