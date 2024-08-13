//package com.example.wangjian.syslog1.config;
//
//import com.wechat.pay.java.core.Config;
//import com.wechat.pay.java.core.RSAAutoCertificateConfig;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//@Configuration
//@Data
//@Component
//public class WxPayConfig {
//
//    private static volatile Config config;
//
//    /** 商户号 */
//    @Value("${wechat.pay.mchId}")
//    public  String mchId;
//
//    /** 商户API私钥路径 */
//    @Value("${wechat.pay.privateKeyPath}")
//    public  String privateKeyPath;
//
//    /** 商户证书序列号 */
//    @Value("${wechat.pay.merchantSerialNumber}")
//    public  String merchantSerialNumber;
//
//    /** 商户APIV3密钥 */
//    @Value("${wechat.pay.apiV3key}")
//    public  String apiV3key;
//
//
//    //SDK 提供的定时更新平台证书
//    @Bean
//    public Config getConfig(WxPayConfig wxPayConfig) {
//        if (config == null) {
//            config = new RSAAutoCertificateConfig.Builder()
//                    .merchantId(mchId)
//                    .privateKeyFromPath("privateKeyPath")
//                    .merchantSerialNumber(merchantSerialNumber)
//                    .apiV3Key(apiV3key)
//                    .build();
//        }
//        return config;
//    }
//
//}
