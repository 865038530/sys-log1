package com.example.wangjian.syslog1.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.wangjian.syslog1.entity.ProductFrom;
import com.example.wangjian.syslog1.service.ProductService;
import com.example.wangjian.syslog1.util.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller

public class JspController {

    @Autowired
    private ProductService productService;


    @GetMapping("/index")
    public String index(HttpSession httpSession) {
        httpSession.setAttribute("userGame", "userGame");
        return "index"; // 返回 JSP 页面的名称，不包括后缀
    }

    @GetMapping("/gotoSuccess")
    public String gotoSuccess(HttpSession httpSession) {
        return "alipaySucess"; // 返回 JSP 页面的名称，不包括后缀
    }

    @GetMapping("/gotoWxSuccess")
    public String gotoWxSuccess(HttpSession httpSession) {
        return "wxPaySucess"; // 返回 JSP 页面的名称，不包括后缀
    }

    @GetMapping("/gotoConfirm")
    public String gotoConfirm(HttpSession httpSession,
                              @RequestParam(value="ids",required=true)String ids,
                              @RequestParam(value="quantity",required=true)int quantity,
                              @RequestParam(value="phone",required=true)String phone,
                              @RequestParam(value="userGame",required=true)String userGame,
                              @RequestParam(value="serviceRange",required=true)String serviceRange

    ) throws Exception {

        ProductFrom productFrom = productService.checkProduct(ids);
        if (productFrom == null) {
            throw new Exception("商品信息不存在!");
        }
        String orderId = IdUtils.getQuantityOrderSerialNo(1)[0];
        httpSession.setAttribute("ids", ids);
        httpSession.setAttribute("quantity", quantity);
        httpSession.setAttribute("productName", productFrom.getName());
        httpSession.setAttribute("amount", productFrom.getAmount() * quantity);
        httpSession.setAttribute("phone", phone);
        httpSession.setAttribute("userGame", userGame);
        httpSession.setAttribute("orderId", orderId);
        httpSession.setAttribute("serviceRange", serviceRange);
        return "submitOrder"; // 返回 JSP 页面的名称，不包括后缀
    }

    @GetMapping("/gotoWxPayConfirm")
    public String gotoWxPayConfirm(HttpSession httpSession,
                              @RequestParam(value="ids",required=true)String ids,
                              @RequestParam(value="quantity",required=true)int quantity,
                              @RequestParam(value="phone",required=true)String phone,
                              @RequestParam(value="userGame",required=true)String userGame,
                              @RequestParam(value="serviceRange",required=true)String serviceRange

    ) throws Exception {

        ProductFrom productFrom = productService.checkProduct(ids);
        if (productFrom == null) {
            throw new Exception("商品信息不存在!");
        }
        String orderId = IdUtils.getQuantityOrderSerialNo(1)[0];
        httpSession.setAttribute("ids", ids);
        httpSession.setAttribute("quantity", quantity);
        httpSession.setAttribute("productName", productFrom.getName());
        httpSession.setAttribute("amount", productFrom.getAmount() * quantity);
        httpSession.setAttribute("phone", phone);
        httpSession.setAttribute("userGame", userGame);
        httpSession.setAttribute("orderId", orderId);
        httpSession.setAttribute("serviceRange", serviceRange);
        return "submitWxPayOrder"; // 返回 JSP 页面的名称，不包括后缀
    }


//    public static void main(String ars[]) {
//        JSONObject jsonObject = new JSONObject();
//
//        //JSONObject jsonObject1 = JSON.parseObject(myStr);
//        //JSONArray jsonArray = JSON.parseArray(str);
//        JSONObject jsonObject1 = JSON.parseObject("{\"alipay_trade_query_response\":{\"code\":\"10000\",\"msg\":\"Success\",\"buyer_logon_id\":\"nqf***@sandbox.com\",\"buyer_pay_amount\":\"0.00\",\"buyer_user_id\":\"2088722040081152\",\"buyer_user_type\":\"PRIVATE\",\"invoice_amount\":\"0.00\",\"out_trade_no\":\"101487989628076851270\",\"point_amount\":\"0.00\",\"receipt_amount\":\"0.00\",\"send_pay_date\":\"2024-07-28 10:11:49\",\"total_amount\":\"6.00\",\"trade_no\":\"2024072822001481150503345286\",\"trade_status\":\"TRADE_SUCCESS\"},\"sign\":\"HMCDSZBCzkD8vUhidBc+3+3ORz54/Sl8vvff11F1XeqdefhlNJJjC+PX2Mm6Ruc2PXDmzoDaVutDNorToHQVMVK3DaEiAVdqWBADqPBtIDZVP0iEje1JWka2hWtVzPjb1VS5DOQH4wzb1ec6ZhWuiDsOkuYdx3ulJrAfCCRHTpFOJjngu+zbtCROocbbvaTtFZmCQPVjnpNVZdp7PSy/mefhKIzE3kxugsVUm4c/XnMG6/XwFwNTDyyr1P7kmhmhPj/tTCg9WxfTBUalm/XpcApgsr0H/K6uBuq7SeW4moR6BM88eBOoVLI9nk810s8rsG+gJXHcLvtGWVs75YWmKg==\"}");
//        String ss = jsonObject1.getString("alipay_trade_query_response");
//        JSONObject res = JSON.parseObject(ss);
//        System.out.printf(res.get("trade_status").toString());
//    }


}
