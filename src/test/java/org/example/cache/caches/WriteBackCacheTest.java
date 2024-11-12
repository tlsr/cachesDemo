package org.example.cache.caches;

import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WriteBackCacheTest {

    @Mock
    private CustomerRepository repo;
    @InjectMocks
    private WriteBackCache cache;

    @Test
    @DisplayName("Should write elements to db upon reaching threshold")
    void test03() {
        //given
        ArrayList<Customer> customers = new ArrayList<>();
        for (int i = 0; i < WriteBackCache.THRESHOLD; i++) {
            Customer customer = new Customer("John" + i,
                "Doe" + i,
                "johnDoe" + i + "@example.com");
            customers.add(customer);
        }
        //when
        customers.forEach(c -> cache.put(c.getEmail(), c));
        //then
        ArgumentCaptor<Collection<Customer>> captor = ArgumentCaptor.forClass(Collection.class);
        verify(repo).saveAll(captor.capture());
        Collection<Customer> saved = captor.getValue();
        assertTrue(customers.containsAll(saved));
    }

}