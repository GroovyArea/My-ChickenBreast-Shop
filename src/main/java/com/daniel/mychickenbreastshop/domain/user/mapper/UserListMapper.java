package com.daniel.mychickenbreastshop.domain.user.mapper;

import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.domain.user.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserListMapper extends GenericDtoMapper<ListResponseDto, User> {

    @Override
    @Mapping(target = "userId", source = "id")
    ListResponseDto toDTO(User user);
}
