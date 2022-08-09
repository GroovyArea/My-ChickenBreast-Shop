package com.daniel.mychickenbreastshop.domain.user.mapper.struct;

import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.dto.UserJoinDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface JoinObjectMapper extends GenericMapper<UserJoinDTO, User> {

}
