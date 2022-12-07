package com.daniel.ddd.user.application.service;

import com.daniel.ddd.global.error.exception.BadRequestException;
import com.daniel.ddd.user.adaptor.out.persistence.UserRepository;
import com.daniel.ddd.user.model.dto.request.UserSearchDto;
import com.daniel.ddd.user.model.dto.response.DetailResponseDto;
import com.daniel.ddd.user.model.dto.response.ListResponseDto;
import com.daniel.ddd.user.application.port.in.SearchUseCase;
import com.daniel.ddd.user.domain.enums.ErrorMessages;
import com.daniel.ddd.user.domain.enums.Role;
import com.daniel.ddd.user.mapper.UserDetailMapper;
import com.daniel.ddd.user.mapper.UserListMapper;
import com.daniel.mychickenbreastshop.domain.user.model.User;
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
