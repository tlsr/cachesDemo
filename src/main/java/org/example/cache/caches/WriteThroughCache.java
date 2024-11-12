package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("WriteThrough")
public class WriteThroughCache extends SimpleCacheAsideCacheWithRandomReplacementPolicy {

    private final CustomerRepository repo;

    public WriteThroughCache(CustomerRepository repo) {
        this.repo = repo;
    }

    @Override
    public void put(String key, Customer value) {
        super.put(key, value);
        repo.save(value);
    }

}
