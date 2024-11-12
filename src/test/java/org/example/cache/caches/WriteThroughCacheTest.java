package org.example.cache.caches;

import org.example.cache.caches.stats.CacheStats;
import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WriteThroughCacheTest {
    @Mock
    private CustomerRepository repo;
    @InjectMocks
    private WriteThroughCache cache;
    private Customer customer = new Customer("John", "Doe", "johnDoe@example.com");


    @Test
    @DisplayName("Size should be zero on empty cache")
    void test00() {
        //given
        //when
        int size = cache.getSize();
        //then
        assertEquals(0, size);
    }

    @Test
    @DisplayName("CacheStats should not be null")
    void test01() {
        //given
        //when
        CacheStats stats = cache.getCacheStats();
        //then
        assertNotNull(stats);
    }


    @Test
    @DisplayName("Should put element to cache")
    void test02() {
        //given
        //when
        cache.put(customer.getEmail(),customer);
        //then
        assertEquals(1, cache.getSize());
    }


    @Test
    @DisplayName("Should write element to db upon put")
    void test03() {
        //given
        //when
        cache.put(customer.getEmail(),customer);
        //then
        verify(repo).save(customer);
    }


    @Test
    @DisplayName("Should clear cache")
    void test04() {
        //given
        cache.put(customer.getEmail(),customer);
        //when
        cache.clear();
        //then
        assertEquals(0, cache.getSize());
    }


    @Test
    @DisplayName("Should get element from cache")
    void test05() {
        //given
        //when
        //then
    }
}