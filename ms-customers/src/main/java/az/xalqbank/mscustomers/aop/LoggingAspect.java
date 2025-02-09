package az.xalqbank.mscustomers.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging method calls and their results in the controller layer.
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Logs method calls before execution in controller layer.
     *
     * @param joinPoint The join point provides access to method details
     */
    @Before("execution(* az.xalqbank.mscustomers.controller.CustomerController.*(..))")
    public void logMethodCall(JoinPoint joinPoint) {
        logger.info("Calling method: {}", joinPoint.getSignature().getName());
    }

    /**
     * Logs method calls after execution in controller layer.
     *
     * @param joinPoint The join point provides access to method details
     */
    @After("execution(* az.xalqbank.mscustomers.controller.CustomerController.*(..))")
    public void logMethodExit(JoinPoint joinPoint) {
        logger.info("Method executed: {}", joinPoint.getSignature().getName());
    }

    /**
     * Logs the return value after method execution in controller layer.
     *
     * @param result The result of the method execution
     */
    @AfterReturning(pointcut = "execution(* az.xalqbank.mscustomers.controller.CustomerController.*(..))", returning = "result")
    public void logMethodReturn(Object result) {
        logger.info("Method returned value: {}", result);
    }

    /**
     * Logs errors if any method throws an exception in controller layer.
     *
     * @param joinPoint The join point provides access to method details
     * @param ex        The exception thrown by the method
     */
    @AfterThrowing(pointcut = "execution(* az.xalqbank.mscustomers.controller.CustomerController.*(..))", throwing = "ex")
    public void logMethodException(JoinPoint joinPoint, Throwable ex) {
        logger.error("Method {} threw an exception: {}", joinPoint.getSignature().getName(), ex.getMessage());
    }
}
