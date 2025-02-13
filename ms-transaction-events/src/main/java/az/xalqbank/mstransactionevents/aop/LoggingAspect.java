package az.xalqbank.mstransactionevents.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution time of service methods.
 */
@Aspect
@Component
public class LoggingAspect {

    /**
     * Logs the execution time of methods in the service package.
     *
     * @param joinPoint the join point representing the method.
     * @return the result of the method execution.
     * @throws Throwable if the method execution throws an exception.
     */
    @Around("execution(* az.xalqbank.mstransactionevents.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - startTime;
        logger.info("{} executed in {} ms", joinPoint.getSignature(), executionTime);

        return result;
    }
}
