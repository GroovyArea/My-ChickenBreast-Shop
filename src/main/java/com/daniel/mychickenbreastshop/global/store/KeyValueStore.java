package com.daniel.mychickenbreastshop.global.store;

import java.util.Optional;
import java.util.function.Predicate;

public interface KeyValueStore {

    <T> Optional<T> getValue(String key);

    <T> void save(String key, T value);

    <T> T executeWithLock(String key, Predicate<T> predicate);
}
