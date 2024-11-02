package org.example.cache.service;

import org.example.cache.entities.Customer;
import org.example.cache.repo.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository repo;

    public CustomerService(CustomerRepository repo) {
        this.repo = repo;
    }


    public List<Customer> getCustomerByLastName(String lastName) {
        return repo.findByLastName(lastName);
    }
}
