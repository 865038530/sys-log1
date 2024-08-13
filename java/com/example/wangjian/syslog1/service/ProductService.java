package com.example.wangjian.syslog1.service;

import com.example.wangjian.syslog1.entity.AliPay;
import com.example.wangjian.syslog1.entity.GameUserFrom;
import com.example.wangjian.syslog1.entity.OrderFrom;
import com.example.wangjian.syslog1.entity.ProductFrom;

public interface ProductService {
    /**
     * 查询商品
     * @param id
     * @return
     */
    ProductFrom checkProduct(String id);

    /**
     * 生成订单
     * @param pay
     */
    void insetAlipaymentOrderInfo(AliPay pay);

    /**
     * 更新订单
     * @param aliPay
     */
    void updateOrderNotify(AliPay aliPay);

    /**
     *
     * @param out_trade_no
     */
    int checkOrderSuccess(String out_trade_no);

    /**
     *
     * @param out_trade_no
     * @return
     */
    GameUserFrom getOrderInfo(String out_trade_no);

    void updateOrderSend(String out_trade_no, int sendStatus);
}
