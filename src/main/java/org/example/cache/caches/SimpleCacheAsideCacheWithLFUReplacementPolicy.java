package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@Qualifier("LFUReplacement")
@Scope("prototype")
public class SimpleCacheAsideCacheWithLFUReplacementPolicy extends SimpleCacheAsideCacheWithRandomReplacementPolicy {

    private Map<String, Integer> accessCounter = new HashMap<>();

    @Override
    public void put(String key, Customer value) {
        super.put(key, value);
        accessCounter.put(key, 0);
    }

    @Override
    public Optional<Customer> get(String email) {
        Optional<Customer> customerOpt = super.get(email);
        if (customerOpt.isPresent()) {
            Integer i = accessCounter.get(email);
            if (i == null) {
                accessCounter.put(email, 1);
            } else {
                accessCounter.put(email, ++i);
            }
        }
        return customerOpt;
    }


    @Override
    protected void evict() {
        Optional<String> keyToRemoveOpt = accessCounter
            .entrySet()
            .stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey);
        if (keyToRemoveOpt.isPresent()) {
            remove(keyToRemoveOpt.get());
            accessCounter.remove(keyToRemoveOpt.get());
        }
    }

}
