package com.daniel.mychickenbreastshop.user.application.service;

import com.daniel.mychickenbreastshop.global.error.exception.BadRequestException;
import com.daniel.mychickenbreastshop.user.adaptor.out.persistence.UserRepository;
import com.daniel.mychickenbreastshop.user.application.port.in.SearchUseCase;
import com.daniel.mychickenbreastshop.user.domain.User;
import com.daniel.mychickenbreastshop.user.domain.enums.ErrorMessages;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import com.daniel.mychickenbreastshop.user.mapper.UserDetailMapper;
import com.daniel.mychickenbreastshop.user.mapper.UserListMapper;
import com.daniel.mychickenbreastshop.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.ListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService implements SearchUseCase {

    private final UserRepository userRepository;
    private final UserDetailMapper userDetailMapper;
    private final UserListMapper userListMapper;

    @Override
    public DetailResponseDto getUser(Long userId) {
        return userDetailMapper.toDTO(userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.USER_NOT_EXISTS.getMessage())));
    }

    @Override
    public List<ListResponseDto> getAllUsers(Pageable pageable) {
        List<User> users = userRepository.findAll(pageable).getContent();

        return users.stream()
                .map(userListMapper::toDTO)
                .toList();
    }

    @Override
    public List<ListResponseDto> searchUsers(Pageable pageable, Role role, UserSearchDto userSearchDto) {
        List<User> searchedUsers = userRepository.findUserWithDynamicQuery(pageable, userSearchDto, role).getContent();

        return searchedUsers.stream()
                .map(userListMapper::toDTO)
                .toList();    }
}
