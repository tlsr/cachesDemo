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
class E2CacheAsideExampleWithLFUReplacementPolicy {

    @Autowired
    @Qualifier("LFUReplacement")
    private SimpleCacheAsideCacheWithRandomReplacementPolicy cache;
    @Autowired
    private AspectsLoggersConfig aspectsLoggersConfig;
    @Autowired
    private CustomerRepository customerRepository;

    CustomerService service;

    @BeforeEach
    void beforeEach() {
        cache.clear();
        cache.resetStats();
        service = new CustomerService(customerRepository, cache);
    }

    @AfterEach
    void afterEach() {
        aspectsLoggersConfig.enableAll();
    }

    /*
     * This example shows LFU(Least Frequently used) replacement policy
     *
     * */
    @Test
    @DisplayName("LFU example")
    void test1() {
        aspectsLoggersConfig.disableAll();
        List<String> keys = KeysProvider.get20Keys();
        cache.printStats();
        PrintUtils.print("Getting 20 Customers via service for the first time");
        for (String key : keys) {
            service.fetchByEmail(key);
        }
        List<String> usedKeys = keys.subList(0, 19);
        String unusedKey = keys.get(19);
        cache.printStats();
        PrintUtils.print("Emulating different amount of gets for 19 of them");
        for (int i = 0; i < usedKeys.size(); i++) {
            for (int j = 0; j < i + 1; j++) {
                service.fetchByEmail(usedKeys.get(i));
            }
        }
        cache.printStats();
        aspectsLoggersConfig.enableAll();
        PrintUtils.print("Getting user that not in cache at max cache capacity to force eviction");
        String newCustomerEmail = "LashaunBernier157@test.com";
        service.fetchByEmail(newCustomerEmail);
        cache.printStats();
        Optional<Customer> unusedCustomerOpt = cache.get(unusedKey);
        Optional<Customer> newCustomerOpt = cache.get(newCustomerEmail);
        //then
        assertTrue(unusedCustomerOpt.isEmpty());
        assertTrue(newCustomerOpt.isPresent());
    }

}
