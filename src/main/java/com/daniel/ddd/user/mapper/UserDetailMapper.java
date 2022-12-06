package com.daniel.ddd.user.mapper;


import com.daniel.ddd.global.mapper.GenericDtoMapper;
import com.daniel.ddd.user.application.dto.response.DetailResponseDto;
import com.daniel.ddd.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDetailMapper extends GenericDtoMapper<DetailResponseDto, User> {

    @Mapping(target = "userId", source = "id")
    @Override
    DetailResponseDto toDTO(User user);
}
