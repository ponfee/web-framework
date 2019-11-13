package cn.ponfee.web.framework.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;

import code.ponfee.commons.constrain.Jsr303Validator;

@ControllerAdvice
@Aspect
@Order(1)
public class WebJsr303Validator extends Jsr303Validator {

    @Around("execution(public * cn.ponfee.web..*.controller..*Controller..*(..)) && args(..,bindingResult)")
    public Object validateParam(ProceedingJoinPoint pjp, BindingResult bindingResult) throws Throwable {
        return super.verify(pjp, bindingResult);
    }

}
