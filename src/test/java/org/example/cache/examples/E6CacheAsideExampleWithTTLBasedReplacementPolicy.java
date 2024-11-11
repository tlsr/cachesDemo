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
class E6CacheAsideExampleWithTTLBasedReplacementPolicy {

    @Autowired
    @Qualifier("TTLReplacement")
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
     * This example shows TTL(Time To Live) Based replacement policy
     *
     * */
    @Test
    @DisplayName("MRU example")
    void test1() throws InterruptedException {
        aspectsLoggersConfig.disableAll();
        List<String> keys = KeysProvider.get10Keys();
        cache.printStats();
        PrintUtils.print("Getting 10 Customers via service for the first time with 1s sleep between reads");
        for (String key : keys) {
            Thread.sleep(1000);
            service.fetchByEmail(key);
        }
        cache.printStats();
        PrintUtils.print("Getting 20 Customers via service second time without sleep");
        for (String key : keys) {
            service.fetchByEmail(key);
        }
        cache.printStats();
        String first = keys.getFirst();
        PrintUtils.print("This email : " + first + " was fetched first so it has earliest time stamp. So if we wait 15 more seconds it will be evicted");
        PrintUtils.print("Waiting 15s");
        Thread.sleep(15000);
        PrintUtils.print("Getting one more Customer");
        aspectsLoggersConfig.enableAll();
        String keyThatNotInAList = KeysProvider.getKeyThatNotInAList();
        service.fetchByEmail(keyThatNotInAList);
        cache.printStats();
        PrintUtils.print("Reading: " + first + " directly from cache");
        Optional<Customer> optionalCustomer = cache.get(first);
        cache.printStats();
        PrintUtils.print("Reading: " + keyThatNotInAList + " directly from cache.");
        Optional<Customer> recentOneOpt = cache.get(keyThatNotInAList);
        //then
        assertTrue(optionalCustomer.isEmpty());
        assertTrue(recentOneOpt.isPresent());
    }

}
