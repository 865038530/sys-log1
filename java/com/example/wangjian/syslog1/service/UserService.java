package com.example.wangjian.syslog1.service;

import com.example.wangjian.syslog1.entity.OrderFrom;

public interface UserService {

    void checkUser(String account,String password);

    OrderFrom getOrder();
}
