package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier("MRUReplacement")
@Scope("prototype")
public class SimpleCacheAsideCacheWithMRUReplacementPolicy extends SimpleCacheAsideCacheWithRandomReplacementPolicy {

    private List<String> accessOrder = new LinkedList<>();

    @Override
    public void put(String key, Customer value) {
        super.put(key, value);
        if (accessOrder.contains(key)) {
            accessOrder.remove(key);
            accessOrder.addLast(key);
        } else {
            accessOrder.addLast(key);
        }
    }

    @Override
    public Optional<Customer> get(String email) {
        Optional<Customer> customerOpt = super.get(email);
        if (customerOpt.isPresent()) {
            accessOrder.remove(email);
            accessOrder.addLast(email);
        }
        return customerOpt;
    }


    @Override
    protected void evict() {
        String last = accessOrder.getLast();
        accessOrder.remove(last);
        super.remove(last);
    }

}
