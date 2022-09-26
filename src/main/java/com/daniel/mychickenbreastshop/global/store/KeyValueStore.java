package com.daniel.mychickenbreastshop.global.store;

import java.util.Optional;
import java.util.function.Supplier;

public interface KeyValueStore {

    <T> Optional<Object> getValue(String key);

    <T> void save(String key, T value);

    <T> T executeWithLock(String key, Supplier<T> supplier) throws InterruptedException;
}
