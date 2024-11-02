package org.example.cache.caches;

import java.util.Optional;

public interface Cache<T> {

    final int CAPACITY = 20;

    Optional<T> get(String key);

    void put(T value);

    void clear();

    void remove(String key);

    default int getCapacity() {
        return CAPACITY;
    }

    int getSize();

}
