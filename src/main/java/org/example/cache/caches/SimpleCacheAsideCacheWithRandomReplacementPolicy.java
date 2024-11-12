package org.example.cache.caches;

import org.example.cache.caches.stats.CacheStats;
import org.example.cache.caches.stats.WithStats;
import org.example.cache.entities.Customer;
import org.example.cache.utils.VerifyUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.cache.exceptions.ExceptionMessageConstants.CACHE_DOES_NOT_SUPPORT_NULL_KEYS;

@Component
@Qualifier("RandomReplacement")
@Scope("prototype")
public class SimpleCacheAsideCacheWithRandomReplacementPolicy implements Cache<Customer>, WithStats {


    protected final Map<String, Customer> storage = new ConcurrentHashMap<>();
    private CacheStats cacheStats = new CacheStats(CAPACITY);

    @Override
    public Optional<Customer> get(String email) {
        VerifyUtil.verify(email != null, CACHE_DOES_NOT_SUPPORT_NULL_KEYS);
        Optional<Customer> resOpt = Optional.ofNullable(storage.get(email));
        if (resOpt.isPresent()) {
            cacheStats.incrementHits();
        } else {
            cacheStats.incrementMisses();
        }
        return resOpt;
    }

    @Override
    public void put(String key, Customer value) {
        VerifyUtil.verify(key != null, CACHE_DOES_NOT_SUPPORT_NULL_KEYS);
        if (storage.size() >= CAPACITY) {
            evict();
        }
        cacheStats.incrementSize();
        storage.put(key, value);
    }

    protected void evict() {
        Optional<String> keyOpt = storage.keySet().stream().findAny();
        keyOpt.ifPresent(this::remove);
    }

    @Override
    public void clear() {
        int sizeBeforeClear = storage.size();
        for (int i = 0; i < sizeBeforeClear; i++) {
            cacheStats.incrementEvictions();
            cacheStats.decrementSize();
        }
        storage.clear();
    }

    @Override
    public void remove(String key) {
        VerifyUtil.verify(key != null, CACHE_DOES_NOT_SUPPORT_NULL_KEYS);
        Customer removed = storage.remove(key);
        if (removed != null) {
            cacheStats.decrementSize();
            cacheStats.incrementEvictions();
        }
    }

    @Override
    public int getSize() {
        assert cacheStats.size() == storage.size();
        return cacheStats.size();
    }

    @Override
    public CacheStats getCacheStats() {
        return this.cacheStats;
    }

    @Override
    public void resetStats() {
        this.cacheStats = new CacheStats(CAPACITY);
    }
}
