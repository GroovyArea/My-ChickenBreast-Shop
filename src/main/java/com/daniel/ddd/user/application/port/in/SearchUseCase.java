package com.daniel.ddd.user.application.port.in;

import com.daniel.ddd.user.application.dto.request.UserSearchDto;
import com.daniel.ddd.user.application.dto.response.DetailResponseDto;
import com.daniel.ddd.user.application.dto.response.ListResponseDto;
import com.daniel.ddd.user.domain.enums.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SearchUseCase {

    DetailResponseDto getUser(Long userId);

    List<ListResponseDto> getAllUsers(Pageable pageable);

    List<ListResponseDto> searchUsers(Pageable pageable, Role role, UserSearchDto userSearchDto);
}
