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
class E4CacheAsideExampleWithFIFOReplacementPolicy {

    @Autowired
    @Qualifier("FIFOReplacement")
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
     * This example shows FIFO(First-In,Last-Out) replacement policy
     *
     * */
    @Test
    @DisplayName("FIFO example")
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
        String firstIn = keys.get(0);
        PrintUtils.print("This email : " + firstIn + " was added first so it is \"First-In\"");
        PrintUtils.print("Getting one more Customer");
        aspectsLoggersConfig.enableAll();
        String keyThatNotInAList = KeysProvider.getKeyThatNotInAList();
        service.fetchByEmail(keyThatNotInAList);
        cache.printStats();

        PrintUtils.print("Reading: " + firstIn + " directly from cache");
        Optional<Customer> firstInOpt = cache.get(firstIn);
        PrintUtils.print("Reading: " + keyThatNotInAList + " directly from cache.");
        Optional<Customer> recentOneOpt = cache.get(keyThatNotInAList);
        //then
        assertTrue(firstInOpt.isEmpty());
        assertTrue(recentOneOpt.isPresent());
    }

}
