package com.example.wangjian.syslog1.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class JsonTest {

    public static void main(String ss[]) {
//        String notice = "{\"mchid\":\"1682702563\",\"appid\":\"wxf66bcdbe7d24ba16\",\"out_trade_no\":\"101851162183791001677\",\"transaction_id\":\"4200002330202408074903140194\",\"trade_type\":\"NATIVE\",\"trade_state\":\"SUCCESS\",\"trade_state_desc\":\"支付成功\",\"bank_type\":\"OTHERS\",\"attach\":\"\",\"success_time\":\"2024-08-07T10:42:56+08:00\"," +
//                "\"payer\":{\"openid\":\"o3PMf67FUymSm2D-r8HqCCSPcalE\"},\"amount\":{\"total\":1,\"payer_total\":1,\"currency\":\"CNY\",\"payer_currency\":\"CNY\"}}";
//        JSONObject jsonObject = JSON.parseObject(notice);
//        log.info("商戶號-->{}",jsonObject.getString("mchid"));
//        log.info("应用id-->{}",jsonObject.getString("appid"));
//        log.info("商户单号-->{}",jsonObject.getString("out_trade_no"));
//        log.info("微信单号-->{}",jsonObject.getString("transaction_id"));
//        log.info("支付状态-->{}",jsonObject.getString("trade_state"));
//        log.info("支付时间-->{}",jsonObject.getString("success_time"));
//        JSONObject payer = jsonObject.getJSONObject("payer");
//        log.info("支付账号-->{}",payer.getString("openid"));
//        JSONObject amount = jsonObject.getJSONObject("amount");
//        log.info("支付金额-->{}",amount.getIntValue("total"));
//
//        log.info("==============================================查询的");
//        String search = "{\"amount\":{\"currency\":\"CNY\",\"payer_currency\":\"CNY\",\"payer_total\":1,\"total\":1},\"appid\":\"wxf66bcdbe7d24ba16\",\"attach\":\"\",\"bank_type\":\"OTHERS\",\"mchid\":\"1682702563\",\"out_trade_no\":\"101851162183791001677\",\"payer\":{\"openid\":\"o3PMf67FUymSm2D-r8HqCCSPcalE\"},\"promotion_detail\":[],\"success_time\":\"2024-08-07T10:42:56+08:00\",\"trade_state\":\"SUCCESS\",\"trade_state_desc\":\"支付成功\",\"trade_type\":\"NATIVE\",\"transaction_id\":\"4200002330202408074903140194\"}";
//        JSONObject jsonObject1 = JSON.parseObject(search);
//
//        log.info("商戶號-->{}",jsonObject1.getString("mchid"));
//        log.info("应用id-->{}",jsonObject1.getString("appid"));
//        log.info("商户单号-->{}",jsonObject1.getString("out_trade_no"));
//        log.info("微信单号-->{}",jsonObject1.getString("transaction_id"));
//        log.info("支付状态-->{}",jsonObject1.getString("trade_state"));
//        log.info("支付时间-->{}",jsonObject1.getString("success_time"));
//        JSONObject payer1 = jsonObject1.getJSONObject("payer");
//        log.info("支付账号-->{}",payer1.getString("openid"));
//        JSONObject amount1 = jsonObject1.getJSONObject("amount");
//        log.info("支付金额-->{}",amount1.getIntValue("total"));

//        String pay_time = "";
//        if (StringUtils.isNotEmpty("2024-08-07T10:42:56+08:00")) {
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
//            try {
//                Date pay_time1 = df.parse("2024-08-07T10:42:56+08:00");
//
//                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String dateString2 = sdf2.format(pay_time1);
//                log.info("时间-{}",dateString2);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


    }


}
