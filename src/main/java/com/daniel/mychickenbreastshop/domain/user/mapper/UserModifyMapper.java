package com.daniel.mychickenbreastshop.domain.user.mapper;

import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericEntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserModifyMapper extends GenericEntityMapper<ModifyRequestDto, User> {
}