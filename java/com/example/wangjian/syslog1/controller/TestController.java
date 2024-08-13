package com.example.wangjian.syslog1.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.wangjian.syslog1.entity.LogFrom;
import com.example.wangjian.syslog1.entity.OrderFrom;
import com.example.wangjian.syslog1.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags="测试")
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserService userService;

    @ApiOperation("测试")
    @PostMapping("/test")
    public String test(@RequestBody LogFrom logForm) {
        LogFrom logForm1  = new LogFrom();
        logForm1.setId("111");
        logForm1.setName("222");
        logForm1.setType("333");
        logForm1.setUrl("444");
        logForm1.setPath("555");
        logForm1.setTempTime(666);
        logForm1.setParam("777");

        return JSONObject.toJSONString(logForm1);
    }

    @ApiOperation("获取信息")
    @PostMapping("/getOrder")
    public OrderFrom getOrder(@RequestBody LogFrom logForm) {
        return userService.getOrder();
    }

}
