package cn.ponfee.web.framework.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import code.ponfee.commons.exception.BasicException;
import code.ponfee.commons.exception.UnauthorizedException;
import code.ponfee.commons.model.Result;

@RestController
@RequestMapping("test/exception")
public class TestExceptionHandleController {

    @GetMapping("unauthorizedexception")
    public Result<Void> unauthorizedexception() {
        throw new UnauthorizedException("unauthorizedexception");
    }
    
    @GetMapping("basicexception")
    public Result<Void> basicException() {
        throw new BasicException("basicexception");
    }

    @GetMapping("illegalargumentexception")
    public Result<Void> illegalargumentexception() {
        throw new IllegalArgumentException("IllegalArgumentException");
    }

    @GetMapping("servererror")
    public Result<Void> servererror() {
        throw new RuntimeException("RuntimeException");
    }

    @GetMapping("argserror")
    public Result<Void> argserror(int type) {
        return Result.SUCCESS;
    }

    @PostMapping("methodnotsupported")
    public Result<Void> methodnotsupported() {
        return Result.SUCCESS;
    }

}
