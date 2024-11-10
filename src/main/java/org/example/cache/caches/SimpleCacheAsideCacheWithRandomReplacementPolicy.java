package org.example.cache.caches;

import org.example.cache.caches.stats.WithStats;
import org.example.cache.utils.VerifyUtil;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.cache.exceptions.ExceptionMessageConstants.CACHE_DOES_NOT_SUPPORT_NULL_KEYS;

@Component
public class SimpleCacheAsideCacheWithRandomReplacementPolicy<T> implements Cache<T>, WithStats {


    private final Map<String, T> storage = new ConcurrentHashMap<>();
    private int hitCount = 0;
    private int missCount = 0;
    private int evictionCount = 0;

    @Override
    public Optional<T> get(String email) {
        VerifyUtil.verify(email != null, CACHE_DOES_NOT_SUPPORT_NULL_KEYS);
        Optional<T> resOpt = Optional.ofNullable(storage.get(email));
        if (resOpt.isPresent()) {
            hitCount++;
        } else {
            missCount++;
        }
        return resOpt;
    }

    @Override
    public void put(String key, T value) {
        VerifyUtil.verify(key != null, CACHE_DOES_NOT_SUPPORT_NULL_KEYS);
        if (getSize() == CAPACITY) {
            Optional<String> keyOpt = storage.keySet().stream().findAny();
            keyOpt.ifPresent(this::remove);
        }
        storage.put(key, value);
    }

    @Override
    public void clear() {
        int sizeBeforeClear = storage.size();
        evictionCount += sizeBeforeClear;
        storage.clear();
    }

    @Override
    public void remove(String key) {
        VerifyUtil.verify(key != null, CACHE_DOES_NOT_SUPPORT_NULL_KEYS);
        T removed = storage.remove(key);
        if (removed != null) {
            evictionCount++;
        }
    }

    @Override
    public int getSize() {
        return storage.size();
    }

    @Override
    public int hitCount() {
        return hitCount;
    }

    @Override
    public int missCount() {
        return missCount;
    }

    @Override
    public int evictionCount() {
        return evictionCount;
    }

    @Override
    public double hitRate() {
        return (double) hitCount / (hitCount + missCount);
    }

    @Override
    public int capacity() {
        return getCapacity();
    }

    @Override
    public int size() {
        return storage.size();
    }

    @Override
    public void resetStats() {
        this.hitCount = 0;
        this.missCount = 0;
        this.evictionCount = 0;
    }

}
