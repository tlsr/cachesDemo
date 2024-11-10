package org.example.cache.aspects.logers;

import org.aspectj.lang.ProceedingJoinPoint;

public class Utils {

    private Utils() {}

    public static void after(ProceedingJoinPoint pjp) {
        System.out.println("<<====  Exiting method: " + pjp.getSignature().getName()
            + " of class: " + pjp.getTarget().getClass().getSimpleName());
    }

    public static void before(ProceedingJoinPoint pjp) {
        System.out.println("====>>  Entering method: " + pjp.getSignature().getName()
            + " of class: " + pjp.getTarget().getClass().getSimpleName());
    }

}
