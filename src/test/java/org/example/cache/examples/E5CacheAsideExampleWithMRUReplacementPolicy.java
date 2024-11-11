package org.example.cache.examples;

import org.example.cache.caches.SimpleCacheAsideCacheWithRandomReplacementPolicy;
import org.example.cache.configs.AspectsLoggersConfig;
import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.example.cache.service.CustomerService;
import org.example.cache.utils.KeysProvider;
import org.example.cache.utils.PrintUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class E5CacheAsideExampleWithMRUReplacementPolicy {

    @Autowired
    @Qualifier("MRUReplacement")
    private SimpleCacheAsideCacheWithRandomReplacementPolicy cache;
    @Autowired
    private AspectsLoggersConfig aspectsLoggersConfig;
    @Autowired
    private CustomerRepository customerRepository;

    CustomerService service;

    @BeforeEach
    void beforeEach() {
        aspectsLoggersConfig.disableAll();
        cache.clear();
        cache.resetStats();
        aspectsLoggersConfig.enableAll();
        service = new CustomerService(customerRepository, cache);
    }

    @AfterEach
    void afterEach() {
        aspectsLoggersConfig.enableAll();
    }

    /*
     * This example shows MRU(Most Recently used) replacement policy
     *
     * */
    @Test
    @DisplayName("MRU example")
    void test1() {
        aspectsLoggersConfig.disableAll();
        List<String> keys = KeysProvider.get20Keys();
        cache.printStats();
        PrintUtils.print("Getting 20 Customers via service for the first time");
        for (String key : keys) {
            service.fetchByEmail(key);
        }
        cache.printStats();
        PrintUtils.print("Getting 20 Customers via service second time");
        for (String key : keys) {
            service.fetchByEmail(key);
        }
        cache.printStats();
        String last = keys.getLast();
        PrintUtils.print("This email : " + last + " was fetched last so it is most recently used");
        PrintUtils.print("Getting one more Customer");
        aspectsLoggersConfig.enableAll();
        String keyThatNotInAList = KeysProvider.getKeyThatNotInAList();
        service.fetchByEmail(keyThatNotInAList);
        cache.printStats();

        PrintUtils.print("Reading: " + last + " directly from cache");
        Optional<Customer> leastRecentlyOpt = cache.get(last);
        PrintUtils.print("Reading: " + keyThatNotInAList + " directly from cache.");
        Optional<Customer> recentOneOpt = cache.get(keyThatNotInAList);
        //then
        assertTrue(leastRecentlyOpt.isEmpty());
        assertTrue(recentOneOpt.isPresent());
    }

}
