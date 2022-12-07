package com.daniel.ddd.user.mapper;

import com.daniel.ddd.global.mapper.GenericEntityMapper;
import com.daniel.ddd.user.model.dto.request.JoinRequestDto;
import com.daniel.ddd.user.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserJoinMapper extends GenericEntityMapper<JoinRequestDto, User> {
}
