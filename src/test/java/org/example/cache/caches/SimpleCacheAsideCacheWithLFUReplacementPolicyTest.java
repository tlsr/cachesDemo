package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.example.cache.utils.KeysProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleCacheAsideCacheWithLFUReplacementPolicyTest {

    @Test
    @DisplayName("Should remove Least frequently accessed item when at max capacity")
    void test00() {
        //given
        Cache<Customer> cache = new SimpleCacheAsideCacheWithLFUReplacementPolicy();
        List<String> keys = KeysProvider.get20Keys();
        for (String key : keys) {
            cache.put(key, new Customer("firstName", "lastName", key));
        }
        List<String> usedKeys = keys.subList(0, 19);
        String unusedKey = keys.get(19);
        for (String key : usedKeys) {
            cache.get(key);
        }
        cache.put("some@test.com", new Customer("firstName", "lastName", "some@test.com"));
        //when
        Optional<Customer> customer = cache.get(unusedKey);
        //then
        assertTrue(customer.isEmpty());
    }

}