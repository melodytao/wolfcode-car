package com.wolfcode.car.framework.aspectj;

import com.alibaba.fastjson2.JSON;
import com.wolfcode.car.common.annotation.Log;
import com.wolfcode.car.common.core.domain.model.LoginUser;
import com.wolfcode.car.common.enums.BusinessStatus;
import com.wolfcode.car.common.enums.HttpMethod;
import com.wolfcode.car.common.utils.SecurityUtils;
import com.wolfcode.car.common.utils.ServletUtils;
import com.wolfcode.car.common.utils.StringUtils;
import com.wolfcode.car.common.utils.ip.IpUtils;
import com.wolfcode.car.framework.manager.AsyncManager;
import com.wolfcode.car.framework.manager.factory.AsyncFactory;
import com.wolfcode.car.system.domain.SysOperLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.context.properties.bind.BindContext;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.Map;

@Component
@Aspect
@Slf4j
public class LogAspect {
    /**
     * 处理完请求后执行
     *
     * @param joinPoint     切入点
     * @param controllerLog 自定义注解Log
     * @param jsonResult    返回jsonResult
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint
     * @param controllerLog
     * @param e
     */
    @AfterThrowing(pointcut = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    private void handleLog(JoinPoint joinPoint, Log controllerLog,
                           Exception e, Object jsonResult) {
        try{
            // 获取当前的用户
            LoginUser loginUser = SecurityUtils.getLoginUser();
            // 操作日志
            SysOperLog operLog = new SysOperLog();
            // 状态
            operLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求地址
            operLog.setOperIp(IpUtils.getIpAddr(ServletUtils.getRequest()));
            // 请求url
            operLog.setOperUrl(ServletUtils.getRequest().getRequestURI());
            // 操作人员
            if (loginUser != null) {
                operLog.setOperName(loginUser.getUsername());
            }
            if (e != null) {
                // 状态
                operLog.setStatus(BusinessStatus.FAIL.ordinal());
                // 错误信息
                operLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
            }
            // 方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            // 操作类型
            operLog.setBusinessType(controllerLog.businessType().ordinal());
            // 标题
            operLog.setTitle(controllerLog.title());
            // 操作任类别
            operLog.setOperatorType(controllerLog.operatorType().ordinal());
            // 请求参数参数的信息
            if (controllerLog.isSaveRequestData()) {
                setRequestValue(joinPoint, operLog);
            }
            // 响应参数
            if (controllerLog.isSaveResponseData() && StringUtils.isNotNull(jsonResult)) {
                operLog.setJsonResult(StringUtils.substring(JSON.toJSONString(jsonResult), 0, 2000));
            }
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOper(operLog));
        }catch (Exception exp){
            log.error("==前置通知异常==");
            log.error("异常信息:{}",exp.getMessage());
            exp.printStackTrace();
        }

    }

    /**
     * 获取请求的参数，放到operLog中
     *
     * @param joinPoint
     * @param operLog
     */
    private void setRequestValue(JoinPoint joinPoint, SysOperLog operLog) {
        //对PUT或POST进行特殊处理（文件上传的参数，过滤掉）
        String requestMethod = operLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operLog.setOperParam(StringUtils.substring(params, 0, 2000));
        } else {
            //获取@PathVariable 的参数值
            Map paramsMap = (Map) ServletUtils.getRequest().
                    getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            operLog.setOperParam(StringUtils.substring(paramsMap.toString(), 0, 2000));
        }
    }

    /**
     * 参数拼接，排除文件上传
     * MultipartFile
     *
     * @param args
     * @return
     */
    private String argsArrayToString(Object[] args) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotEmpty(args)) {
            for (Object arg : args) {
                if (StringUtils.isNotNull(arg) && !isFilterObject(arg)) {
                    sb.append(JSON.toJSON(arg));
                }
            }
        }
        return sb.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象
     *
     * @param arg 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    private boolean isFilterObject(Object arg) {
        Class<?> clazz = arg.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) arg;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if(Map.class.isAssignableFrom(clazz)){
            Map map=(Map)arg;
            for (Object value : map.entrySet()) {
               Map.Entry entry = (Map.Entry)value;
               return entry.getValue() instanceof  MultipartFile;
            }
        }
        return arg instanceof MultipartFile || arg instanceof HttpServletRequest
                || arg instanceof HttpServletResponse || arg instanceof BindResult;
    }
}
