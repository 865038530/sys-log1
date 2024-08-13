package com.example.wangjian.syslog1.service.impl;

import com.example.wangjian.syslog1.config.TokenConfig;
import com.example.wangjian.syslog1.entity.OrderFrom;
import com.example.wangjian.syslog1.mapper.OrderMapper;
import com.example.wangjian.syslog1.service.UserService;
import jdk.nashorn.internal.parser.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final OrderMapper orderMapper;

    @Override
    public void checkUser(String account, String password) {
        String token  = TokenConfig.createToken(UUID.randomUUID().toString(),"zhangsan","lisi","wangwu");
        log.error("token-->{}",token);
        // 将token 返回给前端，设置到redis中
        //TokenConfig.getTokenRes(token);
    }

    @Override
    public OrderFrom getOrder() {
        return orderMapper.getOrder();
    }
}
