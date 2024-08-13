package com.example.wangjian.syslog1.service.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.ExtUserInfo;
import com.alipay.api.domain.InvoiceKeyInfo;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.domain.InvoiceInfo;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.domain.ExtendParams;
import com.alipay.api.domain.GoodsDetail;
import com.alipay.api.domain.SubMerchant;

import com.alipay.api.FileItem;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class AlipayTradePagePay {

    public static void main(String[] args) throws AlipayApiException {
        // 初始化SDK
        AlipayClient alipayClient = new DefaultAlipayClient(getAlipayConfig());

        // 构造请求参数以调用接口
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        // 设置商户门店编号
        //model.setStoreId("NJ_001");

        // 设置订单绝对超时时间
        model.setTimeExpire("2024-07-21 10:00:00");

        // 设置业务扩展参数
//        ExtendParams extendParams = new ExtendParams();
//        extendParams.setSysServiceProviderId("2088511833207846");
//        extendParams.setHbFqSellerPercent("100");
//        extendParams.setHbFqNum("3");
//        extendParams.setIndustryRefluxInfo("{\"scene_code\":\"metro_tradeorder\",\"channel\":\"xxxx\",\"scene_data\":{\"asset_name\":\"ALIPAY\"}}");
//        extendParams.setSpecifiedSellerName("XXX的跨境小铺");
//        extendParams.setRoyaltyFreeze("true");
//        extendParams.setCardType("S0JP0000");
//        model.setExtendParams(extendParams);

        // 设置订单标题
        model.setSubject("Iphone6 16G");

        // 设置请求来源地址
        model.setRequestFromUrl("https://");

        // 设置产品码
        model.setProductCode("FAST_INSTANT_TRADE_PAY");

        // 设置PC扫码支付的方式
        model.setQrPayMode("1");

        // 设置商户自定义二维码宽度
        model.setQrcodeWidth(100L);

        // 设置请求后页面的集成方式
        model.setIntegrationType("PCWEB");

        // 设置订单包含的商品列表信息
//        List<GoodsDetail> goodsDetail = new ArrayList<GoodsDetail>();
//        GoodsDetail goodsDetail0 = new GoodsDetail();
//        goodsDetail0.setGoodsName("ipad");
//        goodsDetail0.setAlipayGoodsId("20010001");
//        goodsDetail0.setQuantity(1L);
//        goodsDetail0.setPrice("2000");
//        goodsDetail0.setGoodsId("apple-01");
//        goodsDetail0.setGoodsCategory("34543238");
//        goodsDetail0.setCategoriesTree("124868003|126232002|126252004");
//        goodsDetail0.setShowUrl("http://www.alipay.com/xxx.jpg");
//        goodsDetail.add(goodsDetail0);
//        model.setGoodsDetail(goodsDetail);

        // 设置商户的原始订单号
        model.setMerchantOrderNo(UUID.randomUUID().toString());

//        // 设置二级商户信息
//        SubMerchant subMerchant = new SubMerchant();
//        subMerchant.setMerchantId("2088000603999128");
//        subMerchant.setMerchantType("alipay");
//        model.setSubMerchant(subMerchant);

        // 设置开票信息
//        InvoiceInfo invoiceInfo = new InvoiceInfo();
//        InvoiceKeyInfo keyInfo = new InvoiceKeyInfo();
//        keyInfo.setTaxNum("1464888883494");
//        keyInfo.setIsSupportInvoice(true);
//        keyInfo.setInvoiceMerchantName("ABC|003");
//        invoiceInfo.setKeyInfo(keyInfo);
//        invoiceInfo.setDetails("[{\"code\":\"100294400\",\"name\":\"服饰\",\"num\":\"2\",\"sumPrice\":\"200.00\",\"taxRate\":\"6%\"}]");
//        model.setInvoiceInfo(invoiceInfo);

        // 设置商户订单号
        model.setOutTradeNo("20150320010101001");

        // 设置外部指定买家
//        ExtUserInfo extUserInfo = new ExtUserInfo();
//        extUserInfo.setCertType("IDENTITY_CARD");
//        extUserInfo.setCertNo("362334768769238881");
//        extUserInfo.setName("李明");
//        extUserInfo.setMobile("16587658765");
//        extUserInfo.setMinAge("18");
//        extUserInfo.setNeedCheckInfo("F");
//        extUserInfo.setIdentityHash("27bfcd1dee4f22c8fe8a2374af9b660419d1361b1c207e9b41a754a113f38fcc");
        //model.setExtUserInfo(extUserInfo);

        // 设置订单总金额
        model.setTotalAmount("88.88");

        // 设置商户传入业务信息
        model.setBusinessParams("{\"mc_create_trade_ip\":\"127.0.0.1\"}");

        // 设置优惠参数
        model.setPromoParams("{\"storeIdType\":\"1\"}");

        request.setBizModel(model);
        // 第三方代调用模式下请设置app_auth_token
        // request.putOtherTextParam("app_auth_token", "<-- 请填写应用授权令牌 -->");

        AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "POST");
        // 如果需要返回GET请求，请使用
        // AlipayTradePagePayResponse response = alipayClient.pageExecute(request, "GET");
        String pageRedirectionData = response.getBody();
        log.info("返回脚本-{}",pageRedirectionData);

        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            // System.out.println(diagnosisUrl);
        }
    }

    private static AlipayConfig getAlipayConfig() {
        String privateKey  = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCSXRHi5xnNW4fis/aFi1z5jf0nbUElwf/L4ZuYI16WBYTaHS34Ipjqzfshc8JGTuhVyW276En3eWOT73wRyRQmRw308cJr7oxioXYZnRYftn+cmp3B06/IIUlgPUH/okBy++5r0bDtlTcGashhL106dtNw9nAJvGGjuJeAD20N9wrzG7yK1Cd3BkwdsI47QGR99OSeH2eDsMeNI2o7pJbWVG/3m1LdtQpD6hFUvPzqMB7+AU5oLmOZC+Tu2eqlgxfsi+O9Ihgyah9kqgGEy+xqTwuk/RFB0y/bxUqsToD8twuNEfriDUEpYnMTtFD4WvKRCaODj7bb5Z2nmq0H8OyXAgMBAAECggEAP4fUfrgvc/saBaZ2CEuQ5OXkD9wVni0SOk5IeoZHxTjKDN8DmCR0Wd9k9YVIu7n+kVYooprWmGwBdDJMmW+9pkvLXBogeTcHirxpBf8wnj1aMQDQH1UihO0l63dLkYm9Dewa9oQDl2zggJGTPtQPVJRFfVALG77AZw91+2k2vgtokxAb00PwhVxdjxbP1T3OTxc5lsdraEXo0pt1HhEK5T7S2cHAFSwBJpbOeDN5YW5XRUvy3VvPXTStU2X6jf+Y0Qp5jQbmWVJ1dCLfkVVESK5R9Wl8SrvEyME8RgTWNJdogE6hoHuQcyUvFNxEE9G1obc9NDYQvDQsyoZ9HQUqeQKBgQDBomD5iKoyzsTGF81D1wPb5GbsOq2eRv8cfM48gAr4ARn5j33nJeiaxpk9Af3KJ8HoT24eIaXqNuP8B6bFaORVUS12I09y7LznpG03Uk/mwhHBCgpHwXxUavgorVZ0kGe003VJ22I7T+VlQ1mHkJvm7x8hNp6T4TPndaRQjW7ulQKBgQDBgRphER9mzN/rRNDcZyfLZItmPdY3I1KLwD8UQFrOce8OtCux50JwYlPaQzuWwBfeeaF4BfeCOHG7/xlRdYL9hyLUhSXLdCjAby3F2UbnP9fmmYN19blkyBWTdui/dUkcSW+bLMvA5n7g9iXqw7X/SCHdHcikCMUNDtzsfO9fewKBgFcSS4UqHU6nh/iD73uYDlYtCNMlN7k6t7D5tkuiUExhQmrkSXtNoqEmuPWkFZl380whm6TVAKSndjF4x1dx6WssC8NZ8Xn+VpvbZnk//EoJ7q3dc+38ZYoYZ+rytzPHU7sOc2l4y11cegzlEjdRoalg0aC/zOM2m35zeiX203FZAoGAH5Y9gh7Ta2qCGtuO5IuKOW9eOgBaGYIQlXN6WInwyMZcaaALiezgMYw7d+OFtKQCwXRFYVxH/N/N4QxRS7TM/ymyC9L4Sm/Zvx/m7ub65gDL836XsQe9jbADpaDBeY9hsdZi+4eicwitKXfE1L/B07womvmjqPMth2Yntgrm4EUCgYB7UFNj0pykFz6L7ZaWDTPwkaV0NPAxeFNaYXCarsSAOwiJknksg9oUjmliECb4DOwQsOg2N+VRIooOn/hIHoG7VJIySD9SfC+hldqpinZfIJyjV0/epICxaxqlHwJ/5vsoHvMZh/KnEuwtQ1PW86nVjhlduUtRMO4Rmpj/c0G81A==";
        String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhJxuL+3FpGcFURgww0zg3+9S/qnMg9tC7bCqM2zlWByd4/Hbov8MGm2yfaBoEFzYxSVzj+n3vCBXnuWIStis5OwrkX2XQxJ6aRSmAdIZs6Ry8OheSsu1kv7b5VgMzhV3tuXORL7XdF/GMNo2wfANSQ46wIj41+ojTgk8XYjBppcmjmhtfMteypanLg9dniEie5o/MHjING4chzkHzmVBEiTWdhiVD9pSZOZdpant80PeMWdbTvulLm1J68Tp/VT/wC6oTexo7xjipErs+vQn907F9LSGubt+8P6BA9/ieiDKKe1xhJwhJtRUTZICAwDBQk6KBkZONkoZoZPGDM/3nQIDAQAB";
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi-sandbox.dl.alipaydev.com/gateway.do");
        alipayConfig.setAppId("9021000139647982");
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setFormat("json");
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        alipayConfig.setCharset("UTF-8");
        alipayConfig.setSignType("RSA2");
        return alipayConfig;
    }
}
