package com.example.wangjian.syslog1.util;

import com.example.wangjian.syslog1.exception.BusinessException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CommonUser {

    public static String getUserId (){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Object obj = attributes.getRequest().getAttribute("userName");
        if (null == obj) {
            throw new BusinessException("get user error");
        }
        return (String) obj;
    }
}
