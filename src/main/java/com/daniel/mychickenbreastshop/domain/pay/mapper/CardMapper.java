package com.daniel.mychickenbreastshop.domain.pay.mapper;

import com.daniel.mychickenbreastshop.domain.pay.domain.CardVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CardMapper {

    CardVO selectCard(String tid);

    void insertCard(@Param("CardVO") CardVO cardVO, @Param("tId") String tId);
}
