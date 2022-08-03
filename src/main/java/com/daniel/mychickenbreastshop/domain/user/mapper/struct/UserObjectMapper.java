package com.daniel.mychickenbreastshop.domain.user.mapper.struct;

import com.daniel.mychickenbreastshop.domain.user.domain.UserVO;
import com.daniel.mychickenbreastshop.domain.user.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserObjectMapper extends GenericMapper<UserDTO, UserVO> {
}
