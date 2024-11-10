package org.example.cache.service;

import org.example.cache.entities.Customer;

import java.util.Optional;

public interface ICustomerService {

    Optional<Customer> fetchByEmail(String email);

    void save(Customer customer);

}
