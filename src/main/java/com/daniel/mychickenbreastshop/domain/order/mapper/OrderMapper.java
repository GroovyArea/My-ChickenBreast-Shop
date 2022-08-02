package com.daniel.mychickenbreastshop.domain.order.mapper;

import com.daniel.mychickenbreastshop.domain.order.domain.OrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderVO> selectOrderList(String userId);

    void insertOrder(OrderVO orderVO);

    void updateOrder(String tid);
}
