package com.daniel.mychickenbreastshop.user.application.port.in;

import com.daniel.mychickenbreastshop.user.model.dto.request.UserSearchDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.user.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.user.domain.enums.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserSearchUseCase {

    DetailResponseDto getUser(Long userId);

    List<ListResponseDto> getAllUsers(Pageable pageable);

    List<ListResponseDto> searchUsers(Pageable pageable, Role role, UserSearchDto userSearchDto);
}
