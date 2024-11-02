package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.example.cache.utils.VerifyUtil;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static org.example.cache.exceptions.ExceptionMessageConstants.CACHE_DOES_NOT_SUPPORT_NULL_KEYS;

public class SimpleCacheAsideCache implements Cache<Customer> {


    private final Map<String, Customer> storage = new ConcurrentHashMap<>();

    @Override
    public Optional<Customer> get(String email) {
        VerifyUtil.verify(email != null, CACHE_DOES_NOT_SUPPORT_NULL_KEYS);
        return Optional.ofNullable(storage.get(email));
    }

    @Override
    public void put(Customer value) {
        VerifyUtil.verify(value.getEmail() != null, CACHE_DOES_NOT_SUPPORT_NULL_KEYS);
        if (getSize() == CAPACITY) {
            Optional<String> keyOpt = storage.keySet().stream().findAny();

            keyOpt.ifPresent(this::remove);
        }
        storage.put(value.getEmail(), value);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    public void remove(String key) {
        VerifyUtil.verify(key != null, CACHE_DOES_NOT_SUPPORT_NULL_KEYS);
        storage.remove(key);
    }

    @Override
    public int getSize() {
        return storage.size();
    }

}
