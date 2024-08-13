package com.example.wangjian.syslog1.config;


import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.wangjian.syslog1.entity.LogFrom;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Aspect
@Order(5)
@Component
public class LogAspect {

    private static final ThreadLocal<LogFrom> threadLocal = new ThreadLocal<>();


    @Pointcut("@annotation(com.example.wangjian.syslog1.aop.Syslog)")
    public void pointcut(){

    }

    @Before("pointcut()")
    public void advice(JoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String clientIp = request.getRemoteAddr();
            String url = request.getRequestURI().toString();
            String type = request.getMethod();
            String path  = request.getServletPath();

            Object[] args = joinPoint.getArgs();
            Signature signature  = joinPoint.getSignature();
            MethodSignature methodSignature = (MethodSignature) signature;
            String[] parameterNames = methodSignature.getParameterNames();
            StringBuffer stringBuffer = new StringBuffer();
            for(int i=0,len=parameterNames.length;i<len;i++) {
                stringBuffer.append(args[i]);
            }
            LogFrom logFrom = new LogFrom();
            logFrom.setId("111");
            logFrom.setName("222");
            logFrom.setType(type);
            logFrom.setClientIp(clientIp);
            logFrom.setUrl(url);
            logFrom.setPath(path);
            logFrom.setTempTime(start);
            logFrom.setParam(stringBuffer.toString());
            threadLocal.set(logFrom);
        }

    }

    @AfterReturning(returning = "ret",pointcut = "pointcut()")
    public void doAfterReturning(Object ret){
        String jsonStr = JSONUtil.parseObj(ret).toString();
        LogFrom logFrom = threadLocal.get();
        logFrom.setResponse(jsonStr);
        long times = logFrom.getTempTime();
        long runtime  = System.currentTimeMillis()-times;
        logFrom.setRuntime(runtime);
        log.error("持久化数据库--->{}", JSONObject.toJSONString(logFrom));
        threadLocal.remove();
    }

}
