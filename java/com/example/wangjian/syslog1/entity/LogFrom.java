package com.example.wangjian.syslog1.entity;


import lombok.Data;

@Data
public class LogFrom {

    private String id;

    private String name;

    private String clientIp;

    private String url;

    private String path;

    private String type;

    private String param;

    private String response;

    private long runtime;

    private long tempTime;



}
