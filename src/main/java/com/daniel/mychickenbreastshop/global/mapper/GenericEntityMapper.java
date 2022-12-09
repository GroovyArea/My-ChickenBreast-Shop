package com.daniel.mychickenbreastshop.global.mapper;

public interface GenericEntityMapper<D, E> {

    E toEntity(D d);

}
