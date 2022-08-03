package com.daniel.mychickenbreastshop.domain.user.mapper.struct;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface GenericMapper<D, V> {

    D toDTO(V v);

    V toVO(D d);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromVO(D dto, @MappingTarget V vo);
}
