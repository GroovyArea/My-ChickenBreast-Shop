package com.daniel.mychickenbreastshop.domain.user.mapper;

import com.daniel.mychickenbreastshop.domain.user.domain.UserVO;
import com.daniel.mychickenbreastshop.global.util.Pager;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserMapper {

    UserVO selectUser(String userId);

    List<UserVO> selectUserList(String searchKeyword, String searchValue, Pager pager);

    void insertUser(UserVO userVO);

    void updateUser(UserVO userVO);

    void changeGradeUser(Map<String, Object> map);

}
