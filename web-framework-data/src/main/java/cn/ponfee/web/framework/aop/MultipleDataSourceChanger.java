package cn.ponfee.web.framework.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

import code.ponfee.commons.data.DataSourceNaming;
import code.ponfee.commons.data.MultipleDataSourceAspect;

//@Component
//@Aspect
public class MultipleDataSourceChanger extends MultipleDataSourceAspect {
    @Around(value = "execution(public * cn.ponfee..*.service.impl..*Impl..*(..)) && @annotation(dsn)", argNames = "pjp,dsn")
    @Override
    public Object doAround(ProceedingJoinPoint pjp, DataSourceNaming dsn) throws Throwable {
        return super.doAround(pjp, dsn);
    }
}
