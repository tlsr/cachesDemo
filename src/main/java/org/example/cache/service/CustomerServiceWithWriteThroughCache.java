package org.example.cache.service;

import org.example.cache.caches.WriteThroughCache;
import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceWithWriteThroughCache implements ICustomerService {

    private final CustomerRepository repo;
    private final WriteThroughCache cache;

    public CustomerServiceWithWriteThroughCache(CustomerRepository repo,
                                                @Qualifier("WriteThrough") WriteThroughCache cache) {
        this.repo = repo;
        this.cache = cache;
    }

    @Override
    public Optional<Customer> fetchByEmail(String email) {
        return Optional.ofNullable(repo.findByEmail(email));
    }

    @Override
    public void save(Customer customer) {
        cache.put(customer.getEmail(), customer);
    }

}
