package org.example.cache.examples;

import org.example.cache.caches.SimpleCacheAsideCacheWithRandomReplacementPolicy;
import org.example.cache.configs.AspectsLoggersConfig;
import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.example.cache.service.CustomerService;
import org.example.cache.utils.PrintUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class E2CacheAsideExampleWithLRUReplacementPolicy {

    @Autowired
    @Qualifier("LRUReplacement")
    private SimpleCacheAsideCacheWithRandomReplacementPolicy cache;
    @Autowired
    private AspectsLoggersConfig aspectsLoggersConfig;

    @Autowired
    CustomerService service;

    @Autowired
    CustomerRepository customerRepository;

    @AfterEach
    void afterEach() {
        aspectsLoggersConfig.enableAll();
    }

    /*
     * This example shows how bad implementation of cache-aside pattern can lead to stale data reads.
     * For a sake of simplicity I'm modifying object using repo.
     * */
    @Test
    @DisplayName("Cache-aside stale data example")
    void test1() {
        //given
        //when
        aspectsLoggersConfig.disableLogsAroundMethods();
        PrintUtils.print("Fetching object from service");
        service.fetchByEmail("JammieRyan61@test.com").get();
        PrintUtils.print("Fetched object from repo directly");
        Customer customerFromRepo = customerRepository.findByEmail("JammieRyan61@test.com");
        PrintUtils.print("Modifying object using repo");
        customerFromRepo.setFirstName("JammieRyan");
        PrintUtils.print("Saving object via repo");
        Customer afterSave = customerRepository.save(customerFromRepo);
        PrintUtils.print("Fetching object second time");
        Optional<Customer> customerFromServiceOpt = service.fetchByEmail("JammieRyan61@test.com");
        cache.printStats();
        //then
        assertEquals(afterSave, customerFromServiceOpt.get());
    }

}
