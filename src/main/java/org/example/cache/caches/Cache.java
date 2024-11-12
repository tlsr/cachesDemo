package org.example.cache.caches;

import java.util.Optional;

public interface Cache<T> {

    int DEFAULT_CAPACITY = 20;

    Optional<T> get(String key);

    void put(String key, T value);

    void clear();

    void remove(String key);

    default int getCapacity() {
        return DEFAULT_CAPACITY;
    }

    int getSize();

    void ensureCapacity(int minCapacity);

}
