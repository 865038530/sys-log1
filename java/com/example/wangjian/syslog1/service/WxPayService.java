package com.example.wangjian.syslog1.service;

import com.example.wangjian.syslog1.entity.OrderFrom;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface WxPayService {


    void callBack(HttpServletRequest request, HttpServletResponse response);


    String queryOrderStatus(String orderId);

    String queryOrderStatusMyData(String orderId);
}
