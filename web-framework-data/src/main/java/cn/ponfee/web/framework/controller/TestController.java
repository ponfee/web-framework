package cn.ponfee.web.framework.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.ponfee.web.framework.service.TestService;
import code.ponfee.commons.model.Result;

/**
 * http://localhost:8100/test/transaction?datasource=default&bool=true
 * http://localhost:8100/test/transaction?datasource=default&bool=false
 * 
 * 
 * http://localhost:8100/test/transaction?datasource=secondary&bool=true
 * http://localhost:8100/test/transaction?datasource=secondary&bool=false
 */
@RestController
@RequestMapping("test")
public class TestController {

    @Resource
    TestService service;

    @GetMapping("transaction")
    public Result<Void> transaction(@RequestParam("datasource") String datasource, @RequestParam("bool") boolean bool) {
        service.testTransaction(datasource, bool);
        return Result.SUCCESS;
    }
}
