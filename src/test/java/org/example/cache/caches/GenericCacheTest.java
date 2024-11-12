package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.example.cache.exceptions.CachingProjectException;
import org.example.cache.repo.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GenericCacheTest {

    static List<Cache<Customer>> getAllCacheImpls() {
        CustomerRepository repo = Mockito.mock(CustomerRepository.class);
        return List.of(new SimpleCacheAsideCacheWithRandomReplacementPolicy(),
            new SimpleCacheAsideCacheWithFIFOReplacementPolicy(),
            new SimpleCacheAsideCacheWithLFUReplacementPolicy(),
            new SimpleCacheAsideCacheWithLRUReplacementPolicy(),
            new SimpleCacheAsideCacheWithMRUReplacementPolicy(),
            new SimpleCacheAsideCacheWithTTLBasedReplacementPolicy(),
            new WriteThroughCache(repo),
            new WriteBackCache(repo));
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should return Optional.empty when call is performed to empty cache")
    void test(Cache<Customer> cache) {
        //given
        //when
        Optional<Customer> userOpt = cache.get("johnDoe@test.com");
        //then
        assertTrue(userOpt.isEmpty());
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should throw an exception if get is called with null key")
    void test1(Cache<Customer> cache) {
        //given
        //when
        assertThrows(CachingProjectException.class, () -> cache.get(null));
        //then
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should size should be one after one object added")
    void test2(Cache<Customer> cache) {
        //given
        //when
        cache.put("Doe@test.com", new Customer("John", "Doe", "Doe@test.com"));
        //then
        assertEquals(1, cache.getSize());
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should throw an exception if customer email is null")
    void test3(Cache<Customer> cache) {
        //given
        Customer customer = new Customer("John", "Doe", null);
        //when
        assertThrows(CachingProjectException.class, () -> cache.put(null, customer));
        //then
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should retrieve same object after object was added")
    void test4(Cache<Customer> cache) {
        //given

        Customer customer = new Customer("John", "Doe", "Doe@test.com");
        cache.put("Doe@test.com", customer);
        //when
        Optional<Customer> retrieved = cache.get("Doe@test.com");
        //then
        assertTrue(retrieved.isPresent());
        assertEquals(customer, retrieved.get());
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should clear cache")
    void test5(Cache<Customer> cache) {
        //given
        Customer customer = new Customer("John", "Doe", "Doe@test.com");
        cache.put("Doe@test.com", customer);
        //when
        cache.clear();
        //then
        assertEquals(0, cache.getSize());
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should remove entity by key")
    void test6(Cache<Customer> cache) {
        //given
        Customer customer1 = new Customer("John", "Doe", "Doe@test.com");
        Customer customer2 = new Customer("Mary", "Sue", "Sue@test.com");
        cache.put("Doe@test.com", customer1);
        cache.put("Sue@test.com", customer2);
        //when
        cache.remove("Doe@test.com");
        //then
        assertEquals(1, cache.getSize());
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should throw an exception if null passed to remove method")
    void test7(Cache<Customer> cache) {
        //given
        //when
        assertThrows(CachingProjectException.class, () -> cache.remove(null));
        //then
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should update entry in cache after put")
    void test8(Cache<Customer> cache) {
        //given
        Customer customer = new Customer("John", "Doe", "Doe@test.com");
        Customer updatedCustomer = new Customer("Jonathan", "Doe", "Doe@test.com");
        cache.put("Doe@test.com", customer);
        //when
        cache.put("Doe@test.com", updatedCustomer);
        //then
        Optional<Customer> fromCache = cache.get("Doe@test.com");
        assertTrue(fromCache.isPresent());
        assertEquals(updatedCustomer, fromCache.get());
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Size should not be greater than capacity")
    void test9(Cache<Customer> cache) {
        //given
        for (int i = 0; i < Cache.CAPACITY; i++) {
            cache.put("Doe" + i + "@test.com", new Customer("John", "Doe", "Doe" + i + "@test.com"));
        }
        Customer customer = new Customer("Mary", "Sue", "Sue@test.com");
        //when
        cache.put("Sue@test.com", customer);
        //then
        assertEquals(Cache.CAPACITY, cache.getSize());
    }

    @ParameterizedTest
    @MethodSource("getAllCacheImpls")
    @DisplayName("Should add new entry if on max capacity")
    void test10(Cache<Customer> cache) {
        //given
        for (int i = 0; i < Cache.CAPACITY; i++) {
            cache.put("Doe" + i + "@test.com", new Customer("John", "Doe", "Doe" + i + "@test.com"));
        }
        Customer customer = new Customer("Mary", "Sue", "Sue@test.com");
        //when
        cache.put("Sue@test.com", customer);
        //then
        Optional<Customer> fromCache = cache.get("Sue@test.com");
        assertTrue(fromCache.isPresent());
        assertEquals(customer, fromCache.get());
    }

}
