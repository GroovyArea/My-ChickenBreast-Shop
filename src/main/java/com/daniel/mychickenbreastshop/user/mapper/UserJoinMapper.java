package com.daniel.mychickenbreastshop.user.mapper;

import com.daniel.mychickenbreastshop.global.mapper.GenericEntityMapper;
import com.daniel.mychickenbreastshop.user.model.dto.request.JoinRequestDto;
import com.daniel.mychickenbreastshop.user.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserJoinMapper extends GenericEntityMapper<JoinRequestDto, User> {
}
