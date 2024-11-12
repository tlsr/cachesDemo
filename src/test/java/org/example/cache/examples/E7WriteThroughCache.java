package org.example.cache.examples;

import org.example.cache.caches.WriteThroughCache;
import org.example.cache.configs.AspectsLoggersConfig;
import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.example.cache.service.CustomerServiceWithWriteThroughCache;
import org.example.cache.utils.PrintUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class E7WriteThroughCache {

    @Autowired
    @Qualifier("WriteThrough")
    private WriteThroughCache cache;
    @Autowired
    private AspectsLoggersConfig aspectsLoggersConfig;
    @Autowired
    private CustomerRepository customerRepository;

    CustomerServiceWithWriteThroughCache service;

    @BeforeEach
    void beforeEach() {
        aspectsLoggersConfig.disableAll();
        cache.clear();
        cache.resetStats();
        aspectsLoggersConfig.enableAll();
        service = new CustomerServiceWithWriteThroughCache(customerRepository, cache);
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
    @DisplayName("Write Through example")
    void test1() {
        PrintUtils.print("Saving customer via Service!");
        Customer customer = new Customer("John", "Doe", "john.doe@example.com");
        service.save(customer);
    }

}
