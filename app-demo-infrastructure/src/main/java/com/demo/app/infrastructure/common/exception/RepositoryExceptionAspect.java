package com.demo.app.infrastructure.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;


/**
 * The type Repository exception aspect.
 */
@Aspect
@Component
@Slf4j
public class RepositoryExceptionAspect {

    /**
     * Annotated repository methods.
     */
    @Pointcut("execution(* com.demo.app.infrastructure.repository..*.*(..)) " +
            "&& @within(org.springframework.stereotype.Repository)")
    public void annotatedRepositoryMethods() {
    }

    /**
     * Handle annotated repository exceptions object.
     *
     * @param joinPoint the join point
     * @return the object
     * @throws Throwable the throwable
     */
    @Around("annotatedRepositoryMethods()")
    public Object handleAnnotatedRepositoryExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (InfrastructureException e) {
            throw e;
        } catch (Exception e) {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String methodName = signature.getMethod().getName();
            Object[] args = joinPoint.getArgs();
            log.error("AOP found infrastructure error, method = {}, args = {}",
                    methodName, Arrays.toString(args), e);
            throw new InfrastructureException(InfrastructureErrorCode.GENERAL_FAILED);
        }
    }
}