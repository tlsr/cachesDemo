package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.Queue;

@Component
@Qualifier("FIFOReplacement")
@Scope("prototype")
public class SimpleCacheAsideCacheWithFIFOReplacementPolicy extends SimpleCacheAsideCacheWithRandomReplacementPolicy {
    private Queue<String> insertOder = new LinkedList<>();

    @Override
    public void put(String key, Customer value) {
        super.put(key, value);
        if (insertOder.contains(key)) {
            insertOder.remove(key);
            insertOder.add(key);
        } else {
            insertOder.add(key);
        }
    }

    @Override
    protected void evict() {
        String poll = insertOder.poll();
        if (poll != null) {
            super.remove(poll);
        } else {
            super.evict();
        }
    }

}
