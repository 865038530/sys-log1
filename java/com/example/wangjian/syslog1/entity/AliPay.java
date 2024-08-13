package com.example.wangjian.syslog1.entity;

import lombok.Data;

@Data
public class AliPay {

    private String id;
    // 商户单号
    private String traceNo;
    //金额
    private double totalAmount;
    //数量
    private int orderNum;
    // 订单单价
    private int orderPrice;
    // 订单单价
    private String productId;
    // 商品名称
    private String subject;
    // 支付宝单号
    private String alipayTraceNo;
    // 用户游戏id
    private String userGameId;
    // 所在大区
    private String serviceRange;
    // 用户手机号
    private String userPhone;
    // 暂时不用
    private String alipayTradeNo;
   // 买家支付宝id
    private String customerId;
    //付款时间
    private String payTime;
   // 买家付款就金额
    private String payAmount;
    // 订单号
    private int orderStatus = 1;
    // 支付方式 1 = 支付宝 2 = 微信
    private int payType = 1;

}
