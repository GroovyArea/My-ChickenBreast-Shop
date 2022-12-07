package com.daniel.ddd.global.mapper;

public interface GenericEntityMapper<D, E> {

    E toEntity(D d);

}
