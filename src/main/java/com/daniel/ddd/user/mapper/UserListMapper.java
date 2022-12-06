package com.daniel.ddd.user.mapper;


import com.daniel.ddd.global.mapper.GenericDtoMapper;
import com.daniel.ddd.user.application.dto.response.ListResponseDto;
import com.daniel.ddd.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserListMapper extends GenericDtoMapper<ListResponseDto, User> {

    @Override
    @Mapping(target = "userId", source = "id")
    ListResponseDto toDTO(User user);
}
