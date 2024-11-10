package org.example.cache.service;

import org.example.cache.caches.SimpleCacheAsideCacheWithRandomReplacementPolicy;
import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService implements ICustomerService {
    private final CustomerRepository repo;
    private final SimpleCacheAsideCacheWithRandomReplacementPolicy<Customer> cache;

    public CustomerService(CustomerRepository repo, SimpleCacheAsideCacheWithRandomReplacementPolicy<Customer> cache) {
        this.repo = repo;
        this.cache = cache;
    }

    @Override
    public Optional<Customer> fetchByEmail(String email) {
        Optional<Customer> customerOpt = cache.get(email);
        if (customerOpt.isPresent()) {
            return customerOpt;
        } else {
            Customer customer = repo.findByEmail(email);
            if (customer != null) {
                cache.put(email, customer);
            }
            return Optional.ofNullable(customer);
        }
    }

    @Override
    public void save(Customer customer) {
        repo.save(customer);
    }

}
