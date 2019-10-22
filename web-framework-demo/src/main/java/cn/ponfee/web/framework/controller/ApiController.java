package cn.ponfee.web.framework.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/data1")
    @ResponseBody
    public Object data1() {
        return ImmutableMap.of("x", 1, "y", true, "z", "string");
    }

    @GetMapping("/data2")
    @ResponseBody
    public Object data2() {
        return new Object[] {1, true, "string"};
    }
    
    @GetMapping("/data3")
    @ResponseBody
    public Object data3() {
        return new Object[] {
            ImmutableMap.of("x", 1, "y", true, "z", "string1"),
            ImmutableMap.of("x", 2, "y", false, "z", "string2")
        };
    }
}
