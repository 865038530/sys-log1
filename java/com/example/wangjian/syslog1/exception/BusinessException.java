package com.example.wangjian.syslog1.exception;

public class BusinessException extends RuntimeException{

    private String code;

    private String msg;

    public BusinessException(String code,String msg){
        this.msg = msg;
        this.code = code;
    }

    public BusinessException(String msg){
        this.msg = msg;
    }

}
