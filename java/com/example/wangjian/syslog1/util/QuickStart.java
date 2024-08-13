package com.example.wangjian.syslog1.util;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;

/** Native 支付下单为例 */
public class QuickStart {

    /** 商户号 */
    public static String merchantId = "1682702563";
    /** 商户API私钥路径 */
    public static String privateKeyPath = "C:\\mywecat\\1682702563_20240806_cert\\apiclient_key.pem";
    /** 商户证书序列号 */
    public static String merchantSerialNumber = "595537B7F38528B1C5204F71A1CD30EC3A3447AF";
    /** 商户APIV3密钥 */
    public static String apiV3Key = "f30fe11545b3beb62b3e5fb9171cd0a5";

    public static void main(String[] args) {
        // 使用自动更新平台证书的RSA配置
        // 一个商户号只能初始化一个配置，否则会因为重复的下载任务报错
        Config config =
                new RSAAutoCertificateConfig.Builder()
                        .merchantId(merchantId)
                        .privateKeyFromPath(privateKeyPath)
                        .merchantSerialNumber(merchantSerialNumber)
                        .apiV3Key(apiV3Key)
                        .build();
        // 构建service
        NativePayService service = new NativePayService.Builder().config(config).build();
        // request.setXxx(val)设置所需参数，具体参数可见Request定义
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(1);
        request.setAmount(amount);
        request.setAppid("wxf66bcdbe7d24ba16");
        request.setMchid(merchantId);
        request.setDescription("测试商品标题");
        request.setNotifyUrl("http://www.yuhangzhifu.cn:8331/log/wxpay/notify");
        request.setOutTradeNo("124444446587989856765");
        // 调用下单方法，得到应答
        PrepayResponse response = service.prepay(request);
        // 使用微信扫描 code_url 对应的二维码，即可体验Native支付
        System.out.println(response.getCodeUrl());
    }
}