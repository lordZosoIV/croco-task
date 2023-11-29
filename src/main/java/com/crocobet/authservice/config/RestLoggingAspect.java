package com.crocobet.authservice.config;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class RestLoggingAspect {


    @Around("execution(* com.crocobet.authservice.controller..*(..)))")
    public Object logMethodExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        logRequest(proceedingJoinPoint);

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        logResponse(result);
        //Log method execution time
        log.info("Stopwatch - [{}.{}::{}ms]",
                methodSignature.getDeclaringType().getSimpleName(),
                methodSignature.getName(),
                stopWatch.getTotalTimeMillis());

        return result;
    }

    public void logRequest(ProceedingJoinPoint proceedingJoinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        log.info("<-- {} {}", request.getMethod(), request.getRequestURL());
        log.info("<-- {}", getPayload(proceedingJoinPoint));
    }

    public void logResponse(Object response) {
        log.info("--> {}", response);
    }

    private String getPayload(JoinPoint joinPoint) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < joinPoint.getArgs().length; i++) {
            String parameterName = signature.getParameterNames()[i];
            builder.append(parameterName);
            builder.append(": ");
            builder.append(joinPoint.getArgs()[i] != null ? joinPoint.getArgs()[i].toString() : "null");
            builder.append(", ");
        }

        if (builder.length() > 2) {
            builder.deleteCharAt(builder.length() - 1);
            builder.deleteCharAt(builder.length() - 1);
        }

        return builder.toString();
    }
}