package org.example.cache.aspects.logers;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.example.cache.configs.AspectsLoggersConfig;
import org.example.cache.entities.Customer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

import static org.example.cache.aspects.logers.Utils.after;
import static org.example.cache.aspects.logers.Utils.before;

@Aspect
@Component
public class CacheOperationsLogger {

    private final AspectsLoggersConfig aspectsLoggersConfig;

    public CacheOperationsLogger(AspectsLoggersConfig aspectsLoggersConfig) {
        this.aspectsLoggersConfig = aspectsLoggersConfig;
    }

    @Pointcut("execution(public * org.example.cache.caches.Cache.get(..))")
    public void getPointcut() {

    }

    @Pointcut("execution(public * org.example.cache.caches.Cache.put(..))")
    public void putPointcut() {

    }

    @Pointcut("execution(public * org.example.cache.caches.Cache.remove(..))")
    public void removePointcut() {

    }

    @Around("getPointcut()")
    public Object aroundGet(ProceedingJoinPoint pjp) throws Throwable {
        if (aspectsLoggersConfig.isCacheLoggingEnabled()) {
            Optional<Object> first = Arrays.stream(pjp.getArgs()).filter(String.class::isInstance).findFirst();
            if (first.isPresent()) {
                String key = (String) first.get();
                System.out.println("Getting object from Cache: " + pjp.getTarget().getClass().getSimpleName() + " by key: " + key);
            }
        }
        Object res = pjp.proceed();
        if (aspectsLoggersConfig.isCacheLoggingEnabled()) {
            if (res instanceof Optional<?> optional) {
                if (optional.isPresent()) {
                    System.out.println("Found value in Cache: " + pjp.getTarget().getClass().getSimpleName() + " value: " + optional.get());
                } else {
                    System.out.println("Value is not found in Cache: " + pjp.getTarget().getClass().getSimpleName());
                }
            }
        }
        return res;
    }


    @Around("putPointcut()")
    public Object aroundPut(ProceedingJoinPoint pjp) throws Throwable {
        if (aspectsLoggersConfig.isCacheLoggingEnabled()) {
            Optional<Object> first = Arrays.stream(pjp.getArgs()).filter(Customer.class::isInstance).findFirst();
            if (first.isPresent()) {
                Customer value = (Customer) first.get();
                System.out.println("Putting object to Cache:" + pjp.getTarget().getClass().getSimpleName() + " value: " + value);
            }
        }
        return pjp.proceed();
    }

    @Around("removePointcut()")
    public Object aroundRemove(ProceedingJoinPoint pjp) throws Throwable {
        if (aspectsLoggersConfig.isCacheLoggingEnabled()) {
            Optional<Object> first = Arrays.stream(pjp.getArgs()).filter(String.class::isInstance).findFirst();
            if (first.isPresent()) {
                String value = (String) first.get();
                System.out.println("Removing object from Cache: " + pjp.getTarget().getClass().getSimpleName() + " by key: " + value);
            }
        }
        return pjp.proceed();
    }

    @Pointcut("execution(public * org.example.cache.caches.Cache.*(..))")
    public void allMethodsPointcut() {
    }

    @Around("allMethodsPointcut()")
    public Object aroundAll(ProceedingJoinPoint pjp) throws Throwable {
        if (aspectsLoggersConfig.isCacheLoggingEnabled() && aspectsLoggersConfig.isLogsAroundMethodsEnabled()) {
            before(pjp);
        }
        Object res = pjp.proceed();
        if (aspectsLoggersConfig.isCacheLoggingEnabled() && aspectsLoggersConfig.isLogsAroundMethodsEnabled()) {
            after(pjp);
        }
        return res;
    }


}
