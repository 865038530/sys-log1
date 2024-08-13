package com.example.wangjian.syslog1.entity;

import lombok.Data;

@Data
public class AliPayInfo {
    private String traceNo;
    private double totalAmount;
    private String subject;
    private String alipayTraceNo;
}
