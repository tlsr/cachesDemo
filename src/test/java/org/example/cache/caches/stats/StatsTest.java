package org.example.cache.caches.stats;

import org.example.cache.caches.Cache;
import org.example.cache.entities.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class StatsTest {

    @Autowired
    private ApplicationContext applicationContext;

    private Customer CUSTOMER = new Customer("firstName", "lastName", "email");


    static List<WithStats> getAllMyServiceImplementations(ApplicationContext context) {
        return context
            .getBeansOfType(WithStats.class)
            .values()
            .stream()
            .filter(e -> e instanceof Cache<?>)
            .toList();
    }

    @AfterEach
    void tearDown(ApplicationContext context) throws Exception {
        context
            .getBeansOfType(WithStats.class)
            .values()
            .stream()
            .filter(e -> e instanceof Cache<?>)
            .forEach(e -> {
                ((Cache) e).clear();
                e.resetStats();
            });
    }


    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Should return size 0 on empty cache")
    void test0(WithStats cacheWithStats) {
        //given
        //when
        int size = cacheWithStats.size();
        //then
        assertEquals(0, size);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Should increase size by one when item added to cache")
    void test(WithStats cacheWithStats) {
        //given
        ((Cache) cacheWithStats).put("key", CUSTOMER);
        //when
        int size = cacheWithStats.size();
        //then
        assertEquals(1, size);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("WithStats.capacity should be equal Cache.getCapacity")
    void test2(WithStats cacheWithStats) {
        //given
        int capacity1 = ((Cache) cacheWithStats).getCapacity();
        //when
        int capacity2 = cacheWithStats.capacity();
        //then
        assertEquals(capacity1, capacity2);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Hit count should be zero after resetStats")
    void test3(WithStats cacheWithStats) {
        //given
        cacheWithStats.resetStats();
        //when
        int hits = cacheWithStats.hitCount();
        //then
        assertEquals(0, hits);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Miss count should be zero after resetStats")
    void test4(WithStats cacheWithStats) {
        //given
        cacheWithStats.resetStats();
        //when
        int misses = cacheWithStats.missCount();
        //then
        assertEquals(0, misses);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Eviction count should be zero after resetStats")
    void test5(WithStats cacheWithStats) {
        //given
        cacheWithStats.resetStats();
        //when
        int evictions = cacheWithStats.evictionCount();
        //then
        assertEquals(0, evictions);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Hit count should increase if cache was hit")
    void test6(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        cache.put("key", CUSTOMER);
        //when
        cache.get("key");
        int hits = cacheWithStats.hitCount();
        //then
        assertEquals(1, hits);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Miss count should increase if cache was missed")
    void test7(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        cache.get("key");
        //when
        int misses = cacheWithStats.missCount();
        //then
        assertEquals(1, misses);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Hit count should be 10 if cache was hit 10 times")
    void test8(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        cache.put("key", CUSTOMER);
        //when
        for (int i = 0; i < 10; i++) {
            cache.get("key");
        }
        int hits = cacheWithStats.hitCount();
        //then
        assertEquals(10, hits);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Miss count should be 10 if cache was missed 10 times")
    void test9(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        for (int i = 0; i < 10; i++) {
            cache.get("key");
        }
        //when
        int misses = cacheWithStats.missCount();
        //then
        assertEquals(10, misses);
    }


    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Hit rate should be 1 if one hit and zero misses")
    void test10(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        cache.put("key", CUSTOMER);
        cache.get("key");
        //when
        double hitRate = cacheWithStats.hitRate();
        //then
        assertEquals(1, hitRate, 0.01);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Hit rate should be 0 if zero hits and 1 miss")
    void test11(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        cache.get("key");
        //when
        double hitRate = cacheWithStats.hitRate();
        //then
        assertEquals(0, hitRate, 0.01);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Hit rate should be 0.5 if 5 hits and 5 misses")
    void test12(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        cache.put("key", CUSTOMER);
        cache.get("key");
        cache.get("key");
        cache.get("key");
        cache.get("key");
        cache.get("key");
        cache.get("key1");
        cache.get("key1");
        cache.get("key1");
        cache.get("key1");
        cache.get("key1");
        //when
        double hitRate = cacheWithStats.hitRate();
        //then
        assertEquals(0.5, hitRate, 0.01);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Eviction should be equal to 1 if one element was removed via remove() call")
    void test13(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        cache.put("key", CUSTOMER);
        cache.remove("key");
        //when
        int evictions = cacheWithStats.evictionCount();
        //then
        assertEquals(1, evictions);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Eviction should be equal to 1 if cache contained one element and clear() got called")
    void test14(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        cache.put("key", CUSTOMER);
        cache.clear();
        //when
        int evictions = cacheWithStats.evictionCount();
        //then
        assertEquals(1, evictions);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Eviction should be equal to 1 if one element got removed due to cache reaching max capacity")
    void test15(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        for (int i = 0; i < 20; i++) {
            cache.put("key" + i, CUSTOMER);
        }
        cache.put("key", CUSTOMER);
        //when
        int evictions = cacheWithStats.evictionCount();
        //then
        assertEquals(1, evictions);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Eviction should be equal to 20 if cache contained 20 elements and clear() got called")
    void test16(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        for (int i = 0; i < 20; i++) {
            cache.put("key" + i, CUSTOMER);
        }
        cache.clear();
        //when
        int evictions = cacheWithStats.evictionCount();
        //then
        assertEquals(20, evictions);
    }

    @ParameterizedTest
    @MethodSource("getAllMyServiceImplementations")
    @DisplayName("Eviction should be equal to 20 if 20 elements were inserted after cache already was at max capacity")
    void test17(WithStats cacheWithStats) {
        //given
        Cache cache = ((Cache) cacheWithStats);
        for (int i = 0; i < 20; i++) {
            cache.put("key" + i, CUSTOMER);
        }
        // overflowing
        for (int i = 0; i < 20; i++) {
            cache.put("anotherKey" + i, CUSTOMER);
        }
        //when
        int evictions = cacheWithStats.evictionCount();
        //then
        assertEquals(20, evictions);
    }

}
