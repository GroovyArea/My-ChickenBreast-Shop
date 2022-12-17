package com.daniel.mychickenbreastshop.user.auth.security.mapper;

import com.daniel.mychickenbreastshop.global.mapper.GenericDtoMapper;
import com.daniel.mychickenbreastshop.user.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.user.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrincipalDetailMapper extends GenericDtoMapper<PrincipalDetails, User> {
}
