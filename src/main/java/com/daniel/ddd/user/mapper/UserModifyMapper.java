package com.daniel.ddd.user.mapper;


import com.daniel.ddd.global.mapper.GenericEntityMapper;
import com.daniel.ddd.user.application.dto.request.ModifyRequestDto;
import com.daniel.ddd.user.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserModifyMapper extends GenericEntityMapper<ModifyRequestDto, User> {
}
