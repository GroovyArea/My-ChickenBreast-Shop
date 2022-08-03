package com.daniel.mychickenbreastshop.domain.user.service;

import com.daniel.mychickenbreastshop.domain.user.dao.UserDAO;
import com.daniel.mychickenbreastshop.domain.user.dto.UserDTO;
import com.daniel.mychickenbreastshop.domain.user.dto.UserJoinDTO;
import com.daniel.mychickenbreastshop.domain.user.error.exception.UserExistException;
import com.daniel.mychickenbreastshop.domain.user.mapper.struct.JoinObjectMapper;
import com.daniel.mychickenbreastshop.domain.user.mapper.struct.UserObjectMapper;
import com.daniel.mychickenbreastshop.global.util.PasswordEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;

/**
 * User Sevice 클래스
 *
 * <pre>
 *     <b>history</b>
 *     1.0, 2022.08.03 최초 작성
 * </pre>
 *
 * @author 김남영
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDAO userDAO;
    private final JoinObjectMapper joinObjectMapper;
    private final UserObjectMapper userObjectMapper;

    @Transactional(readOnly = true)
    public UserDTO findById(String userId) {
        return userObjectMapper.toDTO(userDAO.selectUser(userId));
    }

    public void addUser(UserJoinDTO userJoinDTO) {
        if (userDAO.selectUser(userJoinDTO.getUserId()) != null) {
            throw new UserExistException();
        }

        String salt = PasswordEncrypt.getSalt();
        userJoinDTO.setUserSalt(salt);

        try {
            userJoinDTO.setUserPw(PasswordEncrypt.getSecurePassword(userJoinDTO.getUserPw(), salt));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e);
        }

        userDAO.insertUser(joinObjectMapper.toVO(userJoinDTO));
    }


}
