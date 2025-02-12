package az.xalqbank.msfileupload.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging execution of service and controller methods.
 */
@Aspect
@Component
@Slf4j
public class LoggingAspect {

    /**
     * Pointcut for all methods in the controller and service packages.
     */
    @Pointcut("within(az.xalqbank.msfileupload.controller..*) || within(az.xalqbank.msfileupload.service..*)")
    public void applicationPackagePointcut() {
        // Pointcut definition
    }

    /**
     * Advice that logs method entry.
     *
     * @param joinPoint the join point.
     */
    @Before("applicationPackagePointcut()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Entering: {} with arguments: {}", joinPoint.getSignature().toShortString(), joinPoint.getArgs());
    }

    /**
     * Advice that logs method exit.
     *
     * @param joinPoint the join point.
     * @param result    the returned value.
     */
    @AfterReturning(pointcut = "applicationPackagePointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Exiting: {} with result: {}", joinPoint.getSignature().toShortString(), result);
    }

    /**
     * Advice that logs exceptions thrown by methods.
     *
     * @param joinPoint the join point.
     * @param error     the thrown exception.
     */
    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("Exception in {} with cause = {}", joinPoint.getSignature().toShortString(), error.getMessage());
    }
}
