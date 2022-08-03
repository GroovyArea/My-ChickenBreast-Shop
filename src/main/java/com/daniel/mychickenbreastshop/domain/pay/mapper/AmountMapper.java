package com.daniel.mychickenbreastshop.domain.pay.mapper;

import com.daniel.mychickenbreastshop.domain.pay.domain.AmountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AmountMapper {

    AmountVO selectAmount(String tid);

    void insertAmount(@Param("amountVO") AmountVO amountVO, @Param("tId") String tId);
}
