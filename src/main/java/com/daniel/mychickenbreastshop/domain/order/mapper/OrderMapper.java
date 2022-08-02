package com.daniel.mychickenbreastshop.domain.order.mapper;

import com.daniel.mychickenbreastshop.domain.order.domain.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {

    List<OrderVO> selectOrderList(@Param("userId")String userId, @Param("orderStatus") String orderStatus);

    void insertOrder(OrderVO orderVO);

    void updateOrder(String tid);
}
