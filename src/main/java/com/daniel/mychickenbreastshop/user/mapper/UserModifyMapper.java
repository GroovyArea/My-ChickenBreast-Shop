package com.daniel.mychickenbreastshop.user.mapper;


import com.daniel.mychickenbreastshop.global.mapper.GenericEntityMapper;
import com.daniel.mychickenbreastshop.user.model.dto.request.ModifyRequestDto;
import com.daniel.mychickenbreastshop.user.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserModifyMapper extends GenericEntityMapper<ModifyRequestDto, User> {
}
