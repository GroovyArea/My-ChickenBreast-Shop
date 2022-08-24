package com.daniel.mychickenbreastshop.domain.user.mapper.struct;

import com.daniel.mychickenbreastshop.domain.user.domain.User;
import com.daniel.mychickenbreastshop.domain.user.domain.dto.response.DetailResponseDto;
import com.daniel.mychickenbreastshop.global.mapper.GenericMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DetailObjectMapper extends GenericMapper<DetailResponseDto, User> {
}
