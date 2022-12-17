package com.daniel.mychickenbreastshop.global.mapper;

public interface GenericDtoMapper<D, E> {

    D toDTO(E e);

}
