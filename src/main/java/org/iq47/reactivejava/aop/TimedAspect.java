package org.iq47.reactivejava.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimedAspect {
    @Around("@annotation(timed)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, Timed timed) throws Throwable {
        System.out.println(timed.service() + ":");
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        System.out.println("Executed in " + executionTime + "ms");
        return proceed;
    }
}
