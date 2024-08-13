package com.example.wangjian.syslog1.mapper;

import com.example.wangjian.syslog1.entity.OrderFrom;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {

    OrderFrom getOrder();
}
