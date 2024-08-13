package com.example.wangjian.syslog1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayConfig;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.domain.ComplexLabelRule;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.easysdk.factory.Factory;
import com.example.wangjian.syslog1.config.AliPayConfig;
import com.example.wangjian.syslog1.config.TokenConfig;
import com.example.wangjian.syslog1.entity.*;

import com.example.wangjian.syslog1.service.ProductService;
import com.example.wangjian.syslog1.util.IdUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@Slf4j
@RequestMapping("alipay")
@Transactional(rollbackFor = Exception.class)
public class AliPayController {


    AlipayClient alipayClient;

    @Resource
    AliPayConfig aliPayConfig;

    @Autowired
    private ProductService productService;

    //private static final String GATEWAY_URL ="https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT ="JSON";
    private static final String CHARSET ="utf-8";
    private static final String SIGN_TYPE ="RSA2";

    @Autowired
    private TokenConfig tokenConfig;

    @Value("${alipay.gateWay}")
    private String gateWay;

    @GetMapping("/pay") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pay(AliPayInfo aliPay, HttpServletResponse httpResponse) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient(gateWay, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());

        request.setBizContent("{\"out_trade_no\":\"" + aliPay.getTraceNo() + "\","
                + "\"total_amount\":\"" + aliPay.getTotalAmount() + "\","
                + "\"subject\":\"" + aliPay.getSubject() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    private boolean checkParam(AliPayParam aliPay) {
        boolean flag = true;
        if (null == aliPay) {
            flag = false;
        }
        if (StringUtils.isEmpty(aliPay.getIds())) {
            flag = false;
        }
        if (ObjectUtils.isEmpty(aliPay.getQuantity())) {
            flag = false;
        }
        if (StringUtils.isEmpty(aliPay.getPhone())) {
            flag = false;
        }
        if (StringUtils.isEmpty(aliPay.getUserGame())) {
            flag = false;
        }
        if (StringUtils.isEmpty(aliPay.getOrderId())) {
            flag = false;
        }
        if (StringUtils.isEmpty(aliPay.getServiceRange())) {
            flag = false;
        }
        return flag;
    }

    @GetMapping("/pays") // &subject=xxx&traceNo=xxx&totalAmount=xxx
    public void pays(AliPayParam aliPay, HttpServletResponse httpResponse) throws Exception {
        if (!checkParam(aliPay)) {
            throw new Exception("参数有误,请检查后,重新发起!");
        }
        AlipayClient alipayClient = new DefaultAlipayClient(gateWay, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        String orderId = aliPay.getOrderId();
        String productId = aliPay.getIds();
        ProductFrom productFrom = productService.checkProduct(productId);
        if (productFrom == null) {
            throw new Exception("商品信息不存在!");
        }

        int amount =  aliPay.getQuantity() * productFrom.getAmount();

        //aliPay.setTotalAmount(99);
        request.setBizContent("{\"out_trade_no\":\"" + orderId + "\","
                + "\"total_amount\":\"" + amount + "\","
                + "\"user_game\":\"" + aliPay.getUserGame() + "\","
                + "\"ids\":\"" + productId + "\","
                + "\"quantity\":\"" + aliPay.getQuantity() + "\","
                + "\"phone\":\"" + aliPay.getPhone() + "\","
                + "\"serviceRange\":\"" + aliPay.getServiceRange() + "\","
                + "\"subject\":\"" + productFrom.getName() + "\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        String form = "";
        try {
            // 调用SDK生成表单
            form = alipayClient.pageExecute(request).getBody();
            // 订单入库
            AliPay pay = new AliPay();
            pay.setTraceNo(orderId);
            pay.setTotalAmount(amount);
            pay.setOrderNum(aliPay.getQuantity());
            pay.setOrderPrice(productFrom.getAmount());
            pay.setProductId(productFrom.getId());
            pay.setSubject(productFrom.getName());
            pay.setUserGameId(aliPay.getUserGame());
            pay.setUserPhone(aliPay.getPhone());
            pay.setServiceRange(aliPay.getServiceRange());
            pay.setId(UUID.randomUUID().toString());
            productService.insetAlipaymentOrderInfo(pay);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        // 直接将完整的表单html输出到页面
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }





//    private static AlipayConfig getAlipayConfig() {
//        String privateKey  = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCSXRHi5xnNW4fis/aFi1z5jf0nbUElwf/L4ZuYI16WBYTaHS34Ipjqzfshc8JGTuhVyW276En3eWOT73wRyRQmRw308cJr7oxioXYZnRYftn+cmp3B06/IIUlgPUH/okBy++5r0bDtlTcGashhL106dtNw9nAJvGGjuJeAD20N9wrzG7yK1Cd3BkwdsI47QGR99OSeH2eDsMeNI2o7pJbWVG/3m1LdtQpD6hFUvPzqMB7+AU5oLmOZC+Tu2eqlgxfsi+O9Ihgyah9kqgGEy+xqTwuk/RFB0y/bxUqsToD8twuNEfriDUEpYnMTtFD4WvKRCaODj7bb5Z2nmq0H8OyXAgMBAAECggEAP4fUfrgvc/saBaZ2CEuQ5OXkD9wVni0SOk5IeoZHxTjKDN8DmCR0Wd9k9YVIu7n+kVYooprWmGwBdDJMmW+9pkvLXBogeTcHirxpBf8wnj1aMQDQH1UihO0l63dLkYm9Dewa9oQDl2zggJGTPtQPVJRFfVALG77AZw91+2k2vgtokxAb00PwhVxdjxbP1T3OTxc5lsdraEXo0pt1HhEK5T7S2cHAFSwBJpbOeDN5YW5XRUvy3VvPXTStU2X6jf+Y0Qp5jQbmWVJ1dCLfkVVESK5R9Wl8SrvEyME8RgTWNJdogE6hoHuQcyUvFNxEE9G1obc9NDYQvDQsyoZ9HQUqeQKBgQDBomD5iKoyzsTGF81D1wPb5GbsOq2eRv8cfM48gAr4ARn5j33nJeiaxpk9Af3KJ8HoT24eIaXqNuP8B6bFaORVUS12I09y7LznpG03Uk/mwhHBCgpHwXxUavgorVZ0kGe003VJ22I7T+VlQ1mHkJvm7x8hNp6T4TPndaRQjW7ulQKBgQDBgRphER9mzN/rRNDcZyfLZItmPdY3I1KLwD8UQFrOce8OtCux50JwYlPaQzuWwBfeeaF4BfeCOHG7/xlRdYL9hyLUhSXLdCjAby3F2UbnP9fmmYN19blkyBWTdui/dUkcSW+bLMvA5n7g9iXqw7X/SCHdHcikCMUNDtzsfO9fewKBgFcSS4UqHU6nh/iD73uYDlYtCNMlN7k6t7D5tkuiUExhQmrkSXtNoqEmuPWkFZl380whm6TVAKSndjF4x1dx6WssC8NZ8Xn+VpvbZnk//EoJ7q3dc+38ZYoYZ+rytzPHU7sOc2l4y11cegzlEjdRoalg0aC/zOM2m35zeiX203FZAoGAH5Y9gh7Ta2qCGtuO5IuKOW9eOgBaGYIQlXN6WInwyMZcaaALiezgMYw7d+OFtKQCwXRFYVxH/N/N4QxRS7TM/ymyC9L4Sm/Zvx/m7ub65gDL836XsQe9jbADpaDBeY9hsdZi+4eicwitKXfE1L/B07womvmjqPMth2Yntgrm4EUCgYB7UFNj0pykFz6L7ZaWDTPwkaV0NPAxeFNaYXCarsSAOwiJknksg9oUjmliECb4DOwQsOg2N+VRIooOn/hIHoG7VJIySD9SfC+hldqpinZfIJyjV0/epICxaxqlHwJ/5vsoHvMZh/KnEuwtQ1PW86nVjhlduUtRMO4Rmpj/c0G81A==";
//        String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhJxuL+3FpGcFURgww0zg3+9S/qnMg9tC7bCqM2zlWByd4/Hbov8MGm2yfaBoEFzYxSVzj+n3vCBXnuWIStis5OwrkX2XQxJ6aRSmAdIZs6Ry8OheSsu1kv7b5VgMzhV3tuXORL7XdF/GMNo2wfANSQ46wIj41+ojTgk8XYjBppcmjmhtfMteypanLg9dniEie5o/MHjING4chzkHzmVBEiTWdhiVD9pSZOZdpant80PeMWdbTvulLm1J68Tp/VT/wC6oTexo7xjipErs+vQn907F9LSGubt+8P6BA9/ieiDKKe1xhJwhJtRUTZICAwDBQk6KBkZONkoZoZPGDM/3nQIDAQAB";
//        AlipayConfig alipayConfig = new AlipayConfig();
//        alipayConfig.setServerUrl("https://openapi-sandbox.dl.alipaydev.com/gateway.do");
//        alipayConfig.setAppId("9021000139647982");
//        alipayConfig.setPrivateKey(privateKey);
//        alipayConfig.setFormat("json");
//        alipayConfig.setAlipayPublicKey(alipayPublicKey);
//        alipayConfig.setCharset("UTF-8");
//        alipayConfig.setSignType("RSA2");
//        return alipayConfig;
//    }

    @PostMapping("/notify")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest request) throws Exception {
        System.out.println("=========支付宝异步回调========");
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            System.out.println("=========支付宝异步回调业务逻辑开始========");

            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            for (String name : requestParams.keySet()) {
                params.put(name, request.getParameter(name));
                // System.out.println(name + " = " + request.getParameter(name));
            }

            String tradeNo = params.get("out_trade_no");
            String gmtPayment = params.get("gmt_payment");
            String alipayTradeNo = params.get("trade_no");
            log.info("回调参数--{}",params);
            // 支付宝验签
            if (Factory.Payment.Common().verifyNotify(params)) {
                // 验签通过
                String subject = params.get("subject");
                System.out.println("交易名称: " + params.get("subject"));
                System.out.println("交易状态: " + params.get("trade_status"));
                System.out.println("支付宝交易凭证号: " + params.get("trade_no"));
                System.out.println("商户订单号: " + params.get("out_trade_no"));
                System.out.println("交易金额: " + params.get("total_amount"));
                System.out.println("买家在支付宝唯一id: " + params.get("buyer_id"));
                System.out.println("买家付款时间: " + params.get("gmt_payment"));
                System.out.println("买家付款金额: " + params.get("buyer_pay_amount"));
                int cot = productService.checkOrderSuccess(params.get("out_trade_no"));
                if (cot == 0) {
                    // 更新订单未已支付
                    AliPay aliPay = new AliPay();
                    aliPay.setTraceNo(params.get("out_trade_no"));
                    aliPay.setAlipayTraceNo(params.get("trade_no"));
                    aliPay.setCustomerId(params.get("buyer_id"));
                    aliPay.setPayTime(params.get("gmt_payment"));
                    aliPay.setPayAmount(params.get("buyer_pay_amount"));
                    aliPay.setOrderStatus(2);
                    productService.updateOrderNotify(aliPay);
                    CompletableFuture.runAsync(()->{
                        String resStr = "";
                        try {
                            // 去游戏库更新游戏币
                            GameUserFrom gameUserFrom = productService.getOrderInfo(params.get("out_trade_no"));
                            String serviceRange = convertServiceRange(gameUserFrom.getServiceRange());
                            String amount = gameUserFrom.getAmount().split("\\.")[0];
                            String tokenStr = tokenConfig.createToken(gameUserFrom.getUserGameId(),
                                    amount,serviceRange);
                            log.info("异步执行,{}单号{}",tokenStr,params.get("out_trade_no"));
                            // todo 调用游戏服务get
                            CommonId commonId = new CommonId();
                            commonId.setKey(tokenStr);
                            resStr = tokenConfig.sendPOst(commonId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            // todo 更新推送状态
                            // todo 更新推送状态
                            int sendStatus = 0;
                            if ("success".equals(resStr)) {
                                sendStatus = 1;
                            }
                            if ("fail".equals(resStr)) {
                                sendStatus = 3;
                            }
                            if ("success-isNot".equals(resStr)) {
                                sendStatus = 2;
                            }
                            productService.updateOrderSend(params.get("out_trade_no"),sendStatus);
                        }
                    });
                }
            }
        }
        return "success";
    }

    @PostMapping("/queryOrderStatus")  // 注意这里必须是POST接口
    public String queryOrderStatus(@RequestParam(value="orderId",required=true)String orderId) throws Exception {
        //初始化支付宝配置
        AlipayClient alipayClient = new DefaultAlipayClient(gateWay, aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(), FORMAT, CHARSET, aliPayConfig.getAlipayPublicKey(), SIGN_TYPE);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderId);
        request.setBizModel(model);

        // 设置查询选项
        List<String> queryOptions = new ArrayList<String>();
        queryOptions.add("trade_settle_info");
        model.setQueryOptions(queryOptions);

        request.setBizModel(model);
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        System.out.println(response.getBody());

        if (response.isSuccess()) {
            System.out.println("调用成功");
            if (StringUtils.isNotEmpty(response.getBody())) {
                JSONObject jsonObject = JSON.parseObject(response.getBody());
                JSONObject res = JSON.parseObject(jsonObject.getString("alipay_trade_query_response"));
                if ("TRADE_SUCCESS".equals(res.get("trade_status").toString())) {
                    System.out.printf(res.get("trade_status").toString());
                    int cot = productService.checkOrderSuccess(res.getString("out_trade_no"));
                    if (cot == 0) {
                        // 还没异步通知，把订单更新一下
                        // 更新订单未已支付
                        AliPay aliPay = new AliPay();
                        aliPay.setTraceNo(res.getString("out_trade_no"));
                        aliPay.setAlipayTraceNo(res.getString("trade_no"));
                        aliPay.setCustomerId(res.getString("buyer_logon_id"));
                        aliPay.setPayTime(res.getString("send_pay_date"));
                        aliPay.setPayAmount(res.getString("total_amount"));
                        aliPay.setOrderStatus(2);
                        productService.updateOrderNotify(aliPay);
                        CompletableFuture.runAsync(()->{
                            String resStr = "";
                            try {
                                // 去游戏库更新游戏币
                                GameUserFrom gameUserFrom = productService.getOrderInfo(res.getString("out_trade_no"));
                                String serviceRange = convertServiceRange(gameUserFrom.getServiceRange());
                                String amount = gameUserFrom.getAmount().split("\\.")[0];
                                String tokenStr = tokenConfig.createToken(gameUserFrom.getUserGameId(),
                                        amount,serviceRange);
                                log.info("手动更新执行,{}单号{}",tokenStr,res.getString("out_trade_no"));
                                // todo 调用游戏服务
                                CommonId commonId = new CommonId();
                                commonId.setKey(tokenStr);
                                resStr = tokenConfig.sendPOst(commonId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                // todo 更新推送状态
                                int sendStatus = 0;
                                if ("success".equals(resStr)) {
                                    sendStatus = 1;
                                }
                                if ("fail".equals(resStr)) {
                                    sendStatus = 3;
                                }
                                if ("success-isNot".equals(resStr)) {
                                    sendStatus = 2;
                                }
                                productService.updateOrderSend(res.getString("out_trade_no"),sendStatus);
                            }
                        });

                    }
                    return "0";
                }
            }
        } else {
            System.out.println("调用失败");
            // sdk版本是"4.38.0.ALL"及以上,可以参考下面的示例获取诊断链接
            // String diagnosisUrl = DiagnosisUtils.getDiagnosisUrl(response);
            // System.out.println(diagnosisUrl);
        }
        return "1";
    }



    private String convertServiceRange(String range){
        String str = "1";
        if ("二区".equals(range)) {
            str = "2";
        } else if ("三区".equals(range)) {
            str = "3";
        }
        else if ("四区".equals(range)) {
            str = "4";
        }
        else if ("五区".equals(range)) {
            str = "5";
        }
        return str;
    }









}

