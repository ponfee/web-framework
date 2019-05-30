package cn.ponfee.web.framework.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.log.LogRecorder;

/**
 * Application log based spring aop
 *
 * @author Ponfee
 */
@Component
@Order(9) // order越小越是最先执行
@Aspect
public class ApplicationLogger extends LogRecorder /*implements Ordered*/ {

    @Around(
        value = "execution(public * cn.ponfee.web.framework.service.impl..*Impl..*(..)) && @annotation(log)", 
        argNames = "pjp,log"
    )
    @Override
    public Object around(ProceedingJoinPoint pjp, LogAnnotation log) throws Throwable {
        return super.around(pjp, log);
    }

}
