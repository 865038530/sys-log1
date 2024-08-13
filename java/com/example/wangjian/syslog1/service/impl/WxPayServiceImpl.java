package com.example.wangjian.syslog1.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.wangjian.syslog1.config.TokenConfig;
import com.example.wangjian.syslog1.entity.AliPay;
import com.example.wangjian.syslog1.entity.CommonId;
import com.example.wangjian.syslog1.entity.GameUserFrom;
import com.example.wangjian.syslog1.service.ProductService;
import com.example.wangjian.syslog1.service.WxPayService;
import com.ijpay.core.IJPayHttpResponse;
import com.ijpay.core.enums.RequestMethodEnum;
import com.ijpay.core.kit.HttpKit;
import com.ijpay.core.kit.WxPayKit;
import com.ijpay.wxpay.WxPayApi;
import com.ijpay.wxpay.enums.WxDomainEnum;
import com.ijpay.wxpay.enums.v3.BasePayApiEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class WxPayServiceImpl implements WxPayService {



    private static final String SE_NUM = "5BC5958BD5AEAF67149B3AAEDB741AA8D7B8F6D5";

    @Value("${wechat.pay.mchId}")
    public  String mchId;

    /** 商户API私钥路径 */
    @Value("${wechat.pay.privateKeyPath}")
    public  String privateKeyPath;

    /** 商户证书序列号 */
    @Value("${wechat.pay.merchantSerialNumber}")
    public  String merchantSerialNumber;

    /** 商户APIV3密钥 */
    @Value("${wechat.pay.apiV3key}")
    public  String apiV3key;

    @Value("${wechat.pay.platformCertPath}")
    private String platformCertPath;

    @Autowired
    private ProductService productService;

    @Autowired
    private TokenConfig tokenConfig;

    @Override
    public void callBack(HttpServletRequest request, HttpServletResponse response) {
        log.info("收到微信支付回调");
        Map<String, String> map = new HashMap<>(12);
        try {
            String timestamp = request.getHeader("Wechatpay-Timestamp");
            String nonce = request.getHeader("Wechatpay-Nonce");
            String serialNo = request.getHeader("Wechatpay-Serial");
            String signature = request.getHeader("Wechatpay-Signature");

            log.info("timestamp:{} nonce:{} serialNo:{} signature:{}", timestamp, nonce, serialNo, signature);
            String result = HttpKit.readData(request);
            log.info("支付通知密文 {}", result);

            // 需要通过证书序列号查找对应的证书，verifyNotify 中有验证证书的序列号
            String plainText = WxPayKit.verifyNotify(serialNo, result, signature, nonce, timestamp,
                    apiV3key, platformCertPath);

            log.info("支付通知明文 {}", plainText);

            if (StrUtil.isNotEmpty(plainText)) {
                String search = plainText;
                com.alibaba.fastjson.JSONObject jsonObject1 = JSON.parseObject(search);
                log.info("商戶號-->{}",jsonObject1.getString("mchid"));
                log.info("应用id-->{}",jsonObject1.getString("appid"));
                log.info("商户单号-->{}",jsonObject1.getString("out_trade_no"));
                log.info("微信单号-->{}",jsonObject1.getString("transaction_id"));
                log.info("支付状态-->{}",jsonObject1.getString("trade_state"));
                log.info("支付时间-->{}",jsonObject1.getString("success_time"));
                String pay_time = "";
                if (StringUtils.isNotEmpty(jsonObject1.getString("success_time"))) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                    try {
                        Date pay_time1 = df.parse("2024-08-07T10:42:56+08:00");

                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        pay_time = sdf2.format(pay_time1);
                        log.info("时间-{}",pay_time);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                com.alibaba.fastjson.JSONObject payer1 = jsonObject1.getJSONObject("payer");
                log.info("支付账号-->{}",payer1.getString("openid"));
                JSONObject amount1 = jsonObject1.getJSONObject("amount");
                log.info("支付金额-->{}",amount1.getIntValue("total"));
                if ("SUCCESS".equals(jsonObject1.getString("trade_state"))) {
                    System.out.printf(jsonObject1.getString("trade_state"));
                    int cot = productService.checkOrderSuccess(jsonObject1.getString("out_trade_no"));
                    if (cot == 0) {
                        // 还没异步通知，把订单更新一下
                        // 更新订单未已支付
                        AliPay aliPay = new AliPay();
                        aliPay.setTraceNo(jsonObject1.getString("out_trade_no"));
                        aliPay.setAlipayTraceNo(jsonObject1.getString("transaction_id"));
                        aliPay.setCustomerId(jsonObject1.getString("openid"));
                        aliPay.setPayTime(pay_time);
                        aliPay.setPayAmount(String.valueOf(amount1.getIntValue("total") / 100));
                        aliPay.setOrderStatus(2);
                        productService.updateOrderNotify(aliPay);
                        CompletableFuture.runAsync(()->{
                            String resStr = "";
                            try {
                                // 去游戏库更新游戏币
                                GameUserFrom gameUserFrom = productService.getOrderInfo(jsonObject1.getString("out_trade_no"));
                                String serviceRange = convertServiceRange(gameUserFrom.getServiceRange());
                                String amount = gameUserFrom.getAmount().split("\\.")[0];
                                String tokenStr = tokenConfig.createToken(gameUserFrom.getUserGameId(),
                                        amount,serviceRange);
                                log.info("手动更新执行,{}单号{}",tokenStr,jsonObject1.getString("out_trade_no"));
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
                                productService.updateOrderSend(jsonObject1.getString("out_trade_no"),sendStatus);
                            }
                        });
                        response.setStatus(200);
                        map.put("code", "SUCCESS");
                        map.put("message", "SUCCESS");
                    } else {
                        log.info("支付宝回调时已经被处理过了");
                        response.setStatus(200);
                        map.put("code", "SUCCESS");
                        map.put("message", "SUCCESS");
                    }

                }  else {
                    response.setStatus(500);
                    map.put("code", "ERROR");
                    map.put("message", "签名错误");
                }



            } else {
                response.setStatus(500);
                map.put("code", "ERROR");
                map.put("message", "签名错误");
            }
            response.setHeader("Content-type", ContentType.JSON.toString());
            response.getOutputStream().write(JSONUtil.toJsonStr(map).getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (Exception e) {
            log.error("系统异常", e);
        }
    }

    @Override
    public String queryOrderStatus(String orderId) {
        try {
            Map<String, String> params = new HashMap<>(16);
            params.put("mchid", mchId);

            log.info("统一下单参数 {}", JSONUtil.toJsonStr(params));
            String osName = System.getProperty("os.name").toLowerCase();
            String path = "";
            if (osName.contains("win")) {
                path = "C:\\mywecat\\1682702563_20240806_cert\\apiclient_key.pem";
            } else {
                path = "/shangyu-application/private-key/apiclient_key.pem";
            }

            IJPayHttpResponse response = WxPayApi.v3(
                    RequestMethodEnum.GET,
                    WxDomainEnum.CHINA.toString(),
                    String.format(BasePayApiEnum.ORDER_QUERY_BY_OUT_TRADE_NO.toString(), orderId),
                    mchId,
                    merchantSerialNumber,
                    null,
                    path,
                    params
            );
            log.info("查询响应 {}", response);
            if (response.getStatus() == HttpServletResponse.SC_OK) {
                // 根据证书序列号查询对应的证书来验证签名结果
                boolean verifySignature = WxPayKit.verifySignature(response,platformCertPath);
                log.info("verifySignature: {}", verifySignature);
                //String search = "{\"amount\":{\"currency\":\"CNY\",\"payer_currency\":\"CNY\",\"payer_total\":1,\"total\":1},\"appid\":\"wxf66bcdbe7d24ba16\",\"attach\":\"\",\"bank_type\":\"OTHERS\",\"mchid\":\"1682702563\",\"out_trade_no\":\"101851162183791001677\",\"payer\":{\"openid\":\"o3PMf67FUymSm2D-r8HqCCSPcalE\"},\"promotion_detail\":[],\"success_time\":\"2024-08-07T10:42:56+08:00\",\"trade_state\":\"SUCCESS\",\"trade_state_desc\":\"支付成功\",\"trade_type\":\"NATIVE\",\"transaction_id\":\"4200002330202408074903140194\"}";
                String search = response.getBody();
                com.alibaba.fastjson.JSONObject jsonObject1 = JSON.parseObject(search);
                log.info("商戶號-->{}",jsonObject1.getString("mchid"));
                log.info("应用id-->{}",jsonObject1.getString("appid"));
                log.info("商户单号-->{}",jsonObject1.getString("out_trade_no"));
                log.info("微信单号-->{}",jsonObject1.getString("transaction_id"));
                log.info("支付状态-->{}",jsonObject1.getString("trade_state"));
                log.info("支付时间-->{}",jsonObject1.getString("success_time"));
                String pay_time = "";
                if (StringUtils.isNotEmpty(jsonObject1.getString("success_time"))) {
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                    try {
                        Date pay_time1 = df.parse("2024-08-07T10:42:56+08:00");
                        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                         pay_time = sdf2.format(pay_time1);
                        log.info("时间-{}",pay_time);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }




                if ("SUCCESS".equals(jsonObject1.getString("trade_state"))) {
                    com.alibaba.fastjson.JSONObject payer1 = jsonObject1.getJSONObject("payer");
                    log.info("支付账号-->{}",payer1.getString("openid"));
                    JSONObject amount1 = jsonObject1.getJSONObject("amount");
                    log.info("支付金额-->{}",amount1.getIntValue("total"));
                    System.out.printf(jsonObject1.getString("trade_state"));
                    int cot = productService.checkOrderSuccess(jsonObject1.getString("out_trade_no"));
                    if (cot == 0) {
                        // 还没异步通知，把订单更新一下
                        // 更新订单未已支付
                        AliPay aliPay = new AliPay();
                        aliPay.setTraceNo(jsonObject1.getString("out_trade_no"));
                        aliPay.setAlipayTraceNo(jsonObject1.getString("transaction_id"));
                        aliPay.setCustomerId(jsonObject1.getString("openid"));
                        aliPay.setPayTime(pay_time);
                        aliPay.setPayAmount(String.valueOf(amount1.getIntValue("total") * 100));
                        aliPay.setOrderStatus(2);
                        productService.updateOrderNotify(aliPay);
                        CompletableFuture.runAsync(()->{
                            String resStr = "";
                            try {
                                // 去游戏库更新游戏币
                                GameUserFrom gameUserFrom = productService.getOrderInfo(jsonObject1.getString("out_trade_no"));
                                String serviceRange = convertServiceRange(gameUserFrom.getServiceRange());
                                String amount = gameUserFrom.getAmount().split("\\.")[0];
                                String tokenStr = tokenConfig.createToken(gameUserFrom.getUserGameId(),
                                        amount,serviceRange);
                                log.info("手动更新执行,{}单号{}",tokenStr,jsonObject1.getString("out_trade_no"));
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
                                productService.updateOrderSend(jsonObject1.getString("out_trade_no"),sendStatus);
                            }
                        });

                    } else {
                        log.info("支付宝查询订单时已经被处理过了");
                    }
                    return "0";
                } else {
                    return "1";
                }
            }
            return JSONUtil.toJsonStr(response);
        } catch (Exception e) {
            log.error("系统异常", e);
            return "1";
        }
    }

    @Override
    public String queryOrderStatusMyData(String orderId) {
        try {
            int cot = productService.checkOrderSuccess(orderId);
            if (cot > 0) {
                return "0";
            } else {
                return "1";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }

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
