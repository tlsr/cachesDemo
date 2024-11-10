package org.example.cache.aspects.logers;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.cache.configs.AspectsLoggersConfig;
import org.springframework.stereotype.Component;

import static org.example.cache.aspects.logers.Utils.after;
import static org.example.cache.aspects.logers.Utils.before;

@Aspect
@Component
public class ServiceOperationLogger {

    private final AspectsLoggersConfig aspectsLoggersConfig;

    public ServiceOperationLogger(AspectsLoggersConfig aspectsLoggersConfig) {
        this.aspectsLoggersConfig = aspectsLoggersConfig;
    }

    @Pointcut("execution(public * org.example.cache.service.ICustomerService.*(..))")
    public void allMethodsPointcut() {
    }

    @Around("allMethodsPointcut()")
    public Object aroundAll(ProceedingJoinPoint pjp) throws Throwable {
        if (aspectsLoggersConfig.isRepoLoggingEnabled() && aspectsLoggersConfig.isLogsAroundMethodsEnabled()) {
            before(pjp);
        }
        Object res = pjp.proceed();
        if (aspectsLoggersConfig.isRepoLoggingEnabled() && aspectsLoggersConfig.isLogsAroundMethodsEnabled()) {
            after(pjp);
        }
        return res;
    }


}
