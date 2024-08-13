package com.example.wangjian.syslog1.mapper;

import com.example.wangjian.syslog1.entity.AliPay;
import com.example.wangjian.syslog1.entity.GameUserFrom;
import com.example.wangjian.syslog1.entity.OrderFrom;
import com.example.wangjian.syslog1.entity.ProductFrom;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {

    /**
     * 查询商品
     * @param id
     * @return
     */
    ProductFrom queryProductInfos(String id);

    /**
     * 入库订单
     * @param pay
     */
    void insetAlipaymentOrderInfo(AliPay pay);

    /**
     * 跟新订单
     * @param aliPay
     */
    void updateOrderNotify(AliPay aliPay);

    int checkOrderSuccess(String out_trade_no);

    /**
     * 推送游戏单号
     * @param out_trade_no
     * @return
     */
    GameUserFrom getOrderInfo(String out_trade_no);

    void updateOrderSend(@Param("outTradeNo") String outTradeNo, @Param("sendStatus")int sendStatus);
}
