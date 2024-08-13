package com.example.wangjian.syslog1.config;

import com.example.wangjian.syslog1.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Handler;
import java.util.regex.Pattern;

@Slf4j
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private static final String writeStr = "(.*)login(.*)|(.*)register(.*)";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler) {
        String url = request.getRequestURI();
        //log.error("请求地址-->{}",url);
        if (url.contains("swagger") || url.contains("log/error"))
            return true;

        Pattern pattern = Pattern.compile(writeStr);
        if (pattern.matcher(request.getRequestURI()).matches()) {
            return true;
        }
//        String token = request.getHeader("token");
//        if (StringUtils.isEmpty(token)) {
//            throw new BusinessException("token is null");
//        }

        // 解析token,放入request
        //TokenConfig.getTokenRes(token);

        request.setAttribute("userName","wangjain");
        request.setAttribute("number","wangjain");

        return true;
    }


}
