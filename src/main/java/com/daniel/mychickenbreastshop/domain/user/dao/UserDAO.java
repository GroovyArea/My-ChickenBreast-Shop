package com.daniel.mychickenbreastshop.domain.user.dao;

import com.daniel.mychickenbreastshop.domain.user.domain.UserVO;
import com.daniel.mychickenbreastshop.domain.user.mapper.sql.UserMapper;
import com.daniel.mychickenbreastshop.global.util.Pager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * User Repository
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.03 최초작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@Repository
@RequiredArgsConstructor
public class UserDAO {

    private final UserMapper userMapper;

    public UserVO selectUser(String userId) {
        return userMapper.selectUser(userId);
    }

    public List<UserVO> selectUserList(String searchKeyword, String searchValue, Pager pager) {
        return userMapper.selectUserList(searchKeyword, searchValue, pager);
    }

    public void insertUser(UserVO userVO) {
        userMapper.insertUser(userVO);
    }

    public void updateUser(UserVO userVO){
        userMapper.updateUser(userVO);
    }

    public void changeGradeUser(Map<String, Object> map){
        userMapper.changeGradeUser(map);
    }
}
