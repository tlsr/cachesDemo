package org.example.cache.examples;

import org.example.cache.caches.WriteBackCache;
import org.example.cache.configs.AspectsLoggersConfig;
import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.example.cache.service.CustomerServiceWithWriteBackCache;
import org.example.cache.utils.PrintUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class E8WriteBackCache {

    @Autowired
    @Qualifier("WriteBack")
    private WriteBackCache cache;
    @Autowired
    private AspectsLoggersConfig aspectsLoggersConfig;
    @Autowired
    private CustomerRepository customerRepository;

    CustomerServiceWithWriteBackCache service;

    @BeforeEach
    void beforeEach() {
        aspectsLoggersConfig.disableAll();
        cache.clear();
        cache.resetStats();
        aspectsLoggersConfig.enableAll();
        service = new CustomerServiceWithWriteBackCache(customerRepository, cache);
    }

    @AfterEach
    void afterEach() {
        aspectsLoggersConfig.enableAll();
    }

    /*
     * This example shows Write Through cache example
     *
     * */
    @Test
    @DisplayName("Write Back example")
    void test1() {
        PrintUtils.print("Write Back threshold is: " + WriteBackCache.THRESHOLD);
        PrintUtils.print("Saving " + (WriteBackCache.THRESHOLD - 1) + " customers via Service!");
        for (int i = 0; i < WriteBackCache.THRESHOLD - 1; i++) {
            Customer customer = new Customer("John", "Doe", "john.doe" + i + "@example.com");
            service.save(customer);
        }
        cache.printStats();
        PrintUtils.print("Saving one more Customers via Service!");
        Customer customer = new Customer("John", "Doe", "john.doe@example.com");
        service.save(customer);
        cache.printStats();

    }

}
