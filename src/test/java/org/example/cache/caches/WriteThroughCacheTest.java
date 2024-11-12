package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WriteThroughCacheTest {
    @Mock
    private CustomerRepository repo;
    @InjectMocks
    private WriteThroughCache cache;
    private Customer customer = new Customer("John", "Doe", "johnDoe@example.com");

    @Test
    @DisplayName("Should write element to db upon put")
    void test03() {
        //given
        //when
        cache.put(customer.getEmail(),customer);
        //then
        verify(repo).save(customer);
    }

}