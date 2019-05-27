package cn.ponfee.web.framework.aop;

import code.ponfee.commons.log.LogAnnotation;
import code.ponfee.commons.log.LogRecorder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Application log based spring aop
 *
 * @author Ponfee
 */
@Component
@Aspect
public class ApplicationLogger extends LogRecorder {

    @Around(
        value = "execution(public * cn.ponfee.web.framework.service.impl..*Impl..*(..)) && @annotation(log)", 
        argNames = "pjp,log"
    )
    @Override
    public Object around(ProceedingJoinPoint pjp, LogAnnotation log) throws Throwable {
        return super.around(pjp, log);
    }

}
