package com.example.wangjian.syslog1.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.wangjian.syslog1.aop.Syslog;
import com.example.wangjian.syslog1.entity.LogFrom;
import com.example.wangjian.syslog1.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
@Api(value = "测试接口", tags = "用户登录接口")
public class LoginController {

    @Autowired
    private UserService userService;


    @PostMapping("/login")
    @ApiOperation(value = "登录", tags = "登录接口")
    public String login(String username, String password){
        return "登录成功"+username+password;
    }

    @PostMapping("/logout")
    @ApiOperation(value="退出登录",tags = "退出登录")
    public String logout(String username){
        return "退出登录成功";
    }

    @PostMapping("/login1")
    @ApiOperation(value = "登录", tags = "登录接口")
    @Syslog(type = "登录")
    public String login1(String username, String password){
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



    @GetMapping("/index")
    public String index(HttpSession httpSession) {
        httpSession.setAttribute("name", "springboot-jsp");
        return "index"; // 返回 JSP 页面的名称，不包括后缀
    }


}

