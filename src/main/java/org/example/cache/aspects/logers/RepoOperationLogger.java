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
public class RepoOperationLogger {

    private final AspectsLoggersConfig aspectsLoggersConfig;

    public RepoOperationLogger(AspectsLoggersConfig aspectsLoggersConfig) {
        this.aspectsLoggersConfig = aspectsLoggersConfig;
    }


    @Pointcut("execution(public * org.example.cache.repo.CustomerRepository.findByEmail(..))")
    public void fetchByEmailPointcut() {
    }


    @Around("fetchByEmailPointcut()")
    public Object aroundFindByEmail(ProceedingJoinPoint pjp) throws Throwable {
        if (aspectsLoggersConfig.isRepoLoggingEnabled()) {
            Optional<Object> first = Arrays.stream(pjp.getArgs()).filter(String.class::isInstance).findFirst();
            if (first.isPresent()) {
                String key = (String) first.get();
                System.out.println("Getting object from repo: " + pjp.getTarget().getClass().getSimpleName() + " by email: " + key);
            }
        }
        return pjp.proceed();
    }


    @Pointcut("execution(public * org.example.cache.repo.CustomerRepository.save(..))")
    public void savePointcut() {
    }


    @Around("savePointcut()")
    public Object aroundSave(ProceedingJoinPoint pjp) throws Throwable {
        if (aspectsLoggersConfig.isRepoLoggingEnabled()) {
            Optional<Object> first = Arrays.stream(pjp.getArgs()).filter(Customer.class::isInstance).findFirst();
            if (first.isPresent()) {
                Customer value = (Customer) first.get();
                System.out.println("Saving object  to repo: " + pjp.getTarget().getClass().getSimpleName() + "  value: " + value);
            }
        }
        return pjp.proceed();
    }


    @Pointcut("execution(public * org.example.cache.repo.CustomerRepository.*(..))")
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
