package org.example.cache.caches;

import java.util.Optional;

public interface Cache<T> {

    Optional<T> get(String key);

    void put(String key, T value);

    void clear();

    void remove(String key);

}
