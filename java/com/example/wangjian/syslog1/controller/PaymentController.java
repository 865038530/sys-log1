package com.example.wangjian.syslog1.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wangjian.syslog1.aop.Syslog;
import com.example.wangjian.syslog1.entity.LogFrom;
import com.example.wangjian.syslog1.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
@Api(value = "测试接口", tags = "用户登录接口")
public class PaymentController {

    @Autowired
    private UserService userService;



    @PostMapping("/createOrder")
    @ApiOperation(value = "创建订单", tags = "创建订单")
    @Syslog(type = "创建订单")
    public String createOrder(String username, String password){
        LogFrom logForm1  = new LogFrom();
        logForm1.setId("111");
        logForm1.setName("222");
        logForm1.setType("333");
        logForm1.setUrl("444");
        logForm1.setPath("555");
        logForm1.setTempTime(666);
        logForm1.setParam("777");
        userService.checkUser(username,password);
        return JSONObject.toJSONString(logForm1);

    }

}

