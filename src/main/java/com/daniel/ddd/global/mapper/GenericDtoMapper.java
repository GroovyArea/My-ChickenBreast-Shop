package com.daniel.ddd.global.mapper;

public interface GenericDtoMapper<D, E> {

    D toDTO(E e);

}
