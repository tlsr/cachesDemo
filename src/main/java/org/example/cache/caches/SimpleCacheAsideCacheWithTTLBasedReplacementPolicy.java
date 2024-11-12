package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Qualifier("TTLReplacement")
@Scope("prototype")
public class SimpleCacheAsideCacheWithTTLBasedReplacementPolicy extends SimpleCacheAsideCacheWithRandomReplacementPolicy {

    private static long TTL_MILLS = 20000;
    private Map<String, Long> idsToTimeStampAdded = new ConcurrentHashMap<>();

    @Override
    public void put(String key, Customer value) {
        super.put(key, value);
        idsToTimeStampAdded.put(key, System.currentTimeMillis());
    }

    @Override
    public Optional<Customer> get(String email) {
        evict();
        return super.get(email);
    }

    @Override
    protected void evict() {
        List<String> toDelete = idsToTimeStampAdded
            .entrySet()
            .stream()
            .filter(entry -> System.currentTimeMillis() - entry.getValue() > TTL_MILLS)
            .map(Map.Entry::getKey)
            .toList();
        if (toDelete.isEmpty() && storage.size() >= CAPACITY) {
            super.evict();
        } else {
            toDelete.forEach(id -> {
                idsToTimeStampAdded.remove(id);
                remove(id);
            });
        }
    }

}
