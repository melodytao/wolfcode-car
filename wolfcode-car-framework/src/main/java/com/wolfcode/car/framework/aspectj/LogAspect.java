package com.wolfcode.car.framework.aspectj;

import com.wolfcode.car.common.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LogAspect {
    /**
     * 处理完请求后执行
     * @param joinPoint  切入点
     * @param controllerLog 自定义注解Log
     * @param jsonResult 返回jsonResult
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)",returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog,Object jsonResult){
        handleLog(joinPoint,controllerLog,null,jsonResult);
    }

    /**
     * 拦截异常操作
     * @param joinPoint
     * @param controllerLog
     * @param e
     */
    @AfterThrowing(pointcut = "@annotation(controllerLog)",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,Log controllerLog,Exception e){
        handleLog(joinPoint,controllerLog,e,null);
    }

    private void handleLog(JoinPoint joinPoint,Log controllerLog,
                           Exception e,Object jsonResult){

    }
}
