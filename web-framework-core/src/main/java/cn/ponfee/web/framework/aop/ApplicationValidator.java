package cn.ponfee.web.framework.aop;

import code.ponfee.commons.constrain.Constraints;
import code.ponfee.commons.constrain.MethodValidator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Validator parameters before method invoke based spring aop
 *
 * @author Ponfee
 */
@Component
@Order(1) // order越小越是最先执行
@Aspect
public class ApplicationValidator extends MethodValidator {

    @Around(
        value = "execution(public * cn.ponfee.web.framework.service.impl..*Impl..*(..)) && @annotation(cst)", 
        argNames = "pjp,cst"
    )
    @Override
    public Object constrain(ProceedingJoinPoint pjp, Constraints cst) throws Throwable {
        return super.constrain(pjp, cst);
    }
}
