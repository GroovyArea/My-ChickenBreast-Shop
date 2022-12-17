package com.daniel.mychickenbreastshop.user.mapper;


import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import com.daniel.mychickenbreastshop.user.model.dto.response.ListResponseDto;
import com.daniel.mychickenbreastshop.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserListMapper extends GenericDtoMapper<ListResponseDto, User> {

    @Override
    @Mapping(target = "userId", source = "id")
    ListResponseDto toDTO(User user);
}
