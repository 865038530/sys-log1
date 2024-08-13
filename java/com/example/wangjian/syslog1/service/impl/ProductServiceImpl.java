package com.example.wangjian.syslog1.service.impl;

import com.example.wangjian.syslog1.entity.AliPay;
import com.example.wangjian.syslog1.entity.GameUserFrom;
import com.example.wangjian.syslog1.entity.ProductFrom;
import com.example.wangjian.syslog1.mapper.ProductMapper;
import com.example.wangjian.syslog1.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductMapper productMapper;


    @Override
    public ProductFrom checkProduct(String id) {

        return productMapper.queryProductInfos(id);

    }

    @Override
    public void insetAlipaymentOrderInfo(AliPay pay) {
        productMapper.insetAlipaymentOrderInfo(pay);
    }

    @Override
    public void updateOrderNotify(AliPay aliPay) {
        productMapper.updateOrderNotify(aliPay);
    }

    @Override
    public int checkOrderSuccess(String out_trade_no) {
        return productMapper.checkOrderSuccess(out_trade_no);
    }

    @Override
    public GameUserFrom getOrderInfo(String out_trade_no) {
        return productMapper.getOrderInfo(out_trade_no);
    }

    @Override
    public void updateOrderSend(String out_trade_no, int sendStatus) {
        productMapper.updateOrderSend(out_trade_no,sendStatus);
    }
}
