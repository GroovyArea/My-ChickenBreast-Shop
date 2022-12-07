package com.daniel.ddd.user.auth.security.mapper;

import com.daniel.ddd.user.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.user.model.User;
import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrincipalDetailMapper extends GenericDtoMapper<PrincipalDetails, User> {
}