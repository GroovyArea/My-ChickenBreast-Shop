package com.daniel.mychickenbreastshop.auth.security.mapper;

import com.daniel.mychickenbreastshop.auth.security.model.PrincipalDetails;
import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.global.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PrincipalDetailMapper extends GenericMapper<PrincipalDetails, User> {
}