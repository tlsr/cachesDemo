package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("WriteBack")
public class WriteBackCache extends SimpleCacheAsideCacheWithRandomReplacementPolicy {

    public static final int THRESHOLD = 10;

    private final CustomerRepository repo;

    public WriteBackCache(CustomerRepository repo) {
        this.repo = repo;
    }

    @Override
    public void put(String key, Customer value) {
        super.put(key, value);
        if (storage.size() >= THRESHOLD) {
            repo.saveAll(storage.values());
        }
    }

}
